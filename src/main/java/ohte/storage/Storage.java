package ohte.storage;

import ohte.domain.Account;
import ohte.domain.Asset;
import java.lang.reflect.Field;
import java.util.stream.Collectors;
import java.util.List;
import javafx.collections.FXCollections;
import javafx.collections.ObservableSet;

import java.sql.SQLException;

/**
 * Class for storing application date in-memory and synchronizing
 * that data into persistent storage using {@link Persister} implementations.
 */
public class Storage {
    /**
     * An observable and modifiable set containing all account.
     */
    ObservableSet<Account> accountSet = FXCollections.observableSet();

    /**
     * An observable and modifiable set containing all assets.
     */
    ObservableSet<Asset> assetSet = FXCollections.observableSet();

    /**
     * Synchronize the accounts with a storage implementation.
     *
     * @param persister A storage implementation capable of storing {@link Account Accounts}
     */
    public void synchronizeAccounts(Persister<Account> persister) {
        persister.synchronize(accountSet);
    }

    /**
     * Synchronize the assets with an storage implementation.
     *
     * @param persister A storage implementation capable of storing {@link Asset Assets}
     */
    public void synchronizeAssets(Persister<Asset> persister) {
        persister.synchronize(assetSet);
    }

    /**
     * Adds a new account to the global account list.
     *
     * If a {@link Persister} has been defined for accounts,
     * the account is also saved to pesistent memory.
     *
     * @param account The account to be saved
     */
    public void saveAccount(Account account) {
        accountSet.add(account);
    }

    /**
     * Removes an account from the global account list.
     *
     * If a {@link Persister} has been defined for accounts,
     * the account is also removed from pesistent memory.
     *
     * @param account The account to be removed
     */
    public void removeAccount(Account account) {
        accountSet.remove(account);
    }

    /**
     * Searches the global account list for an account with the specified username.
     *
     * @param username The username to look for
     *
     * @return The matching account if found, {@code null} otherwise.
     */
    public Account getAccountByUsername(String username) {
        return accountSet
            .stream()
            .filter(account -> account.getUsername().equals(username))
            .findAny()
            .orElse(null);
    }

    /**
     * Gets a copied and static list of all current accounts.
     * Note that only the list is copied and static, the accounts
     * themselves can still be modiefied.
     *
     * @return List of all current accounts
     */
    public List<Account> getAccounts() {
        return accountSet.stream().collect(Collectors.toList());
    }

    /**
     * Gets an observable list of all accounts.
     *
     * @return An observable set of all accounts
     */
    public ObservableSet<Account> getAccountsObservable() {
        return accountSet;
    }

    /**
     * Adds a new asset to the global account list.
     *
     * If a {@link Persister} has been defined for assets,
     * the asset is also saved to pesistent memory.
     *
     * @param asset Asset to be saved
     */
    public void saveAsset(Asset asset) {
        assetSet.add(asset);
    }

    /**
     * Gets a copied and static list of all current assets.
     * Note that only the list is copied and static, the assets
     * themselves can still be modiefied.
     *
     * @return A list of all current assets
     */
    public List<Asset> getAssets() {
        return assetSet.stream().collect(Collectors.toList());
    }

    /**
     * Gets an observable list of all assets.
     *
     * @return An observable set of all assets
     */
    public ObservableSet<Asset> getAssetsObservable() {
        return assetSet;
    }

    /**
     * Removes an asset from the global asset list.
     *
     * If a {@link Persister} has been defined for assets,
     * the asset is also removed from pesistent memory.
     *
     * @param asset Asset to be removed
     */
    public void removeAsset(Asset asset) {
      assetSet.remove(asset);
    }
}
