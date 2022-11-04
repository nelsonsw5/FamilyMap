package Services;

import DataAccess.DataAccessException;
import DataAccess.DataBase;
import Generate.LocationData;
import Generate.TreeGeneration;
import Requests.FillRequest;
import Results.FillResult;
import com.google.gson.Gson;

import javax.xml.crypto.Data;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.sql.SQLException;

public class FillService {
    /**
     *
     * @param fr pass in a fillRequest object
     * @return a fillResults object saying whether the request was successfully completed.
     */
    public FillResult fill (FillRequest fr) throws FileNotFoundException, SQLException, DataAccessException {
        DataBase db = new DataBase();
        Connection connection = db.getConnection();

        TreeGeneration treeGeneration = new TreeGeneration(connection);
        treeGeneration.generate(fr.getUsername(),fr.getGenerations(),fr.getPersonID());
        db.closeConnection(true);

        FillResult fillResult = new FillResult("Successfully added "+
                treeGeneration.getNumPeopleAdded()+" persons and "+
                treeGeneration.getNumEventsAdded()+" events to the database.", true);
        return fillResult;
    }
}
