package DataAccess;

import Models.AuthTokenModel;
import Models.UserModel;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class AuthTokenDao {

    private final Connection connection;

    public AuthTokenDao (Connection connection) {
        this.connection = connection;
    }
    /**
     * inserts authToken into db.
     * @param a authToken to put into db
     */
    public void insert(AuthTokenModel a) throws DataAccessException {
        PreparedStatement stmt = null;
        try {
            String sql = "insert into AuthToken (" +
                    "auth_token, username)"+
                    " values (?, ?)";

            stmt = connection.prepareStatement(sql);
            stmt.setString(1, a.getAuthToken());
            stmt.setString(2, a.getUsername());
            stmt.executeUpdate();
        }
        catch (SQLException em) {
            throw new DataAccessException("Error encountered while inserting into the database");
        }
    }

    /**
     * if found, returns authToken in db.
     * @return authToken found in the db. null if not found.
     */
    public AuthTokenModel find(String authToken) throws DataAccessException {
        AuthTokenModel a;
        ResultSet rs = null;
        String sql = "select * from AuthToken where auth_token = ?;";
        try (PreparedStatement stmt = connection.prepareStatement(sql)){
            stmt.setString(1, authToken);
            rs = stmt.executeQuery();
            if (rs.next()) {
                a = new AuthTokenModel(rs.getString("auth_token"),rs.getString("username"));
                return a;
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
     * deletes a given authToken from db.
     * @param a authToken to delete from db.
     */
    public void delete(AuthTokenModel a) throws SQLException {
        PreparedStatement stmt = null;
        try {
            String sql = "delete from AuthToken where auth_token = ?";
            stmt = connection.prepareStatement(sql);
            stmt.setString(1, a.getAuthToken());
            stmt.executeUpdate();
        }
        finally {
            if (stmt != null) { stmt.close(); }
        }
    }

    /**
     * clears the authToken table in the db.
     */
    public void clear() throws SQLException {
        PreparedStatement stmt = null;
        try {
            String sql = "delete from AuthToken";
            stmt = connection.prepareStatement(sql);
            stmt.executeUpdate();
        }
        finally {
            if (stmt != null) { stmt.close(); }
        }
    }
}
