package ohte.storage;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;

import java.sql.SQLException;

import org.junit.*;
import static org.junit.Assert.*;

public class FileStorageTest {
  static class AccountTestVisitor extends StorageObjectFieldVisitor {
    String table;
    int columnCount = 0;
    ArrayList<String> columnNames = new ArrayList<>();
    ArrayList<String> escapedValues = new ArrayList<>();

    AccountTestVisitor(String table) {
      this.table = table;
    }

    public void visitString(String field, String value) {
      columnCount++;
      columnNames.add(field);

      if (value == null) {
        escapedValues.add("NULL");
      } else {
        escapedValues.add("'" + value.replaceAll("'", "''").replaceAll("\"", "\"\"") + "'");
      }
    }

    public void visitBoolean(String field, boolean bool) {
      columnCount++;
      columnNames.add(field);
      escapedValues.add(bool ? "1" : "0");
    }

    public void visitInteger(String field, int integer) {
      columnCount++;
      columnNames.add(field);
      escapedValues.add(String.valueOf(integer));
    }

    String build() {
      return "INSERT INTO " +
        table + " (" +
        String.join(", ", columnNames) +
        ") VALUES (" +
        String.join(", ", escapedValues) +
        ")";
    }
  }

  @Test
  public void reflectionTest() {
    Account account = new Account("user");
    AccountTestVisitor visitor = new AccountTestVisitor("accounts");
    visitor.visit(account);
    System.out.println(visitor.build());
  }

  @Test
  public void createFileStorage() throws IOException, SQLException {
    File tmp = File.createTempFile("inventory", null);
    FileStorage.create(tmp.getAbsolutePath());
  }

  @Test
  public void openFileStorage() throws SQLException, IOException {
    File tmp = File.createTempFile("inventory", null);
    FileStorage.create(tmp.getAbsolutePath());
    FileStorage.open(tmp.getAbsolutePath());
  }
}
