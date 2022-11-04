package DataAccess;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class DataBase {

    final String dbFile = "fms.sqlite";
    final String connectionURL = "jdbc:sqlite:" + dbFile;

    private Connection connection;
    public DataBase() { connection = null; }

    public void loadDatabaseDriver() throws ClassNotFoundException {
        final String driver = "org.sqlite.JDBC";
        Class.forName(driver);
    }

    public void openConnection() throws SQLException {
        connection = DriverManager.getConnection(connectionURL);
        connection.setAutoCommit(false);
    }

    public Connection getConnection() throws SQLException {
        if (connection == null) {
            openConnection();
        }
        return connection;
    }

    public void closeConnection(boolean commit) throws SQLException {
        if (commit) { connection.commit(); }
        else { connection.rollback(); }
        connection = null;
    }

    public void deleteAllData() throws SQLException {
        PreparedStatement stmt = null;
        try {
            String sql = "delete from Users;" +
                    "delete from Persons;" +
                    "delete from AuthToken;" +
                    "delete from Events;";
            stmt = connection.prepareStatement(sql);
            stmt.executeUpdate();
        }
        finally {
            if (stmt != null) { stmt.close(); }
        }
    }
}




//    public void createTables() throws SQLException {
//        PreparedStatement stmt = null;
//        try {
//            String sql =
//                    "CREATE TABLE IF NOT EXISTS Users (" +
//                    "username TEXT NOT NULL UNIQUE," +
//                    "password TEXT NOT NULL," +
//                    "email TEXT NOT NULL UNIQUE," +
//                    "first_name TEXT NOT NULL," +
//                    "last_name TEXT NOT NULL," +
//                    "gender TEXT NOT NULL," +
//                    "person_id TEXT NOT NULL," +
//                    "PRIMARY KEY(username)" +
//                    ");" +
//                    "CREATE TABLE IF NOT EXISTS Persons (" +
//                    "person_id TEXT NOT NULL UNIQUE," +
//                    "associated_username TEXT NOT NULL," +
//                    "first_name TEXT NOT NULL," +
//                    "last_name TEXT NOT NULL," +
//                    "gender TEXT NOT NULL," +
//                    "spouse_id TEXT," +
//                    "father_id TEXT," +
//                    "mother_id TEXT," +
//                    "PRIMARY KEY(person_id)" +
//                    ");" +
//                    "CREATE TABLE IF NOT EXISTS Events (" +
//                    "event_id TEXT NOT NULL UNIQUE," +
//                    "associated_username TEXT NOT NULL," +
//                    "person_id TEXT NOT NULL," +
//                    "latitude NUMERIC NOT NULL," +
//                    "longitude NUMERIC NOT NULL," +
//                    "country TEXT NOT NULL," +
//                    "city TEXT NOT NULL," +
//                    "event_type TEXT NOT NULL," +
//                    "year INTEGER NOT NULL," +
//                    "PRIMARY KEY(event_id)" +
//                    ");" +
//                    "CREATE TABLE IF NOT EXISTS AuthToken (" +
//                    "auth_token TEXT NOT NULL UNIQUE," +
//                    "username TEXT NOT NULL," +
//                    "PRIMARY KEY(auth_token)" +
//                    ");";
//            stmt = connection.prepareStatement(sql);
//            stmt.execute();
//        }
//        finally {
//            if (stmt != null) { stmt.close(); }
//        }
//    }

