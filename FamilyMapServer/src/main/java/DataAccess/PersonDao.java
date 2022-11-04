package DataAccess;

import Models.PersonModel;

import java.sql.*;
import java.util.ArrayList;

public class PersonDao {
    Connection connection;

    public PersonDao(Connection connection) {
        this.connection = connection;
    }

    /**
     * takes in a person and inserts into the person table in db.
     * @param p person to add to db.
     */
    public void insert(PersonModel p) throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            String sql = "insert into Persons (" +
                    "person_id, associated_username, " +
                    "first_name, last_name, gender, " +
                    "spouse_id, father_id, mother_id)"+
                    " values (?, ?, ?, ?, ?, ?, ?, ?)";

            stmt = connection.prepareStatement(sql);
            stmt.setString(1, p.getPersonID());
            stmt.setString(2, p.getAssociatedUsername());
            stmt.setString(3, p.getFirstName());
            stmt.setString(4, p.getLastName());
            stmt.setString(5, p.getGender());
            stmt.setString(6, p.getSpouseID());
            stmt.setString(7, p.getFatherID());
            stmt.setString(8, p.getMotherID());
            stmt.executeUpdate();
        }
        catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * returns a person if found, null otherwise
     * @param personID person to find in the person table
     * @return person if found, null otherwise.
     */
    public PersonModel find (String personID) throws DataAccessException {
        PersonModel p;
        ResultSet rs = null;
        String sql = "select * from Persons where person_id = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, personID);
            rs = stmt.executeQuery();
            if (rs.next()) {
                p = new PersonModel(rs.getString("person_id"),rs.getString("associated_username"),
                        rs.getString("first_name"),rs.getString("last_name"),rs.getString("gender"),
                        rs.getString("spouse_id"),rs.getString("father_id"),rs.getString("mother_id"));
                return p;
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

    public PersonModel find (String personID, String associatedUsername) throws DataAccessException {
        PersonModel p;
        ResultSet rs = null;
        String sql = "select * from Persons where person_id = ? and associated_username = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, personID);
            stmt.setString(2, associatedUsername);
            rs = stmt.executeQuery();
            if (rs.next()) {
                p = new PersonModel(rs.getString("person_id"),rs.getString("associated_username"),
                        rs.getString("first_name"),rs.getString("last_name"),rs.getString("gender"),
                        rs.getString("spouse_id"),rs.getString("father_id"),rs.getString("mother_id"));
                return p;
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

    /**
     * finds persons who have associated username of inputted username
     * @param username username to check if in associated_username column
     * @return an array of PersonModel objects
     * @throws DataAccessException
     */
    public PersonModel[] findPersons (String username) throws DataAccessException {
        ArrayList<PersonModel> persons = new ArrayList<>();
        PersonModel p;
        ResultSet rs = null;
        String sql = "select * from Persons where associated_username = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            while (rs.next()) {
                p = new PersonModel(rs.getString("person_id"),rs.getString("associated_username"),
                        rs.getString("first_name"),rs.getString("last_name"),rs.getString("gender"),
                        rs.getString("spouse_id"),rs.getString("father_id"),rs.getString("mother_id"));
                persons.add(p);
            }
            PersonModel[] persons1 = new PersonModel[persons.size()];
            persons.toArray(persons1);
            return persons1;
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
     * deletes a person object from the person table in db.
     * @param p person to delete from person table
     */
    public void delete(PersonModel p) throws SQLException {
        PreparedStatement stmt = null;
        try {
            String sql = "delete from Persons where person_id = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, p.getPersonID());
            stmt.executeUpdate();
        }
        finally {
            if (stmt != null) { stmt.close(); }
        }
    }

    /**
     * clears the person table in db.
     */
    public void clear() throws SQLException {
        PreparedStatement stmt = null;
        try {
            String sql = "delete from Persons";
            stmt = connection.prepareStatement(sql);
            stmt.executeUpdate();
        }
        finally {
            if (stmt != null) { stmt.close(); }
        }
    }
}
