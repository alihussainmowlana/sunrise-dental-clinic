package com.sunrise.dao;

import com.sunrise.model.AppointmentBillingInfo;
import com.sunrise.model.Bill;
import com.sunrise.model.BillDetails;
import com.sunrise.util.DBConnection;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;

/**
 * Data Access Object for Bill entities and billing-related queries.
 */
public class BillDAO {

    /**
     * Retrieves an existing bill and full receipt details for an appointment.
     *
     * @param appointmentNumber the unique appointment number
     * @return populated BillDetails if the appointment was already billed, or null otherwise
     * @throws SQLException if a database access error occurs
     */
    public BillDetails findByAppointmentNumber(String appointmentNumber) throws SQLException {
        if (appointmentNumber == null || appointmentNumber.trim().isEmpty()) {
            return null;
        }

        String sql = "SELECT b.bill_number, a.appointment_number, p.patient_name, p.contact_number, " +
                "d.dentist_name, t.treatment_name, a.appointment_date, a.appointment_time, " +
                "b.consultation_fee, b.treatment_cost, b.total_amount, b.generated_at " +
                "FROM bills b " +
                "JOIN appointments a ON b.appointment_id = a.appointment_id " +
                "JOIN patients p ON a.patient_id = p.patient_id " +
                "JOIN dentists d ON a.dentist_id = d.dentist_id " +
                "JOIN treatments t ON a.treatment_id = t.treatment_id " +
                "WHERE a.appointment_number = ?";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, appointmentNumber.trim());

            try (ResultSet rs = stmt.executeQuery()) {
                if (rs.next()) {
                    Timestamp ts = rs.getTimestamp("generated_at");
                    return new BillDetails(
                            rs.getString("bill_number"),
                            rs.getString("appointment_number"),
                            rs.getString("patient_name"),
                            rs.getString("contact_number"),
                            rs.getString("dentist_name"),
                            rs.getString("treatment_name"),
                            rs.getDate("appointment_date").toLocalDate(),
                            rs.getTime("appointment_time").toLocalTime(),
                            rs.getBigDecimal("consultation_fee"),
                            rs.getBigDecimal("treatment_cost"),
                            rs.getBigDecimal("total_amount"),
                            ts != null ? ts.toLocalDateTime() : null
                    );
                }
            }
        }
        return null;
    }

    /**
     * Retrieves appointment and patient details along with standard treatment cost
     * required to compute and generate a new bill.
     *
     * @param appointmentNumber the unique appointment number
     * @return AppointmentBillingInfo if found, or null otherwise
     * @throws SQLException if a database access error occurs
     */
    public AppointmentBillingInfo findAppointmentForBilling(String appointmentNumber) throws SQLException {
        if (appointmentNumber == null || appointmentNumber.trim().isEmpty()) {
            return null;
        }

        String sql = "SELECT a.appointment_id, a.appointment_number, p.patient_name, p.contact_number, " +
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
                    return new AppointmentBillingInfo(
                            rs.getInt("appointment_id"),
                            rs.getString("appointment_number"),
                            rs.getString("patient_name"),
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

    /**
     * Inserts a new bill record into the bills table.
     *
     * @param bill the Bill entity containing bill details
     * @return true if insertion succeeded, false otherwise
     * @throws SQLException if a database error or UNIQUE constraint violation occurs
     */
    public boolean createBill(Bill bill) throws SQLException {
        if (bill == null) {
            return false;
        }

        String sql = "INSERT INTO bills (bill_number, appointment_id, consultation_fee, treatment_cost, total_amount) " +
                "VALUES (?, ?, ?, ?, ?)";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {

            stmt.setString(1, bill.getBillNumber());
            stmt.setInt(2, bill.getAppointmentId());
            stmt.setBigDecimal(3, bill.getConsultationFee());
            stmt.setBigDecimal(4, bill.getTreatmentCost());
            stmt.setBigDecimal(5, bill.getTotalAmount());

            int affectedRows = stmt.executeUpdate();
            if (affectedRows > 0) {
                try (ResultSet rs = stmt.getGeneratedKeys()) {
                    if (rs.next()) {
                        bill.setBillId(rs.getInt(1));
                    }
                }
                return true;
            }
        }
        return false;
    }
}