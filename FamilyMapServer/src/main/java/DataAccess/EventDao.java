package DataAccess;

import Models.EventModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

public class EventDao {
    Connection connection;

    public EventDao (Connection connection) {this.connection = connection; }
    /**
     * simply inserts an event into the db.
     * @param e event to insert into db.
     */
    public void insert(EventModel e) throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            String sql = "insert into Events (" +
                    "event_id, associated_username, " +
                    "person_id, latitude, longitude, " +
                    "country, city, event_type, year)"+
                    " values (?, ?, ?, ?, ?, ?, ?, ?, ?)";

            stmt = connection.prepareStatement(sql);
            stmt.setString(1, e.getEventID());
            stmt.setString(2, e.getAssociatedUsername());
            stmt.setString(3, e.getPersonID());
            stmt.setFloat(4, e.getLatitude());
            stmt.setFloat(5, e.getLongitude());
            stmt.setString(6, e.getCountry());
            stmt.setString(7, e.getCity());
            stmt.setString(8, e.getEventType());
            stmt.setInt(9, e.getYear());
            stmt.executeUpdate();

        }
        catch (SQLException em) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     *  if found, returns an event from the db.
     * @return event if found; otherwise, return null.
     */
    public EventModel find(String event_id) throws DataAccessException {
        EventModel em;
        ResultSet rs = null;
        String sql = "select * from Events where event_id = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, event_id);
            rs = stmt.executeQuery();
            if (rs.next()) {
                em = new EventModel(rs.getString("event_id"),rs.getString("associated_username"),
                        rs.getString("person_id"),rs.getFloat("latitude"),rs.getFloat("longitude"),
                        rs.getString("country"),rs.getString("city"),rs.getString("event_type"),
                        rs.getInt("year"));
                return em;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        }
        finally {
            if (rs != null) {
                try {
                    rs.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }

    public EventModel find(String event_id, String associatedUsername) throws DataAccessException {
        EventModel em;
        ResultSet rs = null;
        String sql = "select * from Events where event_id = ? and associated_username = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, event_id);
            stmt.setString(2,associatedUsername);
            rs = stmt.executeQuery();
            if (rs.next()) {
                em = new EventModel(rs.getString("event_id"),rs.getString("associated_username"),
                        rs.getString("person_id"),rs.getFloat("latitude"),rs.getFloat("longitude"),
                        rs.getString("country"),rs.getString("city"),rs.getString("event_type"),
                        rs.getInt("year"));
                return em;
            }
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        }
        finally {
            if (rs != null) {
                try {
                    rs.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
        return null;
    }



    public EventModel[] findEvents (String username) throws DataAccessException {
        ArrayList<EventModel> events = new ArrayList<>();
        EventModel em;
        ResultSet rs = null;
        String sql = "select * from Events where associated_username = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            while (rs.next()) {
                em = new EventModel(rs.getString("event_id"),rs.getString("associated_username"),
                        rs.getString("person_id"),rs.getFloat("latitude"),rs.getFloat("longitude"),
                        rs.getString("country"),rs.getString("city"),rs.getString("event_type"),
                        rs.getInt("year"));
                events.add(em);
            }
            EventModel[] events1 = new EventModel[events.size()];
            events.toArray(events1);
            return events1;
        }
        catch (SQLException e) {
            e.printStackTrace();
            throw new DataAccessException("Error encountered while finding event");
        }
        finally {
            if (rs != null) {
                try {
                    rs.close();
                }
                catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * deletes an event from db.
     * @param e event to delete
     */
    public void delete(EventModel e) throws SQLException {
        PreparedStatement stmt = null;
        try {
            String sql = "delete from Events where event_id = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, e.getEventID());
            stmt.executeUpdate();
        }
        finally {
            if (stmt != null) { stmt.close(); }
        }
    }

    /**
     * clears the event table in db.
     */
    public void clear() throws SQLException {
        PreparedStatement stmt = null;
        try {
            String sql = "delete from Events";
            stmt = connection.prepareStatement(sql);
            stmt.executeUpdate();
        }
        finally {
            if (stmt != null) { stmt.close(); }
        }
    }
}
