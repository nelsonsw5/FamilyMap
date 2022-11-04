package Services;

import DataAccess.*;
import Models.AuthTokenModel;
import Models.PersonModel;
import Models.UserModel;
import Requests.FillRequest;
import Results.AllPersonsResult;
import Results.FillResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class FillServiceTest {
    DataBase db;
    Connection connection;

    UserDao uDao;
    PersonDao pDao;
    EventDao eDao;
    AuthTokenDao aDao;

    PersonModel bestPerson;
    UserModel bestUser;
    AuthTokenModel bestAuthToken;

    String authTokenString = "asdfasdf";

    @BeforeEach
    void setUp() throws SQLException, DataAccessException {
        db = new DataBase();
        connection = db.getConnection();

        uDao = new UserDao(connection);
        pDao = new PersonDao(connection);
        eDao = new EventDao(connection);
        aDao = new AuthTokenDao(connection);

        uDao.clear();
        pDao.clear();
        eDao.clear();
        aDao.clear();

        bestPerson = new PersonModel("Biking_123A", "higginsjoshuag", "Gale",
                "Frost", "f", "Japan234", "Ushiku567",
                "Biking_Around123");
        bestUser = new UserModel("higginsjoshuag", "cool_beans12", "higjg@gmail.com",
                "Joshua", "Higgins", "m", "Ushiku567");

        bestAuthToken = new AuthTokenModel(authTokenString,"higginsjoshuag");

        pDao.insert(bestPerson);
        uDao.insert(bestUser);
        aDao.insert(bestAuthToken);

        db.closeConnection(true);
    }

    @Test
    void fillPass() throws SQLException, FileNotFoundException, DataAccessException {

        AllPersonsService allPersonsService = new AllPersonsService();
        AllPersonsResult allPersonsResult = allPersonsService.getAllPersons(bestAuthToken.getAuthToken());

        FillService fillService = new FillService();
        FillRequest fillRequest = new FillRequest(bestUser.getUsername(), 4,null);
        FillResult fillResult = fillService.fill(fillRequest);

        allPersonsService = new AllPersonsService();
        allPersonsResult = allPersonsService.getAllPersons(bestAuthToken.getAuthToken());

        //32 bc there were already people in the db from the setup and those added by the fill.
        assertEquals(32,allPersonsResult.getData().length);
    }

    @Test
    void fillFail() throws SQLException, DataAccessException {

        connection = db.getConnection();

        uDao = new UserDao(connection);
        pDao = new PersonDao(connection);
        eDao = new EventDao(connection);
        aDao = new AuthTokenDao(connection);

        uDao.clear();
        pDao.clear();
        eDao.clear();
        aDao.clear();

        FillService fillService = new FillService();
        FillRequest fillRequest = new FillRequest("cool", 4,null);

        AllPersonsService allPersonsService = new AllPersonsService();
        AllPersonsResult allPersonsResult = allPersonsService.getAllPersons(bestAuthToken.getAuthToken());

        //shouldn't have added anything, so the one person above should be there.
        assertEquals(1,allPersonsResult.getData().length);

        db.closeConnection(true);
    }
}