package database.dao;

import database.Database;
import entities.User;

import java.sql.*;
import java.time.LocalDate;

/**
 * Data Access Object for User entity
 */
public class UserDao {
    private UserDao() {}

    /**
     * Checks if a user exists in the database by email
     * @param email the user's email
     * @return true if user exists, false otherwise
     * @throws SQLException if there is an error executing the SQL query
     */
    public static boolean userExists(String email) throws SQLException {
        String query = "SELECT COUNT(*) FROM Users WHERE email = ?";

        Connection connection = Database.getConnection();

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getInt(1) > 0;
                }
            }
        }

        return false;
    }

    /**
     * Gets a user by email from the database
     * @param email the user's email
     * @return User object if found, null otherwise
     * @throws SQLException if there is an error executing the SQL query
     */
    public static User getUserByEmail(String email) throws SQLException {
        String query = "SELECT name, email, date_of_birth FROM Users WHERE email = ?";

        Connection connection = Database.getConnection();

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, email);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String name = rs.getString("name");
                    String userEmail = rs.getString("email");
                    LocalDate dateOfBirth = rs.getDate("date_of_birth").toLocalDate();

                    return new User(name, userEmail, dateOfBirth);
                }
            }
        }

        return null;
    }

    /**
     * Inserts a new user into the database
     * @param user the user to insert
     * @throws SQLException if there is an error executing the SQL query
     */
    public static void insertUser(User user) throws SQLException {
        String query = "INSERT INTO Users (email, name, date_of_birth) VALUES (?, ?, ?)";

        Connection connection = Database.getConnection();

        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setString(1, user.getEmail());
            pstmt.setString(2, user.getName());
            pstmt.setDate(3, Date.valueOf(user.getDateOfBirth()));

            pstmt.executeUpdate();
        }
    }
}


