package Services;

import DataAccess.*;
import Results.ClearResult;

import javax.xml.crypto.Data;
import java.sql.Connection;
import java.sql.SQLException;

public class ClearService {
    /**
     *
     * @return an clearResult object saying whether the request was successfully completed.
     */
    public ClearResult clear () throws SQLException {

        DataBase db = new DataBase();
        Connection connection;
        try {
            connection = db.getConnection();
            UserDao uDao = new UserDao(connection);
            uDao.clear();
            db.closeConnection(true);

            connection = db.getConnection();
            PersonDao pDao = new PersonDao(connection);
            pDao.clear();
            db.closeConnection(true);

            connection = db.getConnection();
            EventDao eDao = new EventDao(connection);
            eDao.clear();
            db.closeConnection(true);

            connection = db.getConnection();
            AuthTokenDao aDao = new AuthTokenDao(connection);
            aDao.clear();
            db.closeConnection(true);
        }
        catch (SQLException e) {
            ClearResult clearResult = new ClearResult("Error: could not clear databases.", false);
            e.printStackTrace();
        }
        return new ClearResult("Clear succeeded.", true);
    }
}
