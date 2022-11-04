package Services;

import DataAccess.*;
import Models.AuthTokenModel;
import Models.PersonModel;
import Models.UserModel;
import Results.AllPersonsResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class AllPersonsServiceTest {

    private DataBase db;
    private UserModel bestUser;
    String authTokenString;
    PersonModel person1;
    PersonModel person2;
    AuthTokenDao aDao;
    AuthTokenModel authToken;
    PersonDao pDao;

    @BeforeEach
    void setUp() throws SQLException, DataAccessException {
        db = new DataBase();
        Connection connection = db.getConnection();
        db.deleteAllData();
        aDao = new AuthTokenDao(connection);
        pDao = new PersonDao(connection);
        aDao.clear();
        pDao.clear();
        authTokenString = "myAuthyToken";
        authToken = new AuthTokenModel(authTokenString,"jchan132");
        person1 = new PersonModel("Biking_123A", "jchan132", "Gale",
                "Frost", "f", "Japan234", "Ushiku567",
                "Biking_Around123");
        person2 = new PersonModel("JackieChan", "jchan132", "Jackie",
                "Chan", "m", "Japan234", "Ushiku567",
                "chrisyChan842");
        pDao.insert(person1);
        pDao.insert(person2);
        aDao.insert(authToken);
        db.closeConnection(true);
    }

    @Test
    void getAllPersonsPass() throws DataAccessException, SQLException {

        PersonModel[] data = new PersonModel[2];
        data[0] = person1;
        data[1] = person2;

        AllPersonsService allPersonsService = new AllPersonsService();
        AllPersonsResult actualAllPersonsResult = allPersonsService.getAllPersons(authTokenString);

        assertNotNull(actualAllPersonsResult.getData()[1]);
        assertEquals(data[1],actualAllPersonsResult.getData()[1]);
    }

    @Test
    void getAllPersonsFail() throws SQLException, DataAccessException {
        db = new DataBase();
        Connection connection = db.getConnection();
        AuthTokenDao authTokenDao = new AuthTokenDao(connection);
        authTokenDao.clear();
        db.closeConnection(true);

        PersonModel[] data = new PersonModel[2];
        data[0] = person1;
        data[1] = person2;

        AllPersonsService allPersonsService = new AllPersonsService();
        AllPersonsResult actualAllPersonsResult = allPersonsService.getAllPersons(authTokenString);

        assertNull(actualAllPersonsResult.getData());

    }
}