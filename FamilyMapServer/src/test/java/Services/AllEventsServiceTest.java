package Services;

import DataAccess.AuthTokenDao;
import DataAccess.DataAccessException;
import DataAccess.DataBase;
import DataAccess.EventDao;
import Models.AuthTokenModel;
import Models.EventModel;
import Models.UserModel;
import Results.AllEventsResult;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class AllEventsServiceTest {

    private DataBase db;
    private UserModel bestUser;
    String authTokenString;
    EventModel event1;
    EventModel event2;
    AuthTokenDao aDao;
    AuthTokenModel authToken;
    EventDao eDao;

    @BeforeEach
    void setUp() throws SQLException, DataAccessException {
        db = new DataBase();
        Connection connection = db.getConnection();
        db.deleteAllData();
        aDao = new AuthTokenDao(connection);
        eDao = new EventDao(connection);
        aDao.clear();
        eDao.clear();
        authTokenString = "myAuthyToken";
        authToken = new AuthTokenModel(authTokenString,"jackieChan");
        event1 = new EventModel("coolBeans", "jackieChan","1234aasdf",12341,1234,"USA","Provo","kickingButt",2021);
        event2 = new EventModel("niceOne", "jackieChan","1234aasdf",1234,123,"USA","Orem","kickingTrash",2018);
        eDao.insert(event1);
        eDao.insert(event2);
        aDao.insert(authToken);
        db.closeConnection(true);
    }

    @Test
    void getAllEventsPass() throws DataAccessException, SQLException {

        EventModel[] data = new EventModel[2];
        data[0] = event1;
        data[1] = event2;

        AllEventsService allEventsService = new AllEventsService();
        AllEventsResult actualAllEventsResult = allEventsService.getAllEvents(authTokenString);

        assertNotNull(actualAllEventsResult.getData()[1]);
        assertEquals(data[1],actualAllEventsResult.getData()[1]);
    }

    @Test
    void getAllEventsFail() throws SQLException, DataAccessException {
        db = new DataBase();
        Connection connection = db.getConnection();
        AuthTokenDao authTokenDao = new AuthTokenDao(connection);
        authTokenDao.clear();
        db.closeConnection(true);

        EventModel[] data = new EventModel[2];
        data[0] = event1;
        data[1] = event2;

        AllEventsService allEventsService = new AllEventsService();
        AllEventsResult actualAllEventsResult = allEventsService.getAllEvents(authTokenString);

        assertNull(actualAllEventsResult.getData());

    }
}