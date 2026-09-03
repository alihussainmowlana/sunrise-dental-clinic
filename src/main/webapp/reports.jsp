<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.math.BigDecimal" %>
<%@ page import="com.sunrise.model.User" %>
<%@ page import="com.sunrise.model.AppointmentReportRow" %>
<%@ page import="com.sunrise.model.BillingReportRow" %>
<%
    User loggedUser = (User) session.getAttribute("loggedUser");
    if (loggedUser == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    String username = loggedUser.getUsername();
    String role = loggedUser.getRole() != null ? loggedUser.getRole() : "STAFF";

    String selectedReportType = (String) request.getAttribute("selectedReportType");
    if (selectedReportType == null) {
        selectedReportType = "appointments";
    }

    String selectedReportDate = (String) request.getAttribute("selectedReportDate");
    if (selectedReportDate == null) {
        selectedReportDate = java.time.LocalDate.now().toString();
    }

    String errorMessage = (String) request.getAttribute("errorMessage");
    List<AppointmentReportRow> appointmentRows = (List<AppointmentReportRow>) request.getAttribute("appointmentRows");
    List<BillingReportRow> billingRows = (List<BillingReportRow>) request.getAttribute("billingRows");
    BigDecimal dailyTotal = (BigDecimal) request.getAttribute("dailyTotal");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Operational Reports - Sunrise Dental Clinic</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/clinic-theme.css">
</head>
<body>
    <!-- Top Navigation Header -->
    <header class="header-navbar">
        <div class="brand-container">
            <div class="brand-icon">SDC</div>
            <div>
                <div class="brand-title">Sunrise Dental Clinic</div>
                <div class="brand-subtitle">Appointment Management</div>
            </div>
        </div>

        <div class="user-badge-container">
            <span class="user-greeting">Operator: <strong><%= username %></strong></span>
            <span class="role-badge <%= role %>"><%= role %></span>
            <a href="${pageContext.request.contextPath}/dashboard" class="btn-secondary" style="padding: 0.35rem 0.75rem; font-size: 0.8125rem;">Dashboard</a>
            <a href="${pageContext.request.contextPath}/logout" class="btn-logout-nav" id="navLogout">Logout</a>
        </div>
    </header>

    <!-- Main Content Container -->
    <main class="report-container">
        <!-- Report Selection Form Card -->
        <div class="report-filter-card">
            <div class="form-section-header">
                <h2>Operational Reports</h2>
                <p>Generate daily operational schedules and financial summaries for Sunrise Dental Clinic.</p>
            </div>

            <% if (errorMessage != null && !errorMessage.trim().isEmpty()) { %>
                <div class="alert-banner" role="alert">
                    <span><%= errorMessage %></span>
                </div>
            <% } %>

            <form action="${pageContext.request.contextPath}/reports" method="post" autocomplete="off">
                <div class="report-filter-grid">
                    <div class="form-group" style="margin-bottom: 0;">
                        <label for="reportType" class="form-label">Report Type</label>
                        <select id="reportType" name="reportType" class="form-control" required>
                            <option value="appointments" <%= "appointments".equals(selectedReportType) ? "selected" : "" %>>
                                Daily Appointment Schedule
                            </option>
                            <option value="billing" <%= "billing".equals(selectedReportType) ? "selected" : "" %>>
                                Daily Billing Summary
                            </option>
                        </select>
                    </div>

                    <div class="form-group" style="margin-bottom: 0;">
                        <label for="reportDate" class="form-label">Report Date</label>
                        <input type="date"
                               id="reportDate"
                               name="reportDate"
                               class="form-control"
                               value="<%= selectedReportDate %>"
                               required>
                    </div>

                    <div style="display: flex; gap: 0.75rem;">
                        <button type="submit" class="btn-primary" id="btnGenerateReport" style="padding: 0.625rem 1.25rem;">
                            Generate Report
                        </button>
                        <a href="${pageContext.request.contextPath}/dashboard" class="btn-secondary" id="btnBackDashboard" style="padding: 0.625rem 1.25rem;">
                            Dashboard
                        </a>
                    </div>
                </div>
            </form>
        </div>

        <!-- Report 1: Daily Appointment Schedule -->
        <% if ("appointments".equals(selectedReportType) && appointmentRows != null) { %>
            <div class="report-card" id="printableReport">
                <div class="report-header">
                    <div>
                        <h2>SUNRISE DENTAL CLINIC</h2>
                        <div style="font-weight: 700; color: var(--primary-dark); margin-bottom: 0.25rem;">Daily Appointment Schedule</div>
                        <p>Report Date: <strong><%= selectedReportDate %></strong></p>
                    </div>
                    <div class="no-print">
                        <button type="button" class="btn-print" id="btnPrintReport" onclick="window.print()">
                            Print Report
                        </button>
                    </div>
                </div>

                <% if (appointmentRows.isEmpty()) { %>
                    <div class="empty-report-state">
                        No appointments were found for the selected date.
                    </div>
                <% } else { %>
                    <div class="report-table-wrapper">
                        <table class="report-table">
                            <thead>
                                <tr>
                                    <th>Time</th>
                                    <th>Appointment Number</th>
                                    <th>Patient Name</th>
                                    <th>Contact Number</th>
                                    <th>Dentist</th>
                                    <th>Treatment</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (AppointmentReportRow row : appointmentRows) { %>
                                    <tr>
                                        <td style="font-weight: 700; font-family: monospace;"><%= row.getAppointmentTime() %></td>
                                        <td style="font-family: monospace;"><%= row.getAppointmentNumber() %></td>
                                        <td><%= row.getPatientName() %></td>
                                        <td><%= row.getContactNumber() %></td>
                                        <td><%= row.getDentistName() %></td>
                                        <td><%= row.getTreatmentName() %></td>
                                    </tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>
                    <div style="margin-top: 1.25rem; font-size: 0.875rem; color: var(--text-muted); text-align: right;">
                        Total Scheduled Appointments: <strong><%= appointmentRows.size() %></strong>
                    </div>
                <% } %>
            </div>
        <% } %>

        <!-- Report 2: Daily Billing Summary -->
        <% if ("billing".equals(selectedReportType) && billingRows != null) { %>
            <div class="report-card" id="printableReport">
                <div class="report-header">
                    <div>
                        <h2>SUNRISE DENTAL CLINIC</h2>
                        <div style="font-weight: 700; color: var(--primary-dark); margin-bottom: 0.25rem;">Daily Billing Summary</div>
                        <p>Report Date: <strong><%= selectedReportDate %></strong></p>
                    </div>
                    <div class="no-print">
                        <button type="button" class="btn-print" id="btnPrintReport" onclick="window.print()">
                            Print Report
                        </button>
                    </div>
                </div>

                <% if (billingRows.isEmpty()) { %>
                    <div class="empty-report-state">
                        No bills were found for the selected date.
                    </div>
                <% } else { %>
                    <div class="report-table-wrapper">
                        <table class="report-table">
                            <thead>
                                <tr>
                                    <th>Bill Number</th>
                                    <th>Appointment Number</th>
                                    <th>Patient Name</th>
                                    <th>Treatment</th>
                                    <th style="text-align: right;">Treatment Cost</th>
                                    <th style="text-align: right;">Consultation Fee</th>
                                    <th style="text-align: right;">Total</th>
                                </tr>
                            </thead>
                            <tbody>
                                <% for (BillingReportRow row : billingRows) { %>
                                    <tr>
                                        <td style="font-family: monospace;"><%= row.getBillNumber() %></td>
                                        <td style="font-family: monospace;"><%= row.getAppointmentNumber() %></td>
                                        <td><%= row.getPatientName() %></td>
                                        <td><%= row.getTreatmentName() %></td>
                                        <td class="numeric-col"><%= String.format("%,.2f", row.getTreatmentCost()) %></td>
                                        <td class="numeric-col"><%= String.format("%,.2f", row.getConsultationFee()) %></td>
                                        <td class="numeric-col" style="font-weight: 700;"><%= String.format("%,.2f", row.getTotalAmount()) %></td>
                                    </tr>
                                <% } %>
                            </tbody>
                        </table>
                    </div>

                    <div class="report-summary-bar">
                        <span class="report-summary-label">Daily Total:</span>
                        <span class="report-summary-value">LKR <%= String.format("%,.2f", dailyTotal != null ? dailyTotal : BigDecimal.ZERO) %></span>
                    </div>
                <% } %>
            </div>
        <% } %>
    </main>

    <!-- Page Footer -->
    <footer class="app-footer">
        &copy; <%= java.time.Year.now().getValue() %> Sunrise Dental Clinic Appointment System. Operator: <%= username %> (<%= role %>).
    </footer>
</body>
</html>