package ohte.storage;

import ohte.storage.Account;
import ohte.storage.Account.Role;
import org.junit.*;
import static org.junit.Assert.*;

public class AccountTest {
  @Test
  public void passwordHashMatches() {
    Account a = new Account("root");
    a.setPassword("root");
    assertTrue(a.checkPassword("root"));
    assertFalse(a.checkPassword("toor"));
  }

  @Test 
  public void roleConversionToStringConservesEquality() {
    Role[] roles = new Role[] { Role.SUPERUSER, Role.NORMAL };

    for (int i = 0; i < roles.length; i++) {
      String s = roles[i].toString();
      Role r = Role.fromString(s);
      assertEquals(roles[i], r);

      for (int j = 0; j < roles.length; j++) {
        if (j == i) continue;

        assertNotEquals(roles[j], r);
      }
    }
  }

  @Test(expected = IllegalArgumentException.class)
  public void roleConversionFromInvalidStringThrows() throws IllegalArgumentException {
    Role.fromString("INVALID_ROLE");
  }
}
