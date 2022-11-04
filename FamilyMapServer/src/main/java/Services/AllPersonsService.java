package Services;

import DataAccess.AuthTokenDao;
import DataAccess.DataAccessException;
import DataAccess.DataBase;
import DataAccess.PersonDao;
import Models.PersonModel;
import Results.AllPersonsResult;

import java.sql.Connection;
import java.sql.SQLException;

public class AllPersonsService {
    /**
     *
     * @return an allPersonsResult object saying whether the request was successfully completed.
     */
    public AllPersonsResult getAllPersons (String authToken) throws SQLException, DataAccessException {
        DataBase db = new DataBase();
        Connection connection = db.getConnection();

        AuthTokenDao aDao = new AuthTokenDao(connection);
        if (aDao.find(authToken) == null) {
            db.closeConnection(true);
            return new AllPersonsResult(
                    "error: couldn't find username from authToken",
                    false, null);
        }
        String username = aDao.find(authToken).getUsername();
        db.closeConnection(true);
        connection = db.getConnection();
        PersonDao pDao = new PersonDao(connection);
        PersonModel[] persons = pDao.findPersons(username);
        db.closeConnection(true);
        return new AllPersonsResult("success",true, persons);
    }
}
