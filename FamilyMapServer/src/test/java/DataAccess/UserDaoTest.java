package DataAccess;

import Models.UserModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import passoffmodels.User;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class UserDaoTest {
    private DataBase db;
    private UserModel bestUser;
    private UserDao uDao;

    @BeforeEach
    public void setUp() throws SQLException {
        db = new DataBase();
        bestUser = new UserModel("higginsjoshuag", "cool_beans12", "higjg@gmail.com",
                "Joshua", "Higgins", "m", "Ushiku567");
        db.openConnection();
        Connection conn = db.getConnection();
        db.deleteAllData();
        uDao = new UserDao(conn);
    }

    @AfterEach
    public void tearDown() throws SQLException {
        db.closeConnection(false);
    }

    @Test
    void insertPass() throws DataAccessException {
        uDao.insert(bestUser);
        UserModel compareTest = uDao.find(bestUser.getUsername());
        assertNotNull(compareTest);
        assertEquals(bestUser,compareTest);
    }

    @Test
    void insertFail() throws DataAccessException {
        uDao.insert(bestUser);
        assertThrows(DataAccessException.class, ()-> uDao.insert(bestUser));
    }

    @Test
    void findPass() throws DataAccessException {
        uDao.insert(bestUser);
        UserModel compareTest = uDao.find(bestUser.getUsername());
        assertNotNull(compareTest);
        assertEquals(bestUser, compareTest);
    }

    @Test
    void findFail() throws DataAccessException {
        uDao.insert(bestUser);
        assertNull(uDao.find("this_is_a_test"));
    }

    @Test
    void delete() throws SQLException, DataAccessException {
        uDao.insert(bestUser);
        UserModel compareTest = uDao.find(bestUser.getUsername());
        assertEquals(bestUser, compareTest);
        uDao.delete(bestUser);
        assertNull(uDao.find(bestUser.getUsername()));
    }

    @Test
    void clear() throws DataAccessException, SQLException {
        uDao.insert(bestUser);
        UserModel compareTest = uDao.find(bestUser.getUsername());
        assertEquals(bestUser, compareTest);
        uDao.clear();
        assertNull(uDao.find(bestUser.getUsername()));
    }


}