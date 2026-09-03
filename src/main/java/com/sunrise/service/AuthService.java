package com.sunrise.service;

import com.sunrise.dao.UserDAO;
import com.sunrise.model.User;
import com.sunrise.util.PasswordUtil;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service class managing user authentication business logic.
 * Enforces validation, coordinates UserDAO retrieval, and verifies password hashes.
 */
public class AuthService {

    private static final Logger LOGGER = Logger.getLogger(AuthService.class.getName());
    private final UserDAO userDAO;

    public AuthService() {
        this.userDAO = new UserDAO();
    }

    public AuthService(UserDAO userDAO) {
        this.userDAO = userDAO;
    }

    /**
     * Authenticates a user by validating input credentials, retrieving the record from UserDAO,
     * and securely verifying the plaintext password against the stored BCrypt hash.
     *
     * @param username the entered username
     * @param password the entered plaintext password
     * @return the authenticated User object, or null if credentials are invalid or user not found
     */
    public User login(String username, String password) {
        if (username == null || username.trim().isEmpty()) {
            return null;
        }
        if (password == null || password.isEmpty()) {
            return null;
        }

        try {
            User user = userDAO.findByUsername(username.trim());
            if (user == null) {
                LOGGER.log(Level.WARNING, "Authentication failed: Username ''{0}'' not found.", username.trim());
                return null;
            }

            boolean passwordMatches = PasswordUtil.verifyPassword(password, user.getPasswordHash());
            if (passwordMatches) {
                LOGGER.log(Level.INFO, "Authentication successful for user ''{0}'' with role ''{1}''.",
                        new Object[]{user.getUsername(), user.getRole()});
                return user;
            } else {
                LOGGER.log(Level.WARNING, "Authentication failed: Incorrect password for user ''{0}''.", username.trim());
                return null;
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error during authentication for user: " + username.trim(), e);
            return null;
        }
    }
}
