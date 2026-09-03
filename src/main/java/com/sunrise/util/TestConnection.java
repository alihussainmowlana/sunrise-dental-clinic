package com.sunrise.util;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

/**
 * Development utility to verify PostgreSQL database connectivity.
 * Note: This is an ad-hoc verification runner, not the automated testing strategy.
 */
public class TestConnection {

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println(" Sunrise Dental Clinic - Database Connection Test");
        System.out.println("=================================================");

        try (Connection conn = DBConnection.getConnection()) {
            if (conn != null && !conn.isClosed()) {
                DatabaseMetaData meta = conn.getMetaData();
                System.out.println("Status      : SUCCESS - Connected to database successfully!");
                System.out.println("Database    : " + meta.getDatabaseProductName() + " " + meta.getDatabaseProductVersion());
                System.out.println("Driver      : " + meta.getDriverName() + " " + meta.getDriverVersion());
                System.out.println("URL         : " + meta.getURL());
                System.out.println("User        : " + meta.getUserName());
            } else {
                System.err.println("Status      : FAILURE - Connection object is null or closed.");
            }
        } catch (SQLException e) {
            System.err.println("Status      : FAILURE - Could not connect to PostgreSQL database.");
            System.err.println("Error Code  : " + e.getErrorCode());
            System.err.println("SQL State   : " + e.getSQLState());
            System.err.println("Message     : " + e.getMessage());
        } catch (Exception e) {
            System.err.println("Status      : UNEXPECTED ERROR - " + e.getMessage());
            e.printStackTrace();
        }
    }
}
