package ohte.storage;

import javafx.collections.ObservableSet;

/**
 * Interface for implementations which are capable of
 * synchronizing an observable collection into persistent
 * storage.
 */
public interface Persister<T> {
    /**
     * Populate the collection with existing entries from the persistent storage
     * and register listeners to the collection for synchronizing future operations
     * to the persistent storage.
     *
     * @param collection The obeservable collection to synchronize
     */
    void synchronize(ObservableSet<T> collection);
}
