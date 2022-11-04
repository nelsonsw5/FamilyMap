package Services;

import DataAccess.*;
import Models.EventModel;
import Results.AllEventsResult;

import java.sql.Connection;
import java.sql.SQLException;

public class AllEventsService {
    /**
     *
     * @return an allEventsResult object saying whether the request was successfully completed.
     */
    public AllEventsResult getAllEvents(String authToken) throws SQLException, DataAccessException {
        DataBase db = new DataBase();
        Connection connection = db.getConnection();
        AuthTokenDao aDao = new AuthTokenDao(connection);


        if (aDao.find(authToken) == null) {
            db.closeConnection(true);
            return new AllEventsResult(
                    "error: couldn't find username from authToken",
                    false, null);
        }

        String username = aDao.find(authToken).getUsername();
        db.closeConnection(true);
        connection = db.getConnection();

        EventDao eDao = new EventDao(connection);
        EventModel[] events = eDao.findEvents(username);
        db.closeConnection(true);
        return new AllEventsResult("success",true, events);
    }
}
