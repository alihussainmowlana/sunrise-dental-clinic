package com.sunrise.dao;

import com.sunrise.model.Dentist;
import com.sunrise.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Dentist reference data.
 * Read-only operations for populating appointment booking selections.
 */
public class DentistDAO {

    /**
     * Retrieves all available dentists from the database.
     *
     * @return a List of Dentist entities
     * @throws SQLException if a database access error occurs
     */
    public List<Dentist> findAll() throws SQLException {
        List<Dentist> dentists = new ArrayList<>();
        String sql = "SELECT dentist_id, dentist_name FROM dentists ORDER BY dentist_name ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                dentists.add(new Dentist(
                        rs.getInt("dentist_id"),
                        rs.getString("dentist_name")
                ));
            }
        }
        return dentists;
    }
}