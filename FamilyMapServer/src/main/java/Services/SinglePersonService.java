package Services;

import DataAccess.AuthTokenDao;
import DataAccess.DataAccessException;
import DataAccess.DataBase;
import DataAccess.PersonDao;
import Models.AuthTokenModel;
import Models.PersonModel;
import Results.SinglePersonResult;

import java.sql.Connection;
import java.sql.SQLException;

public class SinglePersonService {
    /**
     *
     * @param personID passing in a person so that you can return that person's info
     * @return a singlePersonResult object saying whether the request was successfully completed.
     */
    public SinglePersonResult getSinglePerson(String personID, String authToken) throws DataAccessException, SQLException {
        DataBase db = new DataBase();
        Connection connection = db.getConnection();
        AuthTokenDao aDao = new AuthTokenDao(connection);

        if (aDao.find(authToken) == null) {
            SinglePersonResult spResult = new SinglePersonResult("error: couldn't find authtoken",
                    false,null,null,null,null,
                    null,null,null,null);
            db.closeConnection(true);
            return spResult;
        }
        AuthTokenModel authTokenFound = aDao.find(authToken);
        db.closeConnection(true);
        connection = db.getConnection();

        PersonDao pDao = new PersonDao(connection);
        if (pDao.find(personID) == null) {
            SinglePersonResult spResult = new SinglePersonResult("error: couldn't find person",
                    false,null,null,null,null,
                    null,null,null,null);
            db.closeConnection(true);
            return spResult;
        }

        String username = authTokenFound.getUsername();
        if (pDao.find(personID, username) == null) {
            SinglePersonResult spResult = new SinglePersonResult("error: person not associated with your username",
                    false,null,null,null,null,
                    null,null,null,null);
            db.closeConnection(true);
            return spResult;
        }
        PersonModel person = pDao.find(personID);
        db.closeConnection(true);

        SinglePersonResult spResult = new SinglePersonResult(null,true,
                person.getAssociatedUsername(),person.getPersonID(),person.getFirstName(),
                person.getLastName(),person.getGender(),person.getFatherID(),person.getMotherID(),
                person.getSpouseID());

        return spResult;
    }
}
