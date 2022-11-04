package Results;

public class SingleEventResult extends Result {
    /**
     * string of username of the user in whose family tree the event is in.
     */
    private String associatedUsername;
    /**
     * the id of the requested event
     */
    private String eventID;
    /**
     * latitude of the event
     */
    private float latitude;
    /**
     * longitude of the event (float)
     */
    private float longitude;
    /**
     * country where the event took place.
     */
    private String country;
    /**
     * city where the event took place.
     */
    private String city;
    /**
     * what the event actually was (birth, christening, etc.)
     */
    private String eventType;
    /**
     * year when the event took place.
     */
    private int year;


    private String personID;

    /**
     *
     * @param message saying whether an error occurred, and what it is
     * @param success boolean of whether request was successfully completed
     * @param associatedUsername string of username of the user in whose family tree the event is in.
     * @param eventID the id of the requested event
     * @param latitude latitude of the event
     * @param longitude longitude of the event (float)
     * @param country country where the event took place.
     * @param city  city where the event took place.
     * @param eventType what the event actually was (birth, christening, etc.)
     * @param year year when the event took place.
     * @param personID id of the person who requested the event
     */

    public SingleEventResult(String message, boolean success, String associatedUsername, String eventID, float latitude, float longitude, String country, String city, String eventType, int year, String personID) {
        super(message, success);
        this.associatedUsername = associatedUsername;
        this.eventID = eventID;
        this.latitude = latitude;
        this.longitude = longitude;
        this.country = country;
        this.city = city;
        this.eventType = eventType;
        this.year = year;
        this.personID = personID;
    }



    public String getPersonID() { return personID; }

    public String getAssociatedUsername() { return associatedUsername; }

    public String getEventID() { return eventID; }

    public float getLatitude() { return latitude; }

    public float getLongitude() { return longitude; }

    public String getCountry() { return country; }

    public String getCity() { return city; }

    public String getEventType() { return eventType; }

    public int getYear() { return year; }


}
