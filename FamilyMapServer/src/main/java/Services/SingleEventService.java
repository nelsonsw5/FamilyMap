package Services;

import DataAccess.*;
import Models.AuthTokenModel;
import Models.EventModel;
import Results.SingleEventResult;

import java.sql.Connection;
import java.sql.SQLException;

public class SingleEventService {
    /**
     *
     * @param eventID passing in the id of the event so that you can return the event's info
     * @return a singleEventResult object saying whether the request was successfully completed.
     */
    public SingleEventResult getSingleEvent (String eventID, String authToken) throws SQLException, DataAccessException {
        DataBase db = new DataBase();
        Connection connection = db.getConnection();

        AuthTokenDao aDao = new AuthTokenDao(connection);
        if (aDao.find(authToken) == null) {
            SingleEventResult seResult = new SingleEventResult("error: couldn't find authtoken",
                    false,null,null,0,0,
                    null,null,null,0,null);
            db.closeConnection(true);
            return seResult;
        }
        AuthTokenModel authTokenFound = aDao.find(authToken);
        db.closeConnection(true);
        connection = db.getConnection();

        EventDao eDao = new EventDao(connection);

        if (eDao.find(eventID) == null) {
            SingleEventResult seResult = new SingleEventResult("error: couldn't find event",
                    false,null,null,0,0,
                    null,null,null,0,null);
            db.closeConnection(true);
            return seResult;
        }

        String username = authTokenFound.getUsername();
        if (eDao.find(eventID,username) == null) {
            SingleEventResult seResult = new SingleEventResult("error: event not associated with your username",
                    false,null,null,0,0,
                    null,null,null,0,null);
            db.closeConnection(true);
            return seResult;
        }

        EventModel event = eDao.find(eventID);
        db.closeConnection(true);

        SingleEventResult seResult = new SingleEventResult(null,
                true,event.getAssociatedUsername(),event.getEventID(),
                event.getLatitude(),event.getLongitude(),event.getCountry(),
                event.getCity(),event.getEventType(),event.getYear(),event.getPersonID());

        return seResult;
    }
}
