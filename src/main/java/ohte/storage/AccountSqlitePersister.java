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

public class AccountSqlitePersister
implements Persister<Account>, SetChangeListener<Account>
{
  Connection conn;

  public AccountSqlitePersister(String path) throws SQLException {
    conn = DriverManager.getConnection("jdbc:sqlite:" + path);
  }

  public void synchronize(ObservableSet<Account> collection) {
    createTables();

    try {
      loadInitialAccounts(collection);
    } catch (SQLException sqle) {
      sqle.printStackTrace();
    }

    collection.addListener(this);
  }

  public void onChanged(SetChangeListener.Change<? extends Account> change) {
    if (change.wasAdded()) {
      insertAccount(change.getElementAdded());
    } else if (change.wasRemoved()) {
      deleteAccount(change.getElementRemoved());
    }
  }

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

  private void deleteAccount(Account deleted) {}
}
