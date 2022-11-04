package Generate;

import DataAccess.*;
import Models.EventModel;
import Models.PersonModel;
import Models.UserModel;
import com.google.gson.Gson;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.Reader;
import java.sql.Connection;
import java.util.Random;
import java.util.UUID;

public class TreeGeneration {
    private static final String momGender = "f";
    private static final String dadGender = "m";
    private static final int userBirthYear = 2000;
    private static final int generationGapYears = 25;
    String rootUsername;
    Gson gson;
    Connection connection;
    Reader reader1;
    Reader reader2;
    Reader reader3;
    Reader reader4;
    LocationData locData;
    MNames mNames;
    FNames fNames;
    SNames sNames;
    PersonDao pDao;
    UserDao uDao;
    EventDao eDao;

    int numPeopleAdded;
    int numEventsAdded;

    public int getNumPeopleAdded() { return numPeopleAdded; }
    public int getNumEventsAdded() { return numEventsAdded; }

    public TreeGeneration(Connection connection) throws FileNotFoundException {
        this.connection = connection;
        reader1 = new FileReader("json/locations.json");
        reader2 = new FileReader("json/mnames.json");
        reader3 = new FileReader("json/fnames.json");
        reader4 = new FileReader("json/snames.json");
        gson = new Gson();
        locData = gson.fromJson(reader1, LocationData.class);
        mNames = gson.fromJson(reader2, MNames.class);
        fNames = gson.fromJson(reader3, FNames.class);
        sNames = gson.fromJson(reader4, SNames.class);
        pDao = new PersonDao(connection);
        uDao = new UserDao(connection);
        eDao = new EventDao(connection);
    }

    public void generate(String username, int numGenerations, String rootPersonID) throws DataAccessException {
        rootUsername = username;

        // make root user's UserModel
        if (rootPersonID == null) {
            rootPersonID = UUID.randomUUID().toString();
        }
        UserModel rootUser = uDao.find(username);

        // make root user's PersonModel
        PersonModel rootPerson = new PersonModel(rootPersonID,username,rootUser.getFirstName(),rootUser.getLastName(),
                rootUser.getGender(),null,null,null);

        //make and add birth for user
        String userBirthID = UUID.randomUUID().toString();
        Location randomLoc = locData.getData()[getRandomNumber(locData.getData().length-1)];
        EventModel userBirthday = new EventModel(userBirthID,username,rootPersonID,
                randomLoc.getLatitude(),randomLoc.getLongitude(),randomLoc.getCountry(),
                randomLoc.getCity(),"birth", userBirthYear);
        numEventsAdded++;
        eDao.insert(userBirthday);

        generateParents(rootPerson, numGenerations, userBirthYear);
    }

    private void generateParents(PersonModel personIn, int numGenerations, int personInBirthYear) throws DataAccessException {
        if (numGenerations == 0) {
            numPeopleAdded++;
            pDao.insert(personIn);
            return;
        }

        //generate mom's data
        String momPersonID = UUID.randomUUID().toString();
        personIn.setMotherID(momPersonID);

        //generate dad's data
        String dadPersonID = UUID.randomUUID().toString();
        personIn.setFatherID(dadPersonID);

        numPeopleAdded++;
        pDao.insert(personIn);

        // make mom
        PersonModel mom = new PersonModel(momPersonID, rootUsername,
                fNames.getData()[getRandomNumber(fNames.getData().length-1)],
                sNames.getData()[getRandomNumber(sNames.getData().length-1)],
                momGender,dadPersonID,null,null);

        // make dad
        PersonModel dad = new PersonModel(dadPersonID, rootUsername,
                mNames.getData()[getRandomNumber(mNames.getData().length-1)],
                personIn.getLastName(), dadGender,momPersonID,null,null);

        generateParentEvents(dadPersonID,momPersonID,personInBirthYear);

        int momDadBirthYear = personInBirthYear - generationGapYears;

        generateParents(mom, numGenerations-1, momDadBirthYear);
        generateParents(dad, numGenerations-1, momDadBirthYear);
    }

    private void generateParentEvents(String dadID, String momID, int childBirthYear) throws DataAccessException {
        // DAD BIRTH
        Location dadBirthLocation = locData.getData()[getRandomNumber(locData.getData().length-1)];
        String dadBirthID = UUID.randomUUID().toString();
        EventModel dadBirth = new EventModel(dadBirthID,rootUsername,dadID,dadBirthLocation.getLatitude(),
                dadBirthLocation.getLongitude(),dadBirthLocation.getCountry(), dadBirthLocation.getCity(),
                "birth",childBirthYear-generationGapYears);
        numEventsAdded++;
        eDao.insert(dadBirth);

        // MOM BIRTH
        Location momBirthLocation = locData.getData()[getRandomNumber(locData.getData().length-1)];
        String momBirthID = UUID.randomUUID().toString();
        EventModel momBirth = new EventModel(momBirthID,rootUsername,momID,momBirthLocation.getLatitude(),
                momBirthLocation.getLongitude(),momBirthLocation.getCountry(), momBirthLocation.getCity(),
                "birth",childBirthYear-generationGapYears);
        numEventsAdded++;
        eDao.insert(momBirth);

        // MARRIAGE LOCATION
        Location marriageLocation = locData.getData()[getRandomNumber(locData.getData().length-1)];

        // DAD MARRIAGE
        String dadMarriageID = UUID.randomUUID().toString();
        EventModel dadMarriage = new EventModel(dadMarriageID,rootUsername,dadID,marriageLocation.getLatitude(),
                marriageLocation.getLongitude(),marriageLocation.getCountry(), marriageLocation.getCity(),
                "marriage",childBirthYear);
        numEventsAdded++;
        eDao.insert(dadMarriage);

        // MOM MARRIAGE
        String momMarriageID = UUID.randomUUID().toString();
        EventModel momMarriage = new EventModel(momMarriageID,rootUsername,momID,marriageLocation.getLatitude(),
                marriageLocation.getLongitude(),marriageLocation.getCountry(), marriageLocation.getCity(),
                "marriage",childBirthYear);
        numEventsAdded++;
        eDao.insert(momMarriage);

        // MOM/DAD DEATH LOCATION
        Location deathLocation = locData.getData()[getRandomNumber(locData.getData().length-1)];

        // DAD DEATH
        String dadDeathID = UUID.randomUUID().toString();
        EventModel dadDeath = new EventModel(dadDeathID,rootUsername,dadID,deathLocation.getLatitude(),
                deathLocation.getLongitude(),deathLocation.getCountry(), deathLocation.getCity(),
                "death",childBirthYear + generationGapYears);
        numEventsAdded++;
        eDao.insert(dadDeath);

        // MOM DEATH
        String momDeathID = UUID.randomUUID().toString();
        EventModel momDeath = new EventModel(momDeathID,rootUsername,momID,deathLocation.getLatitude(),
                deathLocation.getLongitude(),deathLocation.getCountry(), deathLocation.getCity(),
                "death",childBirthYear + generationGapYears);
        numEventsAdded++;
        eDao.insert(momDeath);
    }

    public int getRandomNumber(int max) {
        Random random = new Random();
        return random.nextInt(max);
    }
}

