package com.sunrise.util;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

public class PasswordUtilTest {

    @Test
    @DisplayName("Hash password produces a valid BCrypt hash string")
    void testHashPasswordValid() {
        String plain = "admin";
        String hash = PasswordUtil.hashPassword(plain);

        assertNotNull(hash);
        assertTrue(hash.startsWith("$2a$") || hash.startsWith("$2b$") || hash.startsWith("$2y$"));
        assertTrue(PasswordUtil.verifyPassword(plain, hash));
    }

    @Test
    @DisplayName("Verify password returns false for wrong password")
    void testVerifyPasswordWrong() {
        String plain = "admin";
        String hash = PasswordUtil.hashPassword(plain);

        assertFalse(PasswordUtil.verifyPassword("wrongPassword", hash));
    }

    @Test
    @DisplayName("Verify password returns false for null or empty values")
    void testVerifyPasswordNullOrEmpty() {
        assertFalse(PasswordUtil.verifyPassword(null, "$2a$12$somehash"));
        assertFalse(PasswordUtil.verifyPassword("admin", null));
        assertFalse(PasswordUtil.verifyPassword("", "$2a$12$somehash"));
        assertFalse(PasswordUtil.verifyPassword("admin", ""));
        assertFalse(PasswordUtil.verifyPassword("admin", "invalid-hash-format"));
    }

    @Test
    @DisplayName("Hash password throws exception for null or empty input")
    void testHashPasswordThrowsOnEmpty() {
        assertThrows(IllegalArgumentException.class, () -> PasswordUtil.hashPassword(null));
        assertThrows(IllegalArgumentException.class, () -> PasswordUtil.hashPassword(""));
    }
}
