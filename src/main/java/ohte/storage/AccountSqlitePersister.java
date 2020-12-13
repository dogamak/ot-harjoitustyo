package ohte.storage;

import ohte.domain.Account;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ArrayList;
import java.util.List;

import javafx.collections.ObservableSet;

/**
 * A {@link Persister} implementation for {@link Account Accounts} using SQLite as it's backend.
 */
public class AccountSqlitePersister extends SqlitePersister<Account> {
    /** {@inheritDoc} */
    public AccountSqlitePersister(Connection conn) {
      super(conn);
    }

    /**
     * Creates the SQLite table holding the accounts' data.
     */
    @Override
    void createTables() throws SQLException {
        try (Statement stmt = conn.createStatement()) {
            stmt.execute(
                "CREATE TABLE IF NOT EXISTS accounts (" +
                "username TEXT UNIQUE NOT NULL," +
                "role TEXT NOT NULL," +
                "password TEXT NOT NULL" +
                ")"
            );
        }
    }

    /**
     * Imports all accounts from the database.
     */
    @Override
    List<Account> loadInitialEntries() throws SQLException {
        ArrayList<Account> accounts = new ArrayList<>();

        ResultSet results = conn.createStatement()
            .executeQuery("SELECT * FROM accounts");

        while (results.next()) {
            Account account = new Account(results.getString("username"));
            account.setRole(Account.Role.fromString(results.getString("role")));
            account.setPasswordHash(results.getString("password"));

            accounts.add(account);
        }

        return accounts;
    }

    /**
     * There are no editable fields in accounts currently.
     */
    @Override
    void registerEntryListeners(Account entry) {}

    /**
     * Saves a new account into the database.
     */
    @Override
    void insertEntry(Account account) throws SQLException {
        PreparedStatement stmt = conn
            .prepareStatement("INSERT INTO accounts (username, role, password) VALUES (?,?,?)");

        stmt.setString(1, account.getUsername());
        stmt.setString(2, account.getRole().toString());
        stmt.setString(3, account.getPasswordHash());

        stmt.executeUpdate();
    }

    /**
     * Removes an account from the database permanently.
     */
    @Override
    void deleteEntry(Account deleted) throws SQLException {
        PreparedStatement stmt = conn
            .prepareStatement("DELETE FROM accounts WHERE username = ?");

        stmt.setString(1, deleted.getUsername());

        stmt.executeUpdate();
    }
}
