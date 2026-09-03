package com.sunrise.dao;

import com.sunrise.model.User;
import com.sunrise.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * Data Access Object (DAO) for User entities.
 * Handles all direct database operations related to user authentication.
 */
public class UserDAO {

    /**
     * Finds a user by their unique username.
     *
     * @param username the username to search for
     * @return the User object if found, or null otherwise
     * @throws SQLException if a database access error occurs
     */
    public User findByUsername(String username) throws SQLException {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }

        String sql = "SELECT user_id, username, password_hash, role FROM users WHERE username = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username.trim());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new User(
                            rs.getInt("user_id"),
                            rs.getString("username"),
                            rs.getString("password_hash"),
                            rs.getString("role")
                    );
                }
            }
        }
        return null;
    }

    /**
     * Creates a new user record in the database.
     *
     * @param user the user entity containing username, password_hash, and role
     * @return true if insertion was successful, false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean createUser(User user) throws SQLException {
        if (user == null || user.getUsername() == null || user.getPasswordHash() == null || user.getRole() == null) {
            return false;
        }

        String sql = "INSERT INTO users (username, password_hash, role) VALUES (?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, user.getUsername().trim());
            stmt.setString(2, user.getPasswordHash());
            stmt.setString(3, user.getRole().toUpperCase().trim());

            int affectedRows = stmt.executeUpdate();
            return affectedRows > 0;
        }
    }
}
