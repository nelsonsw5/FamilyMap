package Services;

import DataAccess.DataAccessException;
import DataAccess.DataBase;
import DataAccess.UserDao;
import Requests.RegisterRequest;
import Results.RegisterResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class RegisterServiceTest {

    // delete anything in db so that we can test the once and twice registers
    @BeforeEach
    void setUp() throws SQLException {
        DataBase db = new DataBase();
        Connection connection = db.getConnection();
        UserDao uDao = new UserDao(connection);
        uDao.clear();
        db.closeConnection(true);
    }

    @Test
    void registerPass() throws SQLException, DataAccessException {
        RegisterService registerService = new RegisterService();
        RegisterRequest registerRequest = new RegisterRequest("bob","brown",
                "bob@brown.com","BOB","BROWN","m");
        RegisterResult registerResult = registerService.register(registerRequest);
        assertTrue(registerResult.isSuccess());
    }
    //trying to add a user twice
    @Test
    void registerFail() throws SQLException, DataAccessException {
        RegisterService registerService = new RegisterService();
        RegisterRequest registerRequest = new RegisterRequest("bob","brown",
                "bob@brown.com","BOB","BROWN","m");
        RegisterResult registerResult = registerService.register(registerRequest);
        assertTrue(registerResult.isSuccess());
        registerRequest = new RegisterRequest("bob","brown",
                "bob@brown.com","BOB","BROWN","m");
        registerResult = registerService.register(registerRequest);
        assertFalse(registerResult.isSuccess());
    }
}