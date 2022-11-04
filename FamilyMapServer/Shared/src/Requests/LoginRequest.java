package Requests;

public class LoginRequest {
    /**
     * unique, non-empty username
     */
    private String username;
    /**
     * password that's associated with username
     */
    private String password;

    /**
     *
     * @param username unique, non-empty username
     * @param password password that's associated with username
     */

    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    public String getUsername() { return username; }

    public String getPassword() { return password; }
}
