package com.sunrise.util;

import org.mindrot.jbcrypt.BCrypt;

/**
 * Utility class for hashing and verifying passwords using BCrypt.
 * Ensures plaintext passwords are never stored or compared in plain text.
 */
public class PasswordUtil {

    private static final int BCRYPT_LOG_ROUNDS = 12;

    private PasswordUtil() {
        // Private constructor to prevent instantiation
    }

    /**
     * Hashes a plaintext password using BCrypt with a secure salt.
     *
     * @param plainPassword the plaintext password to hash
     * @return the BCrypt hashed password string, or null if input is null
     */
    public static String hashPassword(String plainPassword) {
        if (plainPassword == null || plainPassword.isEmpty()) {
            throw new IllegalArgumentException("Password cannot be null or empty.");
        }
        return BCrypt.hashpw(plainPassword, BCrypt.gensalt(BCRYPT_LOG_ROUNDS));
    }

    /**
     * Verifies a plaintext password against a stored BCrypt password hash.
     *
     * @param plainPassword the plaintext password to verify
     * @param passwordHash  the stored BCrypt hash from the database
     * @return true if password matches hash, false otherwise
     */
    public static boolean verifyPassword(String plainPassword, String passwordHash) {
        if (plainPassword == null || passwordHash == null || plainPassword.isEmpty() || passwordHash.isEmpty()) {
            return false;
        }
        try {
            return BCrypt.checkpw(plainPassword, passwordHash);
        } catch (IllegalArgumentException e) {
            // Handles malformed hash format gracefully
            return false;
        }
    }
}
