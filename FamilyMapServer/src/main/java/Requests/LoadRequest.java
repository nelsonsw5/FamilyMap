package Requests;

import Models.*;

public class LoadRequest {
    /**
     * array of user objects to load into db
     */
    UserModel[] users;
    /**
     * array of person objects to load into db
     */
    PersonModel[] persons;
    /**
     * array of event objects to load into db
     */
    EventModel[] events;

    /**
     *
     * @param users array of user objects to load into db
     * @param persons array of person objects to load into db
     * @param events array of event objects to load into db
     */
    public LoadRequest(UserModel[] users, PersonModel[] persons, EventModel[] events) {
        this.users = users;
        this.persons = persons;
        this.events = events;
    }

    public UserModel[] getUsers() { return users; }

    public PersonModel[] getPersons() { return persons; }

    public EventModel[] getEvents() { return events; }
}
