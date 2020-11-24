package ohte.domain;

/**
 * Plain-Old-Data class for storing user credentials.
 */
public class Credentials {
    private String username;
    private String password;

    public Credentials(String username, String password) {
        this.username = username;
        this.password = password;
    }

    String getUsername() {
        return username;
    }

    String getPassword() {
        return password;
    }
}
