package com.sunrise.service;

import com.sunrise.dao.ReportDAO;
import com.sunrise.model.AppointmentReportRow;
import com.sunrise.model.BillingReportRow;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

/**
 * Service layer coordinating operational reporting logic, date validation, and financial aggregations.
 */
public class ReportService {

    private final ReportDAO reportDAO;

    public ReportService() {
        this.reportDAO = new ReportDAO();
    }

    public ReportService(ReportDAO reportDAO) {
        this.reportDAO = reportDAO;
    }

    /**
     * Retrieves the daily appointment schedule for a specified date.
     *
     * @param date the date for which the schedule is requested
     * @return list of appointment rows ordered by time
     * @throws IllegalArgumentException if date is null
     * @throws SQLException             if a database access error occurs
     */
    public List<AppointmentReportRow> getDailyAppointmentSchedule(LocalDate date) throws SQLException {
        if (date == null) {
            throw new IllegalArgumentException("Report date is required.");
        }
        return reportDAO.findAppointmentsByDate(date);
    }

    /**
     * Retrieves the daily billing summary for a specified date.
     *
     * @param date the date for which billing records are requested
     * @return list of billing rows generated on that date
     * @throws IllegalArgumentException if date is null
     * @throws SQLException             if a database access error occurs
     */
    public List<BillingReportRow> getDailyBillingSummary(LocalDate date) throws SQLException {
        if (date == null) {
            throw new IllegalArgumentException("Report date is required.");
        }
        return reportDAO.findBillsByDate(date);
    }

    /**
     * Computes the grand total sum of all bills in the billing summary using BigDecimal.
     *
     * @param bills the list of billing rows
     * @return total aggregated amount (BigDecimal.ZERO if null or empty)
     */
    public BigDecimal calculateDailyBillingTotal(List<BillingReportRow> bills) {
        if (bills == null || bills.isEmpty()) {
            return BigDecimal.ZERO;
        }

        BigDecimal sum = BigDecimal.ZERO;
        for (BillingReportRow row : bills) {
            if (row != null && row.getTotalAmount() != null) {
                sum = sum.add(row.getTotalAmount());
            }
        }
        return sum;
    }
}