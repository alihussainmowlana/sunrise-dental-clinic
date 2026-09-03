package com.sunrise.dao;

import com.sunrise.model.Treatment;
import com.sunrise.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for Treatment reference data.
 * Read-only operations for populating appointment booking selections.
 */
public class TreatmentDAO {

    /**
     * Retrieves all available dental treatments and their standard costs.
     *
     * @return a List of Treatment entities
     * @throws SQLException if a database access error occurs
     */
    public List<Treatment> findAll() throws SQLException {
        List<Treatment> treatments = new ArrayList<>();
        String sql = "SELECT treatment_id, treatment_name, treatment_cost FROM treatments ORDER BY treatment_name ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                treatments.add(new Treatment(
                        rs.getInt("treatment_id"),
                        rs.getString("treatment_name"),
                        rs.getBigDecimal("treatment_cost")
                ));
            }
        }
        return treatments;
    }
}