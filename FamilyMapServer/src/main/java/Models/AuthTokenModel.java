package Models;

public class AuthTokenModel {
    /**
     * authToken is created whenever someone logs in to the service
     */
    private String authToken;

    /**
     * username is drawn whenever someone logs in to the service
     */
    private String username;

    /**
     * Constructor for the AuthToken class
     * @param authToken created whenever someone logs in to the service
     * @param username drawn whenever someone logs in to the service
     */
    public AuthTokenModel(String authToken, String username) {
        this.authToken = authToken;
        this.username = username;
    }

    public String getAuthToken() { return authToken; }
    public String getUsername() { return username; }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof AuthTokenModel) {
            AuthTokenModel oAuthToken = (AuthTokenModel) o;
            return oAuthToken.getAuthToken().equals(getAuthToken()) &&
                    oAuthToken.getUsername().equals(getUsername());
        }
        else { return false; }
    }
}
