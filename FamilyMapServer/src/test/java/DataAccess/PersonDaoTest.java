package DataAccess;

import Models.PersonModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import passoffmodels.Person;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class PersonDaoTest {
    private DataBase db;
    private PersonModel bestPerson;
    private PersonModel betterPerson;
    private PersonDao pDao;


    @BeforeEach
    public void setUp() throws SQLException {
        db = new DataBase();
        bestPerson = new PersonModel("Biking_123A", "jchan132", "Gale",
                "Frost", "f", "Japan234", "Ushiku567",
                "Biking_Around123");
        betterPerson = new PersonModel("JackieChan", "jchan132", "Jackie",
                "Chan", "m", "Japan234", "Ushiku567",
                "chrisyChan842");
        db.openConnection();
        Connection conn = db.getConnection();
        pDao = new PersonDao(conn);
        pDao.clear();
    }

    @AfterEach
    public void tearDown() throws SQLException {
        db.closeConnection(false);
    }

    @Test
    void insertPass() throws DataAccessException {
        pDao.insert(bestPerson);
        PersonModel compareTest = pDao.find(bestPerson.getPersonID());
        assertNotNull(compareTest);
        assertEquals(bestPerson,compareTest);
    }

    @Test
    void insertFail() throws DataAccessException {
        pDao.insert(bestPerson);
        assertThrows(DataAccessException.class, ()-> pDao.insert(bestPerson));
    }

    @Test
    void findPass() throws DataAccessException {
        pDao.insert(bestPerson);
        PersonModel compareTest = pDao.find(bestPerson.getPersonID());
        assertNotNull(compareTest);
        assertEquals(bestPerson, compareTest);
    }

    @Test
    void deletePass() throws SQLException, DataAccessException {
        pDao.insert(bestPerson);
        PersonModel compareTest = pDao.find(bestPerson.getPersonID());
        assertEquals(bestPerson, compareTest);
        pDao.delete(bestPerson);
        assertNull(pDao.find(bestPerson.getPersonID()));
    }

    @Test
    void findFail() throws DataAccessException {
        pDao.insert(bestPerson);
        assertNull(pDao.find("this_is_a_test"));
    }

    @Test
    void clear() throws DataAccessException, SQLException {
        pDao.insert(bestPerson);
        PersonModel compareTest = pDao.find(bestPerson.getPersonID());
        assertEquals(bestPerson, compareTest);
        pDao.clear();
        assertNull(pDao.find(bestPerson.getPersonID()));
    }

    @Test
    void findPersons() throws DataAccessException {
        pDao.insert(bestPerson);
        pDao.insert(betterPerson);
        PersonModel[] arrToTest = new PersonModel[2];
        arrToTest[0] = bestPerson;
        arrToTest[1] = betterPerson;
        PersonModel[] compareTest = pDao.findPersons(bestPerson.getAssociatedUsername());
        assertEquals(arrToTest[1],compareTest[1]);
    }
}