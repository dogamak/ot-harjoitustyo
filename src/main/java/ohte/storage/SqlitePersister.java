package ohte.storage;

import java.sql.SQLException;
import java.sql.Connection;
import java.sql.Statement;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.List;

import javafx.collections.ObservableSet;
import javafx.collections.SetChangeListener;

public abstract class SqlitePersister<T> implements Persister<T>, SetChangeListener<T> {
    Connection conn;

    SqlitePersister(Connection conn) {
        this.conn = conn;
    }

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

    abstract void registerEntryListeners(T entry);
    abstract void createTables() throws SQLException;
    abstract void insertEntry(T entry) throws SQLException;
    abstract void deleteEntry(T entry) throws SQLException;
    abstract List<T> loadInitialEntries() throws SQLException;
}
