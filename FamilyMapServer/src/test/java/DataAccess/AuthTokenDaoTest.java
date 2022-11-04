package DataAccess;

import Models.AuthTokenModel;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import javax.xml.crypto.Data;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class AuthTokenDaoTest {
    private DataBase db;
    private AuthTokenModel bestAuthToken;
    private AuthTokenDao aDao;

    @BeforeEach
    void setUp() throws SQLException {
        db = new DataBase();
        bestAuthToken = new AuthTokenModel("f2n348","higginsjg");
        db.openConnection();
        Connection conn = db.getConnection();
        db.deleteAllData();
        aDao = new AuthTokenDao(conn);
    }

    @AfterEach
    void tearDown() throws SQLException {
        db.closeConnection(false);
    }

    @Test
    void insert() throws DataAccessException {
        aDao.insert(bestAuthToken);
        AuthTokenModel compare = aDao.find(bestAuthToken.getAuthToken());
        assertNotNull(compare);
        assertEquals(bestAuthToken,compare);
    }

    @Test
    void find() throws DataAccessException {
        aDao.insert(bestAuthToken);
        AuthTokenModel compare = aDao.find(bestAuthToken.getAuthToken());
        assertNotNull(compare);
        assertEquals(bestAuthToken,compare);
    }

    @Test
    void delete() throws SQLException, DataAccessException {
        aDao.insert(bestAuthToken);
        AuthTokenModel compare = aDao.find(bestAuthToken.getAuthToken());
        assertEquals(bestAuthToken,compare);
        aDao.delete(bestAuthToken);
        assertNull(aDao.find(bestAuthToken.getAuthToken()));
    }

    @Test
    void clear() throws DataAccessException, SQLException {
        aDao.insert(bestAuthToken);
        AuthTokenModel compare = aDao.find(bestAuthToken.getAuthToken());
        assertEquals(bestAuthToken,compare);
        aDao.clear();
        assertNull(aDao.find(bestAuthToken.getAuthToken()));
    }
}