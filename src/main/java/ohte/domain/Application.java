package ohte.domain;

import java.sql.SQLException;

import javafx.beans.value.ObservableValue;
import javafx.beans.property.SimpleObjectProperty;

import ohte.domain.Account;
import ohte.storage.Storage;
import ohte.storage.AccountSqlitePersister;

/**
 * Class containing the application state and core bussines logic.
 *
 * Global instance of this class can be accessed with {@link #getSingleton}.
 * This global instance is used by the UI. Other instances of the class
 * can be created for testing purposes or for multiple instances of the
 * application.
 */
public class Application {
    /**
     * Global instance of Application.
     */
    private static Application singleton;

    /**
     * Storage backend of the application.
     */
    Storage storage = new Storage();

    /**
     * Current authenticated account.
     */
    Account account;

    /**
     * Current globally focused object.
     *
     * @see #getFocused
     */
    SimpleObjectProperty<Object> focusedObject = new SimpleObjectProperty(null);

    /**
     * Returns the global instance of {@link Application}.
     * 
     * Initializes the instance if not already initialized.
     *
     * @see #singleton
     */
    public static Application getSingleton() {
        if (singleton == null) {
            singleton = new Application();
        }

        return singleton;
    }

    /**
     * Returns true if user has provided correct credentials and is authenticated.
     */
    public boolean isAuthenticated() {
        return account != null;
    }

    /**
     * Gets the current authenticated {@link Account}.
     *
     * If the user has not authenticated, returns null.
     *
     * @see #isAuthenticated
     */
    public Account getAccount() {
        return account;
    }

    /**
     * Get a reference to the {@link Storage} backend implementation.
     *
     * @see Storage
     */
    public Storage getStorage() {
        return storage;
    }

    /**
     * Get an observable for the globally focused object.
     *
     * <p>The globally focused object is usually the object user has most recently
     * interacted with, however this package does not regard itself with when the
     * UI implementation changes the focused object.
     *
     * <p>The {@link SimpleObjectProperty} type provides methods for registering
     * callbacks, which are triggered whenever the value changes.
     *
     * <p>The globally focused object can be any object. The consumer of this object
     * needs to determine the type of this object with `instanceof` or some other
     * method.
     *
     * @see #focusedObject
     */
    public SimpleObjectProperty<Object> getFocused() {
        return focusedObject;
    }

    /**
     * Creates an unauthenticated instance with no associated storage backend.
     *
     * @see #createInventory
     * @see #openInventory
     */
    public Application() {}

    private void synchronizeToFile(String path) {
        try {
            storage.synchronize(new AccountSqlitePersister(path));
        } catch (SQLException sqle) {
            sqle.printStackTrace();
        }
    }

    /**
     * Creates a new inventory and a superuser account with the provided credentials.
     *
     * The created superuser account is set as the authenticated account.
     *
     * @param path        Path and filename for the inventory file to be created.
     * @param credentials Credentials for creating the superuser account.
     *
     * @see FileStorage#create
     */
    public void createInventory(String path, Credentials credentials) {
        synchronizeToFile(path);

        Account superuser = new Account(credentials.getUsername());
        superuser.setRole(Account.Role.SUPERUSER);
        superuser.setPassword(credentials.getPassword());

        storage.saveAccount(superuser);
        account = superuser;
    }

    /**
     * Opens an existing inventory file and tries to authenticate with the
     * provided credentials.
     *
     * @param  path        Path to an existing inventory file.
     * @param  credentials Credentials for authentication.
     * @return             True, if authentication is successfull.
     *
     * @see FileStorage#open
     */
    public boolean openInventory(String path, Credentials credentials) {
        synchronizeToFile(path);

        Account acc = storage.getAccountByUsername(credentials.getUsername());

        boolean result = acc.checkPassword(credentials.getPassword());

        if (result) {
            account = acc;
        }

        return result;
    }

    public void createAccount(Credentials credentials) {
        Account normal = new Account(credentials.getUsername());
        normal.setRole(Account.Role.NORMAL);
        normal.setPassword(credentials.getPassword());

        storage.saveAccount(normal);
    }

    public void removeAccount(Account account) {
        storage.removeAccount(account);
    }

    public void createAsset() {
        storage.saveAsset(new Asset());
    }

    public void removeAsset(Asset asset) {
      storage.removeAsset(asset);
    }
}
