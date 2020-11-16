package ohte.storage;

import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

import java.util.List;
import java.util.ArrayList;

/**
 * This class contains code for retrieving and storing Accounts
 * to and from the disk.
 *
 * This is an abstract class and as such cannot be used directly.
 * These methods are accessible through the {@link InventoryFile} class.
 */
abstract class AccountStore extends Store {
  /**
   * Creates any tables required by this store.
   */
  static void initialize(Store store) throws SQLException {
    Statement stmt = store.getConnection().createStatement();

    stmt.execute(
      "CREATE TABLE IF NOT EXISTS accounts (" +
        "username TEXT UNIQUE NOT NULL," +
        "role TEXT NOT NULL," +
        "password TEXT NOT NULL" +
      ")"
    );
  }

  /**
   * Retrieves an account from the disk based on a given username.
   *
   * @returns An account, if one exists, otherwise returns null.
   */
  public Account getAccountByUsername(String username) throws SQLException {
    PreparedStatement stmt = getConnection()
      .prepareStatement("SELECT username, password, role FROM accounts WHERE username = ? LIMIT 1");

    stmt.setString(1, username);

    ResultSet result = stmt.executeQuery();

    return FileStorage.populateFromRow(new Account(), result);
  }

  /**
   * Retrieves a list of all defined accounts.
   */
  public List<Account> getAccounts() throws SQLException {
    ResultSet results = getConnection()
      .createStatement()
      .executeQuery("SELECT * FROM accounts");

    ArrayList<Account> accounts = new ArrayList<>();

    while (results.next()) {
      Account account = new Account(results.getString("username"));
      account.setRole(Account.Role.fromString(results.getString("role")));
      account.passwordHash = results.getString("password");

      accounts.add(account);
    }

    return accounts;
  }

  /**
   * Saves a new account to the disk.
   *
   * @throws SQLException if an account with the same name already exists
   */
  public void saveAccount(Account account) throws SQLException {
    PreparedStatement stmt = getConnection()
      .prepareStatement("INSERT INTO accounts (username, role, password) VALUES (?,?,?)");

    stmt.setString(1, account.getUsername());
    stmt.setString(2, account.getRole().toString());
    stmt.setString(3, account.passwordHash);

    stmt.executeUpdate();
  }
}
