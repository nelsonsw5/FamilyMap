package Services;

import DataAccess.*;
import Models.AuthTokenModel;
import Models.EventModel;
import Models.PersonModel;
import Results.SingleEventResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class SingleEventServiceTest {
    String authTokenString = "cool_beans";
    String eventID = "awesomeMan";
    EventModel eventToAdd;
    PersonModel personToAdd;
    AuthTokenModel authTokenToAdd;


    @BeforeEach
    void setUp() throws SQLException, DataAccessException {
        DataBase db = new DataBase();
        Connection connection = db.getConnection();

        eventToAdd = new EventModel("awesomeMan", "jchan132","1234aasdf",12341,1234,"USA","Provo","kickingButt",2021);
        personToAdd = new PersonModel("Biking_123A", "jchan132", "Gale", "Frost", "f", "Japan234", "Ushiku567", "Biking_Around123");
        authTokenToAdd = new AuthTokenModel(authTokenString,"jchan132");

        EventDao eDao = new EventDao(connection);
        PersonDao pDao = new PersonDao(connection);
        AuthTokenDao aDao = new AuthTokenDao(connection);

        eDao.clear();
        pDao.clear();
        aDao.clear();

        eDao.insert(eventToAdd);
        pDao.insert(personToAdd);
        aDao.insert(authTokenToAdd);

        db.closeConnection(true);
    }


    @Test
    void getSingleEventPass() throws SQLException, DataAccessException {
        SingleEventService singleEventService = new SingleEventService();
        SingleEventResult singleEventResult = singleEventService.getSingleEvent(eventID,authTokenString);

        assertTrue(singleEventResult.isSuccess());
        assertEquals(eventToAdd.getCountry(),singleEventResult.getCountry());

    }

    @Test
    void getSingleEventFail() throws SQLException, DataAccessException {
        DataBase db = new DataBase();
        Connection connection = db.getConnection();
        EventDao eDao = new EventDao(connection);
        eDao.clear();
        db.closeConnection(true);

        SingleEventService singleEventService = new SingleEventService();
        SingleEventResult singleEventResult = singleEventService.getSingleEvent(eventID,authTokenString);

        assertFalse(singleEventResult.isSuccess());
        assertNotEquals(eventToAdd.getCountry(),singleEventResult.getCountry());

    }
}