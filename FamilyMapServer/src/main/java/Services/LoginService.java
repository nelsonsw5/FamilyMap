package Services;

import DataAccess.AuthTokenDao;
import DataAccess.DataAccessException;
import DataAccess.DataBase;
import DataAccess.UserDao;
import Models.AuthTokenModel;
import Models.UserModel;
import Requests.LoginRequest;
import Results.LoginResult;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

public class LoginService {
    /**
     *
     * @param lr passing in a loginRequest object with info about the login.
     * @return a loginResult object saying whether the request was successfully completed.
     */
    public LoginResult login(LoginRequest lr) throws DataAccessException, SQLException {
        DataBase db = new DataBase();

        Connection connection = db.getConnection();
        AuthTokenDao aDao = new AuthTokenDao(connection);
        String authTokenString = UUID.randomUUID().toString();
        AuthTokenModel authToken = new AuthTokenModel(authTokenString, lr.getUsername());
        aDao.insert(authToken);
        db.closeConnection(true);

        connection = db.getConnection();
        UserDao uDao = new UserDao(connection);

        if (uDao.find(lr.getUsername()) == null) {
            db.closeConnection(true);
            return new LoginResult("Error: User not found", false, null,null,null);
        }

        if (!uDao.find(lr.getUsername()).getPassword().equals(lr.getPassword()) ) {
            db.closeConnection(true);
            return new LoginResult("Error: Invalid password", false, null,null,null);
        }

        UserModel user = uDao.find(lr.getUsername());

        db.closeConnection(true);
        return new LoginResult(null,true, authTokenString, user.getUsername(), user.getPersonID());
    };

}
