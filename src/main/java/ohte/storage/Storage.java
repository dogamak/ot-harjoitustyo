package ohte.storage;

import java.lang.reflect.Field;

import java.sql.SQLException;

/**
 * Abstract class for storage implementations.
 *
 * Implementors of this class need to provide an implementation
 * for {@link Store#getConnection} and make sure to call {@link #initialize}
 * when creating a new storage.
 */
public abstract class Storage extends AccountStore {
  /**
   * Initialize a new storage.
   *
   * The concrete implementation of this class needs to call
   * this method when creating a new storage.
   */
  void initialize() throws SQLException {
    AccountStore.initialize(this);
  }

  public interface Type<T, Self> {
    T convertTo();
    Self convertFrom(T value);
  }
}
