package com.sunrise.dao;

import com.sunrise.model.AppointmentReportRow;
import com.sunrise.model.BillingReportRow;
import com.sunrise.util.DBConnection;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

/**
 * Data Access Object for operational reports (Daily Appointment Schedule and Daily Billing Summary).
 */
public class ReportDAO {

    /**
     * Retrieves all appointments scheduled for a given date, ordered by appointment time ascending.
     *
     * @param date the appointment date to filter by
     * @return list of AppointmentReportRow objects (empty if no appointments found)
     * @throws SQLException if a database access error occurs
     */
    public List<AppointmentReportRow> findAppointmentsByDate(LocalDate date) throws SQLException {
        List<AppointmentReportRow> rows = new ArrayList<>();
        if (date == null) {
            return rows;
        }

        String sql = "SELECT a.appointment_number, p.patient_name, p.contact_number, " +
                "d.dentist_name, t.treatment_name, a.appointment_time " +
                "FROM appointments a " +
                "JOIN patients p ON a.patient_id = p.patient_id " +
                "JOIN dentists d ON a.dentist_id = d.dentist_id " +
                "JOIN treatments t ON a.treatment_id = t.treatment_id " +
                "WHERE a.appointment_date = ? " +
                "ORDER BY a.appointment_time ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(date));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    rows.add(new AppointmentReportRow(
                            rs.getString("appointment_number"),
                            rs.getString("patient_name"),
                            rs.getString("contact_number"),
                            rs.getString("dentist_name"),
                            rs.getString("treatment_name"),
                            rs.getTime("appointment_time").toLocalTime()
                    ));
                }
            }
        }
        return rows;
    }

    /**
     * Retrieves all bills generated on a given calendar date, ordered by generation timestamp ascending.
     *
     * @param date the date on which bills were generated
     * @return list of BillingReportRow objects (empty if no bills found)
     * @throws SQLException if a database access error occurs
     */
    public List<BillingReportRow> findBillsByDate(LocalDate date) throws SQLException {
        List<BillingReportRow> rows = new ArrayList<>();
        if (date == null) {
            return rows;
        }

        String sql = "SELECT b.bill_number, a.appointment_number, p.patient_name, " +
                "t.treatment_name, b.treatment_cost, b.consultation_fee, " +
                "b.total_amount, b.generated_at " +
                "FROM bills b " +
                "JOIN appointments a ON b.appointment_id = a.appointment_id " +
                "JOIN patients p ON a.patient_id = p.patient_id " +
                "JOIN treatments t ON a.treatment_id = t.treatment_id " +
                "WHERE DATE(b.generated_at) = ? " +
                "ORDER BY b.generated_at ASC";

        try (Connection conn = DBConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setDate(1, Date.valueOf(date));

            try (ResultSet rs = stmt.executeQuery()) {
                while (rs.next()) {
                    Timestamp ts = rs.getTimestamp("generated_at");
                    rows.add(new BillingReportRow(
                            rs.getString("bill_number"),
                            rs.getString("appointment_number"),
                            rs.getString("patient_name"),
                            rs.getString("treatment_name"),
                            rs.getBigDecimal("treatment_cost"),
                            rs.getBigDecimal("consultation_fee"),
                            rs.getBigDecimal("total_amount"),
                            ts != null ? ts.toLocalDateTime() : null
                    ));
                }
            }
        }
        return rows;
    }
}