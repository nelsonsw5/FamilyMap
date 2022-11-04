package DataAccess;

import Models.EventModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class EventDaoTest {
    private DataBase db;
    private EventModel bestEvent;
    private EventModel betterEvent;
    private EventDao eDao;

    @BeforeEach
    void setUp() throws SQLException {
        db = new DataBase();
        bestEvent = new EventModel("coolBeans", "jackieChan","1234aasdf",12341,1234,"USA","Provo","kickingButt",2021);
        betterEvent = new EventModel("niceOne", "jackieChan","1234aasdf",1234,123,"USA","Orem","kickingTrash",2018);
        db.openConnection();
        Connection conn = db.getConnection();
        db.deleteAllData();
        eDao = new EventDao(conn);
    }

    @AfterEach
    void tearDown() throws SQLException {
        db.closeConnection(false);
    }

    @Test
    void insert() throws DataAccessException {
        eDao.insert(bestEvent);
        EventModel compareEvent = eDao.find(bestEvent.getEventID());
        assertNotNull(compareEvent);
        assertEquals(bestEvent,compareEvent);
    }

    @Test
    void find() throws DataAccessException {
        eDao.insert(bestEvent);
        EventModel compareEvent = eDao.find(bestEvent.getEventID());
        assertNotNull(compareEvent);
        assertEquals(bestEvent,compareEvent);
    }

    @Test
    void findEvents() throws DataAccessException {
        eDao.insert(bestEvent);
        eDao.insert(betterEvent);
        EventModel[] arrToTest = new EventModel[2];
        arrToTest[0] = bestEvent;
        arrToTest[1] = betterEvent;
        EventModel[] compareTest = eDao.findEvents(bestEvent.getAssociatedUsername());
        assertEquals(arrToTest[1],compareTest[1]);
    }

    @Test
    void delete() throws DataAccessException, SQLException {
        eDao.insert(bestEvent);
        EventModel compareTest = eDao.find(bestEvent.getEventID());
        assertEquals(bestEvent, compareTest);
        eDao.delete(bestEvent);
        assertNull(eDao.find(bestEvent.getPersonID()));
    }

    @Test
    void clear() throws DataAccessException, SQLException {
        eDao.insert(bestEvent);
        EventModel compareTest = eDao.find(bestEvent.getEventID());
        assertEquals(bestEvent, compareTest);
        eDao.clear();
        assertNull(eDao.find(bestEvent.getPersonID()));
    }
}