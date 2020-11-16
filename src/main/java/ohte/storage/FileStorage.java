package ohte.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Storage implementation that uses a SQLite file as it's backend.
 */
public class FileStorage extends Storage {
  /**
   * JDBC {@link Connection} for the SQLite database.
   */
  Connection conn;

  /**
   * Path to the SQLite database on the disk.
   */
  String path;

  private FileStorage(Connection conn, String path) {
    this.conn = conn;
    this.path = path;
  }

  /**
   * Open an existing SQLite database.
   *
   * Does not create tables.
   */
  public static FileStorage open(String path) throws SQLException {
    String url = "jdbc:sqlite:" + path;
    Connection conn = DriverManager.getConnection(url);
    return new FileStorage(conn, path);
  }

  /**
   * Creates a new SQLite database.
   *
   * Creates all neccessary tables.
   */
  public static FileStorage create(String path) throws SQLException {
    String url = "jdbc:sqlite:" + path;
    Connection conn = DriverManager.getConnection(url);
    FileStorage file = new FileStorage(conn, path);

    file.initialize();

    return file;
  }

  @Override
  Connection getConnection() {
    return conn;
  }

  static class StorageObjectPopulator extends StorageObjectFieldVisitor {
    ResultSet results;

    StorageObjectPopulator(ResultSet results) {
      this.results = results;
    }

    @Override
    public void visitString(FieldValue<String> field) {
      try {
        String value = results.getString(field.name);
        System.out.println("Set " + field.name + " to " + value);
        field.set(value);
      } catch (SQLException sqle) {
      }
    }
  }

  static public <T> T populateFromRow(T object, ResultSet results) {
    StorageObjectPopulator populator = new StorageObjectPopulator(results);
    populator.visit(object);
    return object;
  }
}
