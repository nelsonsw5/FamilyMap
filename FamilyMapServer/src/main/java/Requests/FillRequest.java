package Requests;

public class FillRequest {
    /**
     * unique username of a user
     */
    private String username;
    /**
     * number of generations to fill
     */
    private int generations;

    private String personID;

    public FillRequest(String username, int generations, String personID) {
        this.username = username;
        this.generations = generations;
        this.personID = personID;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public int getGenerations() {
        return generations;
    }

    public void setGenerations(int generations) {
        this.generations = generations;
    }

    public String getPersonID() {
        return personID;
    }

    public void setPersonID(String personID) {
        this.personID = personID;
    }
}
