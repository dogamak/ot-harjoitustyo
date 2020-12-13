package ohte.storage;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;

/**
 * Persister implementation using SQLite as the backend.
 */
public abstract class SqlitePersister<T> implements Persister<T>, SetChangeListener<T> {
    /**
     * JDBC connection handle
     */
    Connection conn;

    /**
     * Create a persister from a JDBC connection handle.
     */
    SqlitePersister(Connection conn) {
        this.conn = conn;
    }

    /**
     * Register callbacks for the provided set and all it's entries
     * and populates the set with initial value from the persistent
     * storage.
     */
    public void synchronize(ObservableSet<T> collection) {
        try {
            createTables();

            List<T> entries = loadInitialEntries();

            for (T entry : entries) {
                registerEntryListeners(entry);
                collection.add(entry);
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }

        collection.addListener(this);
    }

    /**
     * Called whenever the observed collection changes.
     *
     * On entry additions calls {@link SqlitePersister#insertEntry}
     * and {@link SqlitePersister#registerEntryListeners}. On entry
     * removals calls {@link SqlitePersister#deleteEntry}. All of
     * these methods are implemented by the subclass.
     */
    public void onChanged(SetChangeListener.Change<? extends T> change) {
        try {
            if (change.wasAdded()) {
                T entry = change.getElementAdded();
                insertEntry(entry);
                registerEntryListeners(entry);
            } else if (change.wasRemoved()) {
                deleteEntry(change.getElementRemoved());
            }
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    /**
     * Register change listeners for all properties of an entry.
     */
    abstract void registerEntryListeners(T entry);

    /**
     * Create the tables required for storing the collection entries.
     */
    abstract void createTables() throws SQLException;

    /**
     * Handle inserting a new entry to the database.
     */
    abstract void insertEntry(T entry) throws SQLException;

    /**
     * Handle deleting an existing entry from the database.
     */
    abstract void deleteEntry(T entry) throws SQLException;

    /**
     * Query the set of initial entries from the database.
     */
    abstract List<T> loadInitialEntries() throws SQLException;
}
