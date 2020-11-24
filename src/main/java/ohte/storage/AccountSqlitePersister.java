package ohte.storage;

import ohte.domain.Account;

import java.sql.SQLException;
import java.sql.DriverManager;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;

/**
 * Implementation for persisting account data in a SQLite database.
 */
public class AccountSqlitePersister
implements Persister<Account>, SetChangeListener<Account>
{
  /**
   * Handle to the SQLite database.
   */
  Connection conn;

  /**
   * Open or create a SQLite database at the given path.
   */
  public AccountSqlitePersister(String path) throws SQLException {
    conn = DriverManager.getConnection("jdbc:sqlite:" + path);
  }

  public Connection getConnection() {
    return conn;
  }

  /**
   * Called by {@link Storage} when this persister is added.
   *
   * Performs neccessary database setup and imports intial
   * account data from the database. Registers neccessary
   * callbacks.
   */
  public void synchronize(ObservableSet<Account> collection) {
    createTables();

    try {
      loadInitialAccounts(collection);
    } catch (SQLException sqle) {
      sqle.printStackTrace();
    }

    collection.addListener(this);
  }

  /**
   * Callback which is triggered whenever an account is deleted or created.
   */
  public void onChanged(SetChangeListener.Change<? extends Account> change) {
    if (change.wasAdded()) {
      insertAccount(change.getElementAdded());
    } else if (change.wasRemoved()) {
      deleteAccount(change.getElementRemoved());
    }
  }

  /**
   * Creates the SQLite table holding the accounts' data.
   */
  private void createTables() {
    try (Statement stmt = conn.createStatement()) {
      stmt.execute(
        "CREATE TABLE IF NOT EXISTS accounts (" +
          "username TEXT UNIQUE NOT NULL," +
          "role TEXT NOT NULL," +
          "password TEXT NOT NULL" +
        ")"
      );
    } catch (SQLException sqle) {
      sqle.printStackTrace();
    }
  }

  /**
   * Imports all accounts from the database.
   */
  private void loadInitialAccounts(ObservableSet<Account> collection) throws SQLException {
    ResultSet results = conn.createStatement()
      .executeQuery("SELECT * FROM accounts");

    while (results.next()) {
      Account account = new Account(results.getString("username"));
      account.setRole(Account.Role.fromString(results.getString("role")));
      account.setPasswordHash(results.getString("password"));

      collection.add(account);
    }
  }

  /**
   * Saves a new account into the database.
   */
  private void insertAccount(Account account) {
    try {
      PreparedStatement stmt = conn
        .prepareStatement("INSERT INTO accounts (username, role, password) VALUES (?,?,?)");

      stmt.setString(1, account.getUsername());
      stmt.setString(2, account.getRole().toString());
      stmt.setString(3, account.getPasswordHash());

      stmt.executeUpdate();
    } catch (SQLException sqle) {
      sqle.printStackTrace();
    }
  }

  /**
   * Removes an account from the database permanently.
   */
  private void deleteAccount(Account deleted) {
    try {
      PreparedStatement stmt = conn
        .prepareStatement("DELETE FROM accounts WHERE username = ?");

      stmt.setString(1, deleted.getUsername());

      stmt.executeUpdate();
    } catch (SQLException sqle) {
      sqle.printStackTrace();
    }
  }
}
