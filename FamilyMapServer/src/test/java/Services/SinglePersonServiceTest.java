package Services;

import DataAccess.*;
import Models.AuthTokenModel;
import Models.EventModel;
import Models.PersonModel;
import Results.SinglePersonResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import passoffmodels.Person;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class SinglePersonServiceTest {

    String authTokenString = "cool_beans";
    String personID = "Biking_123A";
    EventModel eventToAdd;
    PersonModel personToAdd;
    AuthTokenModel authTokenToAdd;

    @BeforeEach
    void setUp() throws SQLException, DataAccessException {
        DataBase db = new DataBase();
        Connection connection = db.getConnection();

        eventToAdd = new EventModel("awesomeMan", "jchan132","1234aasdf",12341,1234,"USA","Provo","kickingButt",2021);
        personToAdd = new PersonModel(personID, "jchan132", "Gale", "Frost", "f", "Japan234", "Ushiku567", "Biking_Around123");
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
    void getSinglePersonPass() throws SQLException, DataAccessException {
        SinglePersonService singlePersonService = new SinglePersonService();
        SinglePersonResult singlePersonResult = singlePersonService.getSinglePerson(personID,authTokenString);

        assertTrue(singlePersonResult.isSuccess());
        assertEquals(personToAdd.getLastName(),singlePersonResult.getLastName());
    }

    @Test
    void getSinglePersonFail() throws SQLException, DataAccessException {
        DataBase db = new DataBase();
        Connection connection = db.getConnection();
        PersonDao pDao = new PersonDao(connection);
        pDao.clear();
        db.closeConnection(true);

        SinglePersonService singlePersonService = new SinglePersonService();
        SinglePersonResult singlePersonResult = singlePersonService.getSinglePerson(personID,authTokenString);

        assertFalse(singlePersonResult.isSuccess());
        assertNotEquals(personToAdd.getLastName(),singlePersonResult.getLastName());
    }
}