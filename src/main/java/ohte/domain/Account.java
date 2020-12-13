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
         *
         * @param value String representation of a {@link Role}
         *
         * @return Role represented by the string value
         *
         * @throws IllegalArgumentException if the string representation is invalid
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
    };

    /**
     * Creates a new account with the provided username.
     * Other properties have default values.
     *
     * @param username Username for the created account.
     */
    public Account(String username) {
        this.username = username;
        this.role = Role.NORMAL;
    }

    /**
     * Hashes the password and sets the hash to the new value.
     *
     * @param cleartext Clear-text password for hasing.
     */
    public void setPassword(String cleartext) {
        String salt = BCrypt.gensalt(10);
        setPasswordHash(BCrypt.hashpw(cleartext, salt));
    }

    /**
     * Sets the password hash to already hashed value.
     *
     * @param hash Pre-hashed password
     */
    public void setPasswordHash(String hash) {
        passwordHash = hash;
    }

    /**
     * Get the password hash. 
     *
     * @return BCrypt-hashed password
     */
    public String getPasswordHash() {
        return passwordHash;
    }

    /**
     * Compares the given clear-text passowrd against the stored passowrd hash.
     *
     * @param cleartext The clear-text password to check agains
     *
     * @return True if the password matches.
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
     *
     * @return Account's {@link Role}
     */
    public Role getRole() {
        return role;
    }

    /**
     * Returns the account's username.
     *
     * @return Account's username
     */
    public String getUsername() {
        return username;
    }

    public boolean equals(Account other) {
        return username.equals(other.getUsername());
    }
}
