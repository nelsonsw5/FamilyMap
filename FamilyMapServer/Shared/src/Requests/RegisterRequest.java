package Requests;

public class RegisterRequest {
    /**
     * Unique username (non-empty string)
     */
    private String username;
    /**
     * User’s password (non-empty string)
     */
    private String password;
    /**
     * User’s email address (non-empty string)
     */
    private String email;
    /**
     * User’s first name (non-empty string)
     */
    private String firstName;
    /**
     *  User’s last name (non-empty string)
     */
    private String lastName;
    /**
     * User’s gender (string: “f” or “m”)
     */
    private String gender;

    /**
     *
     * @param username Unique username (non-empty string)
     * @param password User’s password (non-empty string)
     * @param email User’s email address (non-empty string)
     * @param firstName User’s first name (non-empty string)
     * @param lastName User’s last name (non-empty string)
     * @param gender User’s gender (string: “f” or “m”)
     */
    public RegisterRequest(String username, String password,
                           String email, String firstName,
                           String lastName, String gender) {
        this.username = username;
        this.password = password;
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
    }

    public String getUsername() { return username; }

    public String getPassword() { return password; }

    public String getEmail() { return email; }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public String getGender() { return gender; }
}
