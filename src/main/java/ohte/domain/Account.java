package ohte.domain;

import org.mindrot.jbcrypt.BCrypt;

/**
 * This data class represents a single Account in the system.
 */
public class Account {
    /**
     * Username of the account. Used for identifying the account.
     */
    String username;

    /**
     * Role of the account. Determines which operations the account has access to.
     * Default value is {@link Role.NORMAL}.
     */
    Role role;

    /**
     * Password hash created using BCrypt.
     */
    String passwordHash;

    /**
     * Enumeration type that represents a user account role.
     */
    public enum Role {
        SUPERUSER(1),
        NORMAL(2);

        int value;

        Role(int value) {
            this.value = value;
        }

        /**
         * Converts a string to a {@link Role} value.
         */
        public static Role fromString(String value) {
            if (value.equals("NORMAL")) {
                return Role.NORMAL;
            } else if (value.equals("SUPERUSER")) {
                return Role.SUPERUSER;
            } else {
                throw new IllegalArgumentException();
            }
        }

        @Override
        public String toString() {
            if (value == Role.NORMAL.value) {
                return "NORMAL";
            } else if (value == Role.SUPERUSER.value) {
                return "SUPERUSER";
            } else {
                throw new RuntimeException("invalid internal representation for Role");
            }
        }

        public String convertTo() {
            return toString();
        }

        static public Role convertFrom(String value) {
            return fromString(value);
        }
    };

    public Account() {}

    /**
     * Creates a new account with the provided username.
     * Other properties have default values.
     */
    public Account(String username) {
        this.username = username;
        this.role = Role.NORMAL;
    }

    /**
     * Hashes the password and sets the hash to the new value.
     */
    public void setPassword(String cleartext) {
        String salt = BCrypt.gensalt(10);
        setPasswordHash(BCrypt.hashpw(cleartext, salt));
    }

    public void setPasswordHash(String hash) {
        passwordHash = hash;
    }

    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Compares the given cleartext passowrd against the stored passowrd hash.
     *
     * @returns True if the password matches.
     */
    public boolean checkPassword(String cleartext) {
        return BCrypt.checkpw(cleartext, passwordHash);
    }

    /**
     * Sets the account's role to the provided value.
     */
    public void setRole(Role role) {
        this.role = role;
    }

    /**
     * Returns the account's role.
     */
    public Role getRole() {
        return role;
    }

    /**
     * Returns the account's username.
     */
    public String getUsername() {
        return username;
    }

    public boolean equals(Account other) {
        return username.equals(other.getUsername());
    }
}
