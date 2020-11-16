package ohte.storage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 * An in-memory implementation primarily for testing.
 */
public class MemoryStorage extends Storage {
  /**
   * JDBC {@link Connection} for the in-memory SQLite database.
   */
  Connection conn;

  /**
   * Creates a new in-memory storage.
   */
  public MemoryStorage() throws SQLException {
    conn = DriverManager.getConnection("jdbc:sqlite::memory:");
    initialize();
  }

  @Override
  Connection getConnection() {
    return conn;
  }
}
