package Services;

import DataAccess.*;
import Requests.LoadRequest;
import Results.LoadResult;

import java.sql.Connection;
import java.sql.SQLException;

public class LoadService {
    /**
     *
     * @param lr passing in a loadRequest object
     * @return a loadResult object saying whether the request was successfully completed.
     */
    public LoadResult load (LoadRequest lr) throws SQLException, DataAccessException {
        DataBase db = new DataBase();
        Connection connection = db.getConnection();

        EventDao eDao = new EventDao(connection);
        eDao.clear();
        for (int i = 0; i < lr.getEvents().length; ++i) {
            eDao.insert(lr.getEvents()[i]);
        }
        db.closeConnection(true);
        connection = db.getConnection();

        UserDao uDao = new UserDao(connection);
        uDao.clear();
        for (int i = 0; i < lr.getUsers().length; ++i) {
            uDao.insert(lr.getUsers()[i]);
        }
        db.closeConnection(true);
        connection = db.getConnection();

        PersonDao pDao = new PersonDao(connection);
        pDao.clear();
        for (int i = 0; i < lr.getPersons().length; ++i) {
            pDao.insert(lr.getPersons()[i]);
        }
        db.closeConnection(true);

        return new LoadResult("Successfully added " +
                lr.getUsers().length + " users, "+ lr.getPersons().length +
                " persons, and " + lr.getEvents().length + " events to the database.",true);
    }
}
