package ohte.storage;

import java.sql.Connection;

/**
 * Common base class for all stores.
 */
abstract class Store {
  /**
   * Provides a database connection handle.
   *
   * This method is implemented by the concrete storage implementation,
   * such as {@link FileStorage}.
   */
  abstract Connection getConnection();
}
