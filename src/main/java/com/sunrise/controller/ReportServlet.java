package com.sunrise.controller;

import com.sunrise.model.AppointmentReportRow;
import com.sunrise.model.BillingReportRow;
import com.sunrise.service.ReportService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller handling operational report requests for Daily Appointment Schedule and Daily Billing Summary.
 */
@WebServlet("/reports")
public class ReportServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(ReportServlet.class.getName());

    private final ReportService reportService;

    public ReportServlet() {
        this.reportService = new ReportService();
    }

    public ReportServlet(ReportService reportService) {
        this.reportService = reportService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        // Set default date to today and default report type
        if (request.getAttribute("selectedReportDate") == null) {
            request.setAttribute("selectedReportDate", LocalDate.now().toString());
        }
        if (request.getAttribute("selectedReportType") == null) {
            request.setAttribute("selectedReportType", "appointments");
        }
        request.getRequestDispatcher("/reports.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String reportType = request.getParameter("reportType");
        String dateStr = request.getParameter("reportDate");

        String selectedType = (reportType != null && !reportType.trim().isEmpty()) ? reportType.trim() : "appointments";
        String selectedDateStr = (dateStr != null && !dateStr.trim().isEmpty()) ? dateStr.trim() : LocalDate.now().toString();

        request.setAttribute("selectedReportType", selectedType);
        request.setAttribute("selectedReportDate", selectedDateStr);

        // 1. Validate Report Type
        if (!"appointments".equals(selectedType) && !"billing".equals(selectedType)) {
            request.setAttribute("errorMessage", "Unsupported report type selected. Please choose Appointments or Billing.");
            request.getRequestDispatcher("/reports.jsp").forward(request, response);
            return;
        }

        // 2. Validate and Parse Report Date
        LocalDate reportDate;
        try {
            reportDate = LocalDate.parse(selectedDateStr);
        } catch (DateTimeParseException e) {
            request.setAttribute("errorMessage", "Please provide a valid report date in YYYY-MM-DD format.");
            request.getRequestDispatcher("/reports.jsp").forward(request, response);
            return;
        }

        // 3. Delegate to ReportService
        try {
            if ("appointments".equals(selectedType)) {
                List<AppointmentReportRow> appointmentRows = reportService.getDailyAppointmentSchedule(reportDate);
                request.setAttribute("appointmentRows", appointmentRows);
            } else {
                List<BillingReportRow> billingRows = reportService.getDailyBillingSummary(reportDate);
                BigDecimal dailyTotal = reportService.calculateDailyBillingTotal(billingRows);
                request.setAttribute("billingRows", billingRows);
                request.setAttribute("dailyTotal", dailyTotal);
            }
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error generating report type '" + selectedType + "' for date: " + reportDate, e);
            request.setAttribute("errorMessage", "A database error occurred while generating the report. Please try again.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error generating report", e);
            request.setAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
        }

        request.getRequestDispatcher("/reports.jsp").forward(request, response);
    }
}