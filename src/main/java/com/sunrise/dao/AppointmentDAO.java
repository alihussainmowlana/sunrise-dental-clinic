package com.sunrise.dao;

import com.sunrise.model.Appointment;
import com.sunrise.model.AppointmentDetails;
import com.sunrise.model.Patient;
import com.sunrise.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Time;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Data Access Object for Appointment and Patient registration transactions and searches.
 * Enforces transaction management, double-booking verification, and joined lookups.
 */
public class AppointmentDAO {

    /**
     * Checks if a dentist is available at the specified date and time slot.
     *
     * @param dentistId       the dentist identifier
     * @param appointmentDate the appointment date
     * @param appointmentTime the appointment time
     * @return true if no conflicting appointment exists (dentist is available), false otherwise
     * @throws SQLException if a database access error occurs
     */
    public boolean isDentistAvailable(int dentistId, LocalDate appointmentDate, LocalTime appointmentTime)
            throws SQLException {
        String sql = "SELECT 1 FROM appointments WHERE dentist_id = ? AND appointment_date = ? AND appointment_time = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, dentistId);
            stmt.setDate(2, Date.valueOf(appointmentDate));
            stmt.setTime(3, Time.valueOf(appointmentTime));

            try (ResultSet rs = stmt.executeQuery()) {
                return !rs.next(); // true if no conflicting record found
            }
        }
    }

    /**
     * Registers a patient and appointment within a single atomic database transaction.
     * If appointment creation fails after patient insertion, the transaction is rolled back.
     *
     * @param patient     the Patient entity to insert
     * @param appointment the Appointment entity to insert
     * @return true if both records were inserted successfully, false otherwise
     * @throws SQLException if a database error or constraint violation occurs
     */
    public boolean registerAppointment(Patient patient, Appointment appointment) throws SQLException {
        if (patient == null || appointment == null) {
            return false;
        }

        String sqlPatient = "INSERT INTO patients (patient_name, address, contact_number) VALUES (?, ?, ?)";
        String sqlAppointment = "INSERT INTO appointments (appointment_number, patient_id, dentist_id, treatment_id, appointment_date, appointment_time) " +
                "VALUES (?, ?, ?, ?, ?, ?)";

        Connection conn = null;
        try {
            conn = DBConnection.getConnection();
            conn.setAutoCommit(false);

            // 1. Insert patient and retrieve generated key
            int generatedPatientId;
            try (PreparedStatement stmtPatient = conn.prepareStatement(sqlPatient, Statement.RETURN_GENERATED_KEYS)) {
                stmtPatient.setString(1, patient.getPatientName().trim());
                stmtPatient.setString(2, patient.getAddress().trim());
                stmtPatient.setString(3, patient.getContactNumber().trim());

                int affectedRows = stmtPatient.executeUpdate();
                if (affectedRows == 0) {
                    conn.rollback();
                    return false;
                }

                try (ResultSet generatedKeys = stmtPatient.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        generatedPatientId = generatedKeys.getInt(1);
                        patient.setPatientId(generatedPatientId);
                        appointment.setPatientId(generatedPatientId);
                    } else {
                        conn.rollback();
                        throw new SQLException("Failed to retrieve generated patient ID. Transaction rolled back.");
                    }
                }
            }

            // 2. Insert appointment with the generated patient ID
            try (PreparedStatement stmtAppt = conn.prepareStatement(sqlAppointment, Statement.RETURN_GENERATED_KEYS)) {
                stmtAppt.setString(1, appointment.getAppointmentNumber());
                stmtAppt.setInt(2, generatedPatientId);
                stmtAppt.setInt(3, appointment.getDentistId());
                stmtAppt.setInt(4, appointment.getTreatmentId());
                stmtAppt.setDate(5, Date.valueOf(appointment.getAppointmentDate()));
                stmtAppt.setTime(6, Time.valueOf(appointment.getAppointmentTime()));

                int affectedRows = stmtAppt.executeUpdate();
                if (affectedRows == 0) {
                    conn.rollback();
                    return false;
                }

                try (ResultSet generatedKeys = stmtAppt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        appointment.setAppointmentId(generatedKeys.getInt(1));
                    }
                }
            }

            // Commit transaction
            conn.commit();
            return true;

        } catch (SQLException e) {
            if (conn != null) {
                try {
                    conn.rollback();
                } catch (SQLException rollbackEx) {
                    e.addSuppressed(rollbackEx);
                }
            }
            throw e;
        } finally {
            if (conn != null) {
                try {
                    conn.setAutoCommit(true);
                    conn.close();
                } catch (SQLException closeEx) {
                    // Suppress close exception if main exception was thrown
                }
            }
        }
    }

    /**
     * Searches for complete appointment and patient details by the unique appointment number.
     * Executes a joined query across appointments, patients, dentists, and treatments tables.
     *
     * @param appointmentNumber the unique appointment number
     * @return the populated AppointmentDetails entity if found, or null otherwise
     * @throws SQLException if a database access error occurs
     */
    public AppointmentDetails findByAppointmentNumber(String appointmentNumber) throws SQLException {
        if (appointmentNumber == null || appointmentNumber.trim().isEmpty()) {
            return null;
        }

        String sql = "SELECT a.appointment_number, p.patient_name, p.address, p.contact_number, " +
                "d.dentist_name, t.treatment_name, t.treatment_cost, " +
                "a.appointment_date, a.appointment_time " +
                "FROM appointments a " +
                "JOIN patients p ON a.patient_id = p.patient_id " +
                "JOIN dentists d ON a.dentist_id = d.dentist_id " +
                "JOIN treatments t ON a.treatment_id = t.treatment_id " +
                "WHERE a.appointment_number = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, appointmentNumber.trim());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    return new AppointmentDetails(
                            rs.getString("appointment_number"),
                            rs.getString("patient_name"),
                            rs.getString("address"),
                            rs.getString("contact_number"),
                            rs.getString("dentist_name"),
                            rs.getString("treatment_name"),
                            rs.getBigDecimal("treatment_cost"),
                            rs.getDate("appointment_date").toLocalDate(),
                            rs.getTime("appointment_time").toLocalTime()
                    );
                }
            }
        }
        return null;
    }
}