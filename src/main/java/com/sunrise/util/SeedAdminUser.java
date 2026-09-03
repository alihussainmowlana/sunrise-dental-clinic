package com.sunrise.util;

import com.sunrise.dao.UserDAO;
import com.sunrise.model.User;

import java.sql.SQLException;

/**
 * Administrative bootstrap runner to seed the initial system administrator account.
 * Plaintext passwords are not stored; a secure BCrypt hash is generated and persisted.
 */
public class SeedAdminUser {

    private static final String DEFAULT_ADMIN_USERNAME = "admin";
    private static final String DEFAULT_ADMIN_DEV_PASSWORD = "admin";
    private static final String DEFAULT_ADMIN_ROLE = "ADMIN";

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println(" Sunrise Dental Clinic - Admin Seed Utility");
        System.out.println("=================================================");

        String username = (args.length > 0 && !args[0].trim().isEmpty()) ? args[0].trim() : DEFAULT_ADMIN_USERNAME;
        String password = (args.length > 1 && !args[1].trim().isEmpty()) ? args[1] : DEFAULT_ADMIN_DEV_PASSWORD;

        UserDAO userDAO = new UserDAO();

        try {
            User existing = userDAO.findByUsername(username);
            if (existing != null) {
                System.out.println("Status  : Account already exists for username '" + username + "' (ID: " + existing.getUserId() + ", Role: " + existing.getRole() + ").");
                return;
            }

            String passwordHash = PasswordUtil.hashPassword(password);
            User adminUser = new User(username, passwordHash, DEFAULT_ADMIN_ROLE);

            boolean created = userDAO.createUser(adminUser);
            if (created) {
                System.out.println("Status  : SUCCESS - Initial administrator account seeded.");
                System.out.println("Username: " + username);
                System.out.println("Role    : " + DEFAULT_ADMIN_ROLE);
                System.out.println("Security: Password hashed via BCrypt (Plaintext not stored).");
            } else {
                System.err.println("Status  : FAILURE - Failed to insert admin user record.");
            }
        } catch (SQLException e) {
            System.err.println("Status  : DATABASE ERROR - " + e.getMessage());
            e.printStackTrace();
        }
    }
}
