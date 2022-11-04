package DataAccess;

import Models.UserModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDao {
    Connection connection;

    public UserDao (Connection connection) { this.connection = connection; }

    /**
     * inserts a user into the user table in the db.
     * @param u user to add to user table.
     */
    public void insert(UserModel u) throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            String sql = "insert into Users (" +
                    "username, password, " +
                    "email, first_name, last_name, " +
                    "gender, person_id)"+
                    " values (?, ?, ?, ?, ?, ?, ?)";

            stmt = connection.prepareStatement(sql);
            stmt.setString(1, u.getUsername());
            stmt.setString(2, u.getPassword());
            stmt.setString(3, u.getEmail());
            stmt.setString(4, u.getFirstName());
            stmt.setString(5, u.getLastName());
            stmt.setString(6, u.getGender());
            stmt.setString(7, u.getPersonID());
            stmt.executeUpdate();

        }
        catch (SQLException e) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * returns a user if found; null otherwise
     * @return user if found; null otherwise
     */
    public UserModel find(String username) throws DataAccessException {
        UserModel u;
        ResultSet rs = null;
        String sql = "select * from Users where username = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, username);
            rs = stmt.executeQuery();
            if (rs.next()) {
                u = new UserModel(rs.getString("username"),rs.getString("password"),
                        rs.getString("email"),rs.getString("first_name"),rs.getString("last_name"),
                        rs.getString("gender"),rs.getString("person_id"));
                return u;
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
     * deletes a user from the user table  in the db.
     * @param u user to delete from table.
     */
    public void delete(UserModel u) throws SQLException {
        PreparedStatement stmt = null;
        try {
            String sql = "delete from Users where username = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, u.getUsername());
            stmt.executeUpdate();
        }
        finally {
            if (stmt != null) { stmt.close(); }
        }
    }

    /**
     * clears the user table in the db.
     */
    public void clear() throws SQLException {
        PreparedStatement stmt = null;
        try {
            String sql = "delete from Users";
            stmt = connection.prepareStatement(sql);
            stmt.executeUpdate();
        }
        finally {
            if (stmt != null) { stmt.close(); }
        }
    }
}
