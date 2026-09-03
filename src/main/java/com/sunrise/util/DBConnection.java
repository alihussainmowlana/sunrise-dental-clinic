package com.sunrise.util;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Utility class to manage PostgreSQL database connections for the Sunrise Dental Clinic system.
 * Reads database connection properties from db.properties on the classpath.
 */
public class DBConnection {

    private static final String DEFAULT_DRIVER = "org.postgresql.Driver";
    private static final String DEFAULT_URL = "jdbc:postgresql://localhost:5432/sunrise_dental_db";
    private static final String DEFAULT_USER = "postgres";
    private static final String DEFAULT_PASSWORD = "postgres";

    private static String url;
    private static String username;
    private static String password;

    static {
        loadConfiguration();
    }

    /**
     * Loads database configuration properties from db.properties file.
     */
    private static void loadConfiguration() {
        Properties props = new Properties();
        String driver = DEFAULT_DRIVER;
        url = DEFAULT_URL;
        username = DEFAULT_USER;
        password = DEFAULT_PASSWORD;

        try (InputStream input = DBConnection.class.getClassLoader().getResourceAsStream("db.properties")) {
            if (input != null) {
                props.load(input);
                driver = props.getProperty("db.driver", DEFAULT_DRIVER);
                url = props.getProperty("db.url", DEFAULT_URL);
                username = props.getProperty("db.username", DEFAULT_USER);
                password = props.getProperty("db.password", DEFAULT_PASSWORD);
            } else {
                System.err.println("Warning: db.properties not found on classpath. Using fallback database configuration.");
            }
        } catch (IOException e) {
            System.err.println("Warning: Error reading db.properties (" + e.getMessage() + "). Using fallback database configuration.");
        }

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("PostgreSQL JDBC Driver not found: " + driver, e);
        }
    }

    /**
     * Obtains a new database Connection to the PostgreSQL database.
     *
     * @return a valid java.sql.Connection instance
     * @throws SQLException if a database access error occurs
     */
    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(url, username, password);
    }
}
