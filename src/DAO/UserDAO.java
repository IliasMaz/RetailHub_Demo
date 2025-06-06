package DAO;

import Connector.SQLiteConnector;
import Entities.User;
import java.util.List;
import java.util.ArrayList;
import java.sql.*;

import org.sqlite.SQLiteException;

public class UserDAO {
    public UserDAO() {}

    public boolean createUser(User user) {
        String sql = "INSERT INTO Users (name, username, password, role, email) VALUES (?, ?, ?, ?, ?)";

        try(Connection conn = SQLiteConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql,Statement.RETURN_GENERATED_KEYS)){

            statement.setString(1, user.getName());
            statement.setString(2, user.getUsername());
            statement.setString(3, user.getPassword());
            statement.setString(4, user.getRole());
            statement.setString(5, user.getEmail());

            int rowsInserted = statement.executeUpdate();

            if(rowsInserted > 0){
                try(ResultSet generatedKeys = statement.getGeneratedKeys()){
                    if(generatedKeys.next()){
                        user.setId(generatedKeys.getInt(1));
                        return true;
                    }
                }
            }


        }catch(SQLException e){
            System.err.println("Error inserting user: " + e.getMessage());
            e.printStackTrace();
        }
        return false;

    }

    public boolean updateUser(User user) {
        String sql = "UPDATE Users SET username = ?, password = ?, role = ?, email = ?, name = ? WHERE id = ?";

        try (Connection conn = SQLiteConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql)){

            statement.setString(1, user.getUsername());
            statement.setString(2, user.getPassword());
            statement.setString(3, user.getRole());
            statement.setString(4, user.getEmail());
            statement.setString(5, user.getName());
            statement.setInt(6, user.getId());

            int rowsUpdated = statement.executeUpdate();

            if(rowsUpdated > 0){
                return true;
            }

        }catch  (SQLException e){
            System.err.println("Error updating user: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteUser(User user){

        String sql = "DELETE  FROM Users WHERE id = ?";

        try (Connection conn = SQLiteConnector.getConnection();
            PreparedStatement statement = conn.prepareStatement(sql)){

            statement.setInt(1, user.getId());

            int rowsDeleted = statement.executeUpdate();

            return (rowsDeleted > 0);

        }catch (SQLException e){
            System.err.println("Error deleting user: " + e.getMessage());
            e.printStackTrace();
        }
        return false;
    }

//    public User findUserByUsername(String username){
//
//        String sql = "SELECT id, username, password, role, email, name FROM Users WHERE username = ?";
//        User user = null;
//
//        try (Connection conn = SQLiteConnector.getConnection();
//             PreparedStatement statement = conn.prepareStatement(sql)) {
//
//            statement.setString(1, username);
//            try (ResultSet rs = statement.executeQuery()) {
//                if (rs.next()) {
//                    user = new User();
//                    user.setId(rs.getInt("id"));
//                    user.setUsername(rs.getString("username"));
//                    user.setPassword(rs.getString("password"));
//                    user.setRole(rs.getString("role"));
//                    user.setEmail(rs.getString("email"));
//                    user.setName(rs.getString("name"));
//                }
//            }
//        } catch (SQLException e) {
//            System.err.println("Error fetching user by username: " + e.getMessage());
//            e.printStackTrace();
//        }
//        return user;
//    }
//
//    public User findUserById(int userId) {
//        String sql = "SELECT id, username, password, role, email, name FROM Users WHERE id = ?";
//        User user = null;
//
//        try (Connection conn = SQLiteConnector.getConnection();
//             PreparedStatement statement = conn.prepareStatement(sql)) {
//
//            statement.setInt(1, userId);
//
//            try (ResultSet rs = statement.executeQuery()) {
//                if (rs.next()) {
//                    user = new User();
//                    user.setId(rs.getInt("id"));
//                    user.setUsername(rs.getString("username"));
//                    user.setPassword(rs.getString("password"));
//                    user.setRole(rs.getString("role"));
//                    user.setEmail(rs.getString("email"));
//                    user.setName(rs.getString("name"));
//                }
//            }
//        } catch (SQLException e) {
//            System.err.println("Error fetching user by ID: " + e.getMessage());
//            e.printStackTrace();
//        }
//        return user;
//    }
//
//    public List<User> getAllUsers(){
//        List<User> users = new ArrayList<>();
//        String sql = "SELECT * FROM Users";
//
//        try (Connection conn = SQLiteConnector.getConnection();
//             PreparedStatement statement = conn.prepareStatement(sql)) {
//
//            try (ResultSet rs = statement.executeQuery()) {
//                while (rs.next()) {
//                    User user = new User();
//                    user.setId(rs.getInt("id"));
//                    user.setUsername(rs.getString("username"));
//                    user.setPassword(rs.getString("password"));
//                    user.setRole(rs.getString("role"));
//                    user.setEmail(rs.getString("email"));
//                    user.setName(rs.getString("name"));
//                    users.add(user);
//                }
//            }
//
//        }catch (SQLException e){
//            System.err.println("Error fetching all users: " + e.getMessage());
//            e.printStackTrace();
//        }
//return users;
//
//    }
public User findUserByUsername(String username) {
    String sql = "SELECT * FROM Users WHERE username = ?";
    User user = null;
    try (Connection conn = SQLiteConnector.getConnection();
         PreparedStatement statement = conn.prepareStatement(sql)) {

        statement.setString(1, username);
        try (ResultSet rs = statement.executeQuery()) {
            if (rs.next()) {
                // Δημιουργία και γέμισμα του αντικειμένου User
                user = new User();
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));         // <<-- ΣΩΣΤΗ ΑΝΑΘΕΣΗ
                user.setUsername(rs.getString("username")); // <<-- ΣΩΣΤΗ ΑΝΑΘΕΣΗ
                user.setPassword(rs.getString("password")); // <<-- ΣΩΣΤΗ ΑΝΑΘΕΣΗ
                user.setRole(rs.getString("role"));         // <<-- ΣΩΣΤΗ ΑΝΑΘΕΣΗ
                user.setEmail(rs.getString("email"));       // <<-- ΣΩΣΤΗ ΑΝΑΘΕΣΗ
            }
        }
    } catch (SQLException e) {
        System.err.println("Error fetching user by username: " + e.getMessage());
        e.printStackTrace();
    }
    return user;
}

    public User findUserById(int userId) {
        String sql = "SELECT * FROM Users WHERE id = ?";
        User user = null;
        try (Connection conn = SQLiteConnector.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {

            statement.setInt(1, userId);
            try (ResultSet rs = statement.executeQuery()) {
                if (rs.next()) {
                    user = new User();
                    user.setId(rs.getInt("id"));
                    user.setName(rs.getString("name"));         // <<-- ΣΩΣΤΗ ΑΝΑΘΕΣΗ
                    user.setUsername(rs.getString("username")); // <<-- ΣΩΣΤΗ ΑΝΑΘΕΣΗ
                    user.setPassword(rs.getString("password")); // <<-- ΣΩΣΤΗ ΑΝΑΘΕΣΗ
                    user.setRole(rs.getString("role"));         // <<-- ΣΩΣΤΗ ΑΝΑΘΕΣΗ
                    user.setEmail(rs.getString("email"));       // <<-- ΣΩΣΤΗ ΑΝΑΘΕΣΗ
                }
            }
        } catch (SQLException e) {
            System.err.println("Error fetching user by ID: " + e.getMessage());
            e.printStackTrace();
        }
        return user;
    }

    public List<User> getAllUsers() {
        List<User> users = new ArrayList<>();
        String sql = "SELECT * FROM Users";

        try (Connection conn = SQLiteConnector.getConnection();
             Statement stmt = conn.createStatement(); // Απλό Statement εδώ
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                User user = new User();
                // Ανάθεση της κάθε στήλης από το ResultSet στο ΣΩΣΤΟ πεδίο του User
                user.setId(rs.getInt("id"));
                user.setName(rs.getString("name"));         // <<-- ΣΩΣΤΗ ΑΝΑΘΕΣΗ
                user.setUsername(rs.getString("username")); // <<-- ΣΩΣΤΗ ΑΝΑΘΕΣΗ
                user.setPassword(rs.getString("password")); // <<-- ΣΩΣΤΗ ΑΝΑΘΕΣΗ
                user.setRole(rs.getString("role"));         // <<-- ΣΩΣΤΗ ΑΝΑΘΕΣΗ
                user.setEmail(rs.getString("email"));       // <<-- ΣΩΣΤΗ ΑΝΑΘΕΣΗ
                users.add(user);
            }
        } catch (SQLException e) {
            System.err.println("Error fetching all users: " + e.getMessage());
            e.printStackTrace();
        }
        return users;
    }

}
