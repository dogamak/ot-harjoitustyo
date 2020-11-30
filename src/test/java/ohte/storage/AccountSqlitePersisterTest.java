package ohte.storage;

import java.io.File;
import java.io.IOException;
import java.sql.SQLException;
import java.sql.Connection;
import java.sql.DriverManager;

import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

import org.junit.*;
import static org.junit.Assert.*;

import ohte.domain.Account;

public class AccountSqlitePersisterTest {
  ObservableSet<Account> set;
  AccountSqlitePersister persister;

  @Before
  public void initializePersister() throws SQLException {
    Connection conn = DriverManager.getConnection("jdbc:sqlite::memory:");
    persister = new AccountSqlitePersister(conn);
    set = FXCollections.observableSet();
    persister.synchronize(set);
  }

  @Test
  public void addingAnAccountToTheSetCausesAnRowToBeInserted() throws SQLException {
    Account account = new Account("username");
    account.setPassword("asd");
    set.add(account);

    int rowCount = persister.getConnection()
      .createStatement()
      .executeQuery("SELECT COUNT(*) FROM accounts")
      .getInt(1);

    assertEquals(1, rowCount);
  }

  @Test
  public void removingAnAccountFromTheSetCausesAnRowToBeDeleted() throws SQLException {
    Account account = new Account("username");
    account.setPassword("asd");
    set.add(account);

    int rowCount = persister.getConnection()
      .createStatement()
      .executeQuery("SELECT COUNT(*) FROM accounts")
      .getInt(1);

    assertEquals(1, rowCount);

    set.remove(account);

    rowCount = persister.getConnection()
      .createStatement()
      .executeQuery("SELECT COUNT(*) FROM accounts")
      .getInt(1);

    assertEquals(0, rowCount);
  }

  @Test
  public void existingAccountsAreLoadedFromTheDatabase() throws IOException, SQLException {
    File tmpFile = File.createTempFile("test", "sqlite");
    String connectionString = "jdbc:sqlite:" + tmpFile.getAbsolutePath();

    try (Connection conn = DriverManager.getConnection(connectionString)) {
      persister = new AccountSqlitePersister(conn);
      set = FXCollections.observableSet();
      persister.synchronize(set);

      Account account = new Account("username");
      account.setPassword("asd");
      set.add(account);
    }

    try (Connection conn = DriverManager.getConnection(connectionString)) {
      persister = new AccountSqlitePersister(conn);
      ObservableSet<Account> newSet = FXCollections.observableSet();
      persister.synchronize(newSet);
      assertEquals(1, newSet.size());
    }
  }
}
