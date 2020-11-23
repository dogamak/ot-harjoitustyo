package ohte.storage;

import javafx.collections.ObservableSet;

public interface Persister<T> {
  void synchronize(ObservableSet<T> collection);
}
