package Services;

import DataAccess.*;
import Encoder.ObjectEncoder;
import Handlers.LoadHandler;
import Models.AuthTokenModel;
import Models.EventModel;
import Models.PersonModel;
import Requests.LoadRequest;
import Results.LoadResult;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

class LoadServiceTest {

    String loadString = "{\n" +
            "   \"users\":[\n" +
            "      {\n" +
            "         \"username\":\"sheila\",\n" +
            "         \"password\":\"parker\",\n" +
            "         \"email\":\"sheila@parker.com\",\n" +
            "         \"firstName\":\"Sheila\",\n" +
            "         \"lastName\":\"Parker\",\n" +
            "         \"gender\":\"f\",\n" +
            "         \"personID\":\"Sheila_Parker\"\n" +
            "      },\n" +
            "      {\n" +
            "         \"username\":\"patrick\",\n" +
            "         \"password\":\"spencer\",\n" +
            "         \"email\":\"patrick@spencer.com\",\n" +
            "         \"firstName\":\"Patrick\",\n" +
            "         \"lastName\":\"Spencer\",\n" +
            "         \"gender\":\"m\",\n" +
            "         \"personID\":\"Patrick_Spencer\"\n" +
            "      }\n" +
            "   ],\n" +
            "   \"persons\":[\n" +
            "      {\n" +
            "         \"firstName\":\"Spencer\",\n" +
            "         \"lastName\":\"Seeger\",\n" +
            "         \"gender\":\"f\",\n" +
            "         \"personID\":\"Golden_Boy\",\n" +
            "         \"associatedUsername\":\"patrick\",\n" +
            "         \"spouseID\":\"Happy_Birthday\"\n" +
            "      }\n" +
            "   ],\n" +
            "   \"events\":[\n" +
            "      {\n" +
            "         \"eventType\":\"birth\",\n" +
            "         \"personID\":\"Sheila_Parker\",\n" +
            "         \"city\":\"Melbourne\",\n" +
            "         \"country\":\"Australia\",\n" +
            "         \"latitude\":-36.1833,\n" +
            "         \"longitude\":144.9667,\n" +
            "         \"year\":1970,\n" +
            "         \"eventID\":\"Sheila_Birth\",\n" +
            "         \"associatedUsername\":\"sheila\"\n" +
            "      },\n" +
            "      {\n" +
            "         \"eventType\":\"marriage\",\n" +
            "         \"personID\":\"Golden_Boy\",\n" +
            "         \"city\":\"Boise\",\n" +
            "         \"country\":\"United States\",\n" +
            "         \"latitude\":43.6167,\n" +
            "         \"longitude\":-115.8,\n" +
            "         \"year\":2016,\n" +
            "         \"eventID\":\"Together_Forever\",\n" +
            "         \"associatedUsername\":\"patrick\"\n" +
            "      }\n" +
            "   ]\n" +
            "}\n" +
            "\n";



    @BeforeEach
    void setUp() throws SQLException {
        DataBase db = new DataBase();
        Connection connection = db.getConnection();

        EventDao eDao = new EventDao(connection);
        PersonDao pDao = new PersonDao(connection);
        UserDao uDao = new UserDao(connection);
        AuthTokenDao aDao = new AuthTokenDao(connection);

        eDao.clear();
        pDao.clear();
        aDao.clear();
        uDao.clear();

        db.closeConnection(true);
    }


    @Test
    void loadPass() throws SQLException, DataAccessException {
        LoadService loadService = new LoadService();
        LoadRequest loadRequest = ObjectEncoder.deserialize(loadString,LoadRequest.class);
        LoadResult loadResult = loadService.load(loadRequest);
        assertTrue(loadResult.isSuccess());
    }

    // test to see if it can add when there's already data in the db.
    // if the load didn't delete, it would throw an SQL error bc eventID must be unique.
    @Test
    void loadPassWithPriorData() throws SQLException, DataAccessException {
        DataBase db = new DataBase();
        Connection connection = db.getConnection();
        String authTokenString = "asdfasdf";

        EventModel eventToAdd = new EventModel("Together_Forever", "jchan132","1234aasdf",12341,1234,"USA","Provo","kickingButt",2021);
        PersonModel personToAdd = new PersonModel("Biking_123A", "jchan132", "Gale", "Frost", "f", "Japan234", "Ushiku567", "Biking_Around123");
        AuthTokenModel authTokenToAdd = new AuthTokenModel(authTokenString,"jchan132");

        EventDao eDao = new EventDao(connection);
        PersonDao pDao = new PersonDao(connection);
        AuthTokenDao aDao = new AuthTokenDao(connection);

        eDao.clear();
        pDao.clear();
        aDao.clear();

        eDao.insert(eventToAdd);
        pDao.insert(personToAdd);
        aDao.insert(authTokenToAdd);

        db.closeConnection(true);

        LoadService loadService = new LoadService();
        LoadRequest loadRequest = ObjectEncoder.deserialize(loadString,LoadRequest.class);
        LoadResult loadResult = loadService.load(loadRequest);

        assertTrue(loadResult.isSuccess());

    }






}