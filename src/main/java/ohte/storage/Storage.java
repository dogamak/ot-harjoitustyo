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
 * Abstract class for storage implementations.
 *
 * Implementors of this class need to provide an implementation
 * for {@link Store#getConnection} and make sure to call {@link #initialize}
 * when creating a new storage.
 */
public class Storage {
    ObservableSet<Account> accountSet = FXCollections.observableSet();
    ObservableSet<Asset> assetSet = FXCollections.observableSet();

    public void synchronize(Persister<Account> persister) {
        persister.synchronize(accountSet);
    }

    public void saveAccount(Account account) {
        accountSet.add(account);
    }

    public void removeAccount(Account account) {
        accountSet.remove(account);
    }

    public Account getAccountByUsername(String username) {
        return accountSet
            .stream()
            .filter(account -> account.getUsername().equals(username))
            .findAny()
            .orElse(null);
    }

    public List<Account> getAccounts() {
        return accountSet.stream().collect(Collectors.toList());
    }

    public ObservableSet<Account> getAccountsObservable() {
        return accountSet;
    }

    public void saveAsset(Asset asset) {
        assetSet.add(asset);
    }

    public List<Asset> getAssets() {
        return assetSet.stream().collect(Collectors.toList());
    }

    public ObservableSet<Asset> getAssetsObservable() {
        return assetSet;
    }

    public interface Type<T, Self> {
        T convertTo();
        Self convertFrom(T value);
    }
}
