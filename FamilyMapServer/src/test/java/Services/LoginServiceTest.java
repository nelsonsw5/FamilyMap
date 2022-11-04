package Services;

import DataAccess.DataAccessException;
import DataAccess.DataBase;
import DataAccess.UserDao;
import Models.UserModel;
import Requests.LoginRequest;
import Results.LoginResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class LoginServiceTest {

    // clear db so that
    @BeforeEach
    void setUp() throws SQLException, DataAccessException {
        DataBase db = new DataBase();
        Connection connection = db.getConnection();
        UserDao uDao = new UserDao(connection);
        uDao.clear();
        uDao.insert(new UserModel("higginsjoshuag", "cool_beans12",
                "higjg@gmail.com", "Joshua", "Higgins", "m",
                "Ushiku567"));
        db.closeConnection(true);
    }

    // make sure you can log-in for a user already in the db.
    @Test
    void loginPass() throws SQLException, DataAccessException {
        LoginService loginService = new LoginService();
        LoginRequest loginRequest = new LoginRequest("higginsjoshuag","cool_beans12");
        LoginResult loginResult = loginService.login(loginRequest);
        assertTrue(loginResult.isSuccess());
    }

    @Test
    // make sure you can't log-in for a user not in the db.
    void loginFail() throws SQLException, DataAccessException {
        LoginService loginService = new LoginService();
        LoginRequest loginRequest = new LoginRequest("higginsjoshuag","wrong_password!");
        LoginResult loginResult = loginService.login(loginRequest);
        assertFalse(loginResult.isSuccess());
    }
}