package Results;

public class SinglePersonResult extends Result {
    /**
     * the username of the user in whose family tree the person is in.
     */
    private String associatedUsername;
    /**
     * the id of the person who is being requested
     */
    private String personID;
    /**
     * first name of the person being requested
     */
    private String firstName;
    /**
     * last name of the person being requested
     */
    private String lastName;
    /**
     * gender of the person being requested
     */
    private String gender;
    /**
     * id of the father of person being requested (can be null).
     */
    private String fatherID;
    /**
     * id of the mother of the person being requested (can be null).
     */
    private String motherID;
    /**
     * id of the spouse of the person being requested (can be null).
     */
    private String spouseID;

    /**
     *
     * @param message saying whether an error occurred, and what it is
     * @param success boolean of whether request was successfully completed
     * @param associatedUsername the username of the user in whose family tree the person is in.
     * @param personID the id of the person who is being requested
     * @param firstName first name of the person being requested
     * @param lastName last name of the person being requested
     * @param gender gender of the person being requested
     * @param fatherID id of the father of person being requested (can be null).
     * @param motherID id of the mother of the person being requested (can be null).
     * @param spouseID id of the spouse of the person being requested (can be null).
     */

    public SinglePersonResult(String message, boolean success, String associatedUsername,
                              String personID, String firstName, String lastName, String gender,
                              String fatherID, String motherID, String spouseID) {
        super(message, success);
        this.associatedUsername = associatedUsername;
        this.personID = personID;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.fatherID = fatherID;
        this.motherID = motherID;
        this.spouseID = spouseID;
    }

    public String getAssociatedUsername() { return associatedUsername; }

    public String getPersonID() { return personID; }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public String getGender() { return gender; }

    public String getFatherID() { return fatherID; }

    public String getMotherID() { return motherID; }

    public String getSpouseID() { return spouseID; }
}
