package Services;

import DataAccess.*;
import Models.EventModel;
import Models.PersonModel;
import Models.UserModel;
import Results.ClearResult;
import jdk.jfr.Event;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import passoffmodels.Person;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class ClearServiceTest {
    DataBase db;

    Connection connection;

    UserModel bestUser;
    PersonModel bestPerson;
    PersonModel betterPerson;
    EventModel bestEvent;
    EventModel betterEvent;

    UserDao uDao;
    PersonDao pDao;
    EventDao eDao;

    @BeforeEach
    void setUp() throws SQLException, DataAccessException {
        db = new DataBase();
        connection = db.getConnection();

        bestUser = new UserModel("higginsjoshuag", "cool_beans12", "higjg@gmail.com",
                "Joshua", "Higgins", "m", "Ushiku567");
        bestPerson = new PersonModel("Biking_123A", "jchan132", "Gale",
                "Frost", "f", "Japan234", "Ushiku567",
                "Biking_Around123");
        betterPerson = new PersonModel("JackieChan", "jchan132", "Jackie",
                "Chan", "m", "Japan234", "Ushiku567",
                "chrisyChan842");
        bestEvent = new EventModel("coolBeans", "jackieChan","1234aasdf",
                12341,1234,"USA","Provo","kickingButt",2021);
        betterEvent = new EventModel("niceOne", "jackieChan","1234aasdf",
                1234,123,"USA","Orem","kickingTrash",2018);

        uDao = new UserDao(connection);
        pDao = new PersonDao(connection);
        eDao = new EventDao(connection);

        uDao.clear();
        pDao.clear();
        eDao.clear();

        uDao.insert(bestUser);
        pDao.insert(bestPerson);
        pDao.insert(betterPerson);
        eDao.insert(bestEvent);
        eDao.insert(betterEvent);

        db.closeConnection(true);
    }

    @Test
    void clearPass() throws SQLException, DataAccessException {
        db = new DataBase();
        connection = db.getConnection();
        uDao = new UserDao(connection);

        assertNotNull(uDao.find(bestUser.getUsername()));
        db.closeConnection(true);

        ClearService clearService = new ClearService();
        ClearResult clearResult = clearService.clear();

        assertTrue(clearResult.isSuccess());

        connection = db.getConnection();
        uDao = new UserDao(connection);
        assertNull(uDao.find(bestUser.getUsername()));
        db.closeConnection(true);
    }
}