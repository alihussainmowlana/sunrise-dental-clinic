package com.sunrise.service;

import com.sunrise.dao.UserDAO;
import com.sunrise.model.User;
import com.sunrise.util.PasswordUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;

public class AuthServiceTest {

    private AuthService authService;

    // Test Stub for UserDAO
    static class StubUserDAO extends UserDAO {
        @Override
        public User findByUsername(String username) throws SQLException {
            if ("admin".equals(username)) {
                return new User(1, "admin", PasswordUtil.hashPassword("admin"), "ADMIN");
            }
            if ("receptionist".equals(username)) {
                return new User(2, "receptionist", PasswordUtil.hashPassword("rec123"), "RECEPTIONIST");
            }
            return null;
        }
    }

    @BeforeEach
    void setUp() {
        authService = new AuthService(new StubUserDAO());
    }

    @Test
    @DisplayName("Login succeeds with valid admin credentials")
    void testLoginSuccess() {
        User user = authService.login("admin", "admin");
        assertNotNull(user);
        assertEquals("admin", user.getUsername());
        assertEquals("ADMIN", user.getRole());
    }

    @Test
    @DisplayName("Login fails with incorrect password")
    void testLoginIncorrectPassword() {
        User user = authService.login("admin", "wrongpass");
        assertNull(user);
    }

    @Test
    @DisplayName("Login fails with non-existent username")
    void testLoginNonExistentUser() {
        User user = authService.login("nonexistent", "somepass");
        assertNull(user);
    }

    @Test
    @DisplayName("Login fails with null or empty credentials")
    void testLoginNullOrEmpty() {
        assertNull(authService.login(null, "admin"));
        assertNull(authService.login("admin", null));
        assertNull(authService.login("", "admin"));
        assertNull(authService.login("admin", ""));
        assertNull(authService.login("   ", "admin"));
    }
}
