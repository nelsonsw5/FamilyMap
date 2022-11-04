package Models;

public class PersonModel {
    /**
     * Unique identifier for this person (non-empty string)
     */
    private String personID;
    /**
     * Username: User (Username) to which this person belongs
     */
    private String associatedUsername;
    /**
     * Person’s first name (non-empty string)
     */
    private String firstName;
    /**
     * Person’s last name (non-empty string)
     */
    private String lastName;
    /**
     * Person’s gender (string: “f” or “m”)
     */
    private String gender;
    /**
     * Person ID of person’s spouse (possibly null)
     */
    private String spouseID;
    /**
     * Person ID of person’s father (possibly null)
     */
    private String fatherID;
    /**
     * Person ID of person’s mother (possibly null)
     */
    private String motherID;
    /**
     *
     * @param personID Unique identifier for this person (non-empty string)
     * @param associatedUsername User (Username) to which this person belongs
     * @param firstName Person’s first name (non-empty string)
     * @param lastName Person’s last name (non-empty string)
     * @param gender Person’s gender (string: “f” or “m”)
     * @param spouseID Person ID of person’s spouse (possibly null)
     * @param fatherID Person ID of person’s father (possibly null)
     * @param motherID Person ID of person’s mother (possibly null)
     */
    public PersonModel(String personID, String associatedUsername, String firstName,
                       String lastName, String gender, String spouseID,
                       String fatherID, String motherID) {
        this.personID = personID;
        this.associatedUsername = associatedUsername;
        this.firstName = firstName;
        this.lastName = lastName;
        this.gender = gender;
        this.spouseID = spouseID;
        this.fatherID = fatherID;
        this.motherID = motherID;
    }

    @Override
    public boolean equals(Object o) {
        if (o == null)
            return false;
        if (o instanceof PersonModel) {
            PersonModel oPerson = (PersonModel) o;
            return oPerson.getPersonID().equals(getPersonID()) &&
                    oPerson.getAssociatedUsername().equals(getAssociatedUsername()) &&
                    oPerson.getFirstName().equals(getFirstName()) &&
                    oPerson.getLastName().equals(getLastName()) &&
                    oPerson.getGender().equals(getGender()) &&
                    oPerson.getSpouseID().equals(getSpouseID()) &&
                    oPerson.getFatherID().equals(getFatherID()) &&
                    oPerson.getMotherID().equals(getMotherID());
        }
        else { return false; }
    }

    public String getPersonID() { return personID; }

    public String getAssociatedUsername() { return associatedUsername; }

    public String getFirstName() { return firstName; }

    public String getLastName() { return lastName; }

    public String getGender() { return gender; }

    public String getSpouseID() { return spouseID; }

    public String getFatherID() { return fatherID; }

    public String getMotherID() { return motherID; }

    public void setPersonID(String personID) { this.personID = personID; }

    public void setAssociatedUsername(String associatedUsername) { this.associatedUsername = associatedUsername; }

    public void setFirstName(String firstName) { this.firstName = firstName; }

    public void setLastName(String lastName) { this.lastName = lastName; }

    public void setGender(String gender) { this.gender = gender; }

    public void setSpouseID(String spouseID) { this.spouseID = spouseID; }

    public void setFatherID(String fatherID) { this.fatherID = fatherID; }

    public void setMotherID(String motherID) { this.motherID = motherID; }
}
