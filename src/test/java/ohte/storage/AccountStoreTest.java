package ohte.storage;

import java.sql.SQLException;

import org.junit.*;
import static org.junit.Assert.*;

public class AccountStoreTest {
  Storage storage;

  @Before
  public void initStorage() throws SQLException {
    storage = new MemoryStorage();
  }

  @Test
  public void addedAccountCanBeRetrieved() throws SQLException {
    Account a = new Account("root");
    a.setRole(Account.Role.SUPERUSER);
    a.setPassword("root");
    storage.saveAccount(a);
    Account b = storage.getAccountByUsername("root");
    assertEquals(Account.Role.SUPERUSER, b.role);
  }

  @Test(expected = SQLException.class)
  public void savingWithExistingUsernameThrows() throws SQLException {
    Account a = new Account("root");
    a.setPassword("root");

    Account b = new Account("root");
    b.setPassword("root");

    storage.saveAccount(a);
    storage.saveAccount(b);
  }
}
