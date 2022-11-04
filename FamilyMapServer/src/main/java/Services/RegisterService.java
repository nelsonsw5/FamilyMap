package Services;

import DataAccess.*;
import Models.AuthTokenModel;
import Models.UserModel;
import Requests.RegisterRequest;
import Results.RegisterResult;

import java.sql.Connection;
import java.sql.SQLException;
import java.util.UUID;

public class RegisterService {
    /**
     *
     * @param rr passing in a registerRequest object with user credentials.
     * @return a registerResult object saying whether the request was successfully completed.
     */
    public RegisterResult register (RegisterRequest rr) throws SQLException, DataAccessException {
        DataBase db = new DataBase();
        Connection connection = db.getConnection();

        UserDao uDao = new UserDao(connection);

        //check to see if user's already registered
        if (uDao.find(rr.getUsername()) != null) {
            db.closeConnection(true);
            return new RegisterResult("error: user already registered",false,
                    null,null,null);
        }

        String personID = UUID.randomUUID().toString();
        UserModel userToAdd = new UserModel(rr.getUsername(), rr.getPassword(),
                rr.getEmail(), rr.getFirstName(), rr.getLastName(), rr.getGender(), personID);
        uDao.insert(userToAdd);
        db.closeConnection(true);
        connection = db.getConnection();

        AuthTokenDao aDao = new AuthTokenDao(connection);
        String authTokenString = UUID.randomUUID().toString();
        AuthTokenModel authToken = new AuthTokenModel(authTokenString, rr.getUsername());
        aDao.insert(authToken);

        db.closeConnection(true);
        return new RegisterResult(null,true,authTokenString,rr.getUsername(),personID);
    }
}
