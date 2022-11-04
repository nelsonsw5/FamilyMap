package Results;

public class RegisterResult extends Result {
    /**
     * when a user registers, he/she will be logged in and issued an authToken
     */
    private String authtoken;
    /**
     *
     */
    private String username;
    /**
     * the user will also be given an id when they register.
     */
    private String personID;

    /**
     *
     * @param message saying whether an error occurred
     * @param success boolean of whether request was successfully completed
     * @param authtoken when a user registers, he/she will be logged in and issued an authToken
     * @param username the user will also sign up with a username for future log-ins
     * @param personID the user will also be given an id when they register.
     */
    public RegisterResult(String message, boolean success, String authtoken, String username, String personID) {
        super(message, success);
        this.authtoken = authtoken;
        this.username = username;
        this.personID = personID;
    }

    public String getAuthtoken() { return authtoken; }

    public String getUsername() { return username; }

    public String getPersonID() { return personID; }

    public void setAuthtoken(String authtoken) {
        this.authtoken = authtoken;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
}
