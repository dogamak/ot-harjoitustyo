package ohte.domain;

import org.junit.*;
import static org.junit.Assert.*;

import ohte.domain.Account.Role;

public class AccountTest {
  @Test
  public void roleIsConvertedToStringCorrectly() {
    assertEquals("SUPERUSER", Role.SUPERUSER.toString());
    assertEquals("NORMAL", Role.NORMAL.toString());
  }

  @Test
  public void roleIsParsedFromStringCorrectly() {
    assertEquals(Role.SUPERUSER, Role.fromString("SUPERUSER"));
    assertEquals(Role.NORMAL, Role.fromString("NORMAL"));
  }

  @Test
  public void parsingInvalidRoleRepresentationThrows() {
    assertThrows(IllegalArgumentException.class, () -> {
      Role.fromString("ILLEGAL");
    });
  }

  @Test
  public void accountEqualsBasedOnUsername() {
      Account a1 = new Account("username");
      a1.setRole(Role.SUPERUSER);
      a1.setPassword("password1");

      Account a2 = new Account("username");
      a1.setRole(Role.NORMAL);
      a1.setPassword("password2");

      assertTrue(a1.equals(a2));
  }
}
