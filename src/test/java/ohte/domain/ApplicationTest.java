package ohte.domain;

import org.junit.*;
import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.mockito.ArgumentMatchers.*;
import org.mockito.ArgumentMatcher;

import java.io.File;
import java.io.IOException;
import java.util.function.Function;
import ohte.storage.Storage;

public class ApplicationTest {
  Storage storage;
  Application app;
  File tmpFile;

  @Before
  public void createApplication() throws IOException {
    storage = mock(Storage.class);
    app = new Application(storage);
    tmpFile = File.createTempFile("test", "inv");
  }

  @After
  public void removeTemporaryFile() {
    tmpFile.delete();
  }

  private Account accountWithRole(Account.Role role) {
    return argThat((acc) -> acc.getRole().equals(role));
  }

  @Test
  public void creatingInventoryAddsPersistersToTheStorage() throws IOException {
    app.createInventory(tmpFile.getAbsolutePath(), new Credentials("", ""));
    verify(storage, times(1)).synchronizeAccounts(any()); 
    verify(storage, times(1)).synchronizeAssets(any()); 
  }

  @Test
  public void creatingInventoryCreatesSuperuser() {
    app.createInventory(tmpFile.getAbsolutePath(), new Credentials("", ""));
    verify(storage, times(1)).saveAccount(accountWithRole(Account.Role.SUPERUSER));
  }

  @Test
  public void createAccountCreatesNormalAccount() {
    app.createAccount(new Credentials("asd", "asd"));
    verify(storage, times(1)).saveAccount(accountWithRole(Account.Role.NORMAL));
  }

  @Test
  public void openingInventoryChecksAccountsPassword() {
    Account account = mock(Account.class);
    when(storage.getAccountByUsername("root")).thenReturn(account);

    app.openInventory(tmpFile.getAbsolutePath(), new Credentials("root", "root"));

    verify(account, times(1)).checkPassword("root");
  }
}
