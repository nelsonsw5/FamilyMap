package Results;

public class LoginResult extends Result {
    /**
     * the request, if successful, will return an authtoken for that user's login
     */
    private String authtoken;
    /**
     * username of the user who logged in
     */
    private String username;
    /**
     * id of the person who just logged in
     */
    private String personID;

    /**
     *
     * @param message saying whether an error occurred
     * @param success boolean of whether request was successfully completed
     * @param authtoken the request, if successful, will return an authtoken for that user's login
     * @param username username of the user who logged in
     * @param personID id of the person who just logged in
     */
    public LoginResult(String message, boolean success, String authtoken, String username, String personID) {
        super(message, success);
        this.authtoken = authtoken;
        this.username = username;
        this.personID = personID;
    }

    public String getAuthtoken() { return authtoken; }

    public String getUsername() { return username; }

    public String getPersonID() { return personID; }
}
