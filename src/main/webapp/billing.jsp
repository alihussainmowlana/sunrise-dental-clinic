<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.sunrise.model.User" %>
<%@ page import="com.sunrise.model.BillDetails" %>
<%@ page import="java.time.format.DateTimeFormatter" %>
<%
    User loggedUser = (User) session.getAttribute("loggedUser");
    if (loggedUser == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    String username = loggedUser.getUsername();
    String role = loggedUser.getRole() != null ? loggedUser.getRole() : "STAFF";

    BillDetails bill = (BillDetails) request.getAttribute("billDetails");
    String errorMessage = (String) request.getAttribute("errorMessage");
    String enteredAppointmentNumber = (String) request.getAttribute("enteredAppointmentNumber");

    DateTimeFormatter timeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm");
    String formattedGeneratedAt = "";
    if (bill != null && bill.getGeneratedAt() != null) {
        formattedGeneratedAt = bill.getGeneratedAt().format(timeFormatter);
    }
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Billing &amp; Receipt - Sunrise Dental Clinic</title>
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
    <main class="form-container">
        <!-- Search & Generate Form Card -->
        <div class="search-card">
            <div class="form-section-header">
                <h2>Calculate &amp; Print Bill</h2>
                <p>Enter the appointment number to compute treatment and consultation charges or view an existing receipt.</p>
            </div>

            <% if (errorMessage != null && !errorMessage.trim().isEmpty()) { %>
                <div class="alert-banner" role="alert">
                    <span><%= errorMessage %></span>
                </div>
            <% } %>

            <form action="${pageContext.request.contextPath}/billing" method="post" autocomplete="off">
                <div class="form-group">
                    <label for="appointmentNumber" class="form-label">Appointment Number <span style="color: var(--danger-color);">*</span></label>
                    <div class="search-input-group">
                        <input type="text"
                               id="appointmentNumber"
                               name="appointmentNumber"
                               class="form-control"
                               placeholder="e.g. APT-FE26B49D"
                               value="<%= enteredAppointmentNumber != null ? enteredAppointmentNumber : "" %>"
                               required
                               autofocus>
                        <button type="submit" class="btn-primary" id="btnGenerateBill" style="width: auto; padding: 0.625rem 1.5rem; white-space: nowrap;">
                            Generate Bill
                        </button>
                    </div>
                </div>

                <div style="margin-top: 1rem;">
                    <a href="${pageContext.request.contextPath}/dashboard" class="btn-secondary" id="btnBackDashboard">
                        Back to Dashboard
                    </a>
                </div>
            </form>
        </div>

        <!-- Bill / Receipt Display Card -->
        <% if (bill != null) { %>
            <div class="receipt-card" id="printableReceipt">
                <!-- Receipt Clinic Header -->
                <div class="receipt-clinic-header">
                    <h2>SUNRISE DENTAL CLINIC</h2>
                    <div class="receipt-subtitle">Official Appointment Bill &amp; Receipt</div>
                </div>

                <!-- Receipt Metadata Grid -->
                <div class="receipt-meta-grid">
                    <div class="receipt-meta-item">
                        <div class="meta-label">Bill Number</div>
                        <div class="meta-value" style="font-family: monospace; color: var(--primary-dark);">
                            <%= bill.getBillNumber() %>
                        </div>
                    </div>
                    <div class="receipt-meta-item">
                        <div class="meta-label">Generated Date &amp; Time</div>
                        <div class="meta-value">
                            <%= formattedGeneratedAt %>
                        </div>
                    </div>
                    <div class="receipt-meta-item">
                        <div class="meta-label">Appointment Reference</div>
                        <div class="meta-value" style="font-family: monospace;">
                            <%= bill.getAppointmentNumber() %>
                        </div>
                    </div>
                    <div class="receipt-meta-item">
                        <div class="meta-label">Appointment Date &amp; Time</div>
                        <div class="meta-value">
                            <%= bill.getAppointmentDate() %> at <%= bill.getAppointmentTime() %>
                        </div>
                    </div>
                </div>

                <!-- Patient & Treatment Details -->
                <div style="display: grid; grid-template-columns: 1fr 1fr; gap: 1.5rem; margin-bottom: 1.5rem;">
                    <div class="details-section" style="background-color: transparent;">
                        <div class="receipt-section-title">Patient Details</div>
                        <div class="info-item">
                            <div class="info-label">Patient Name</div>
                            <div class="info-value"><%= bill.getPatientName() %></div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">Contact Number</div>
                            <div class="info-value"><%= bill.getContactNumber() %></div>
                        </div>
                    </div>

                    <div class="details-section" style="background-color: transparent;">
                        <div class="receipt-section-title">Practitioner Details</div>
                        <div class="info-item">
                            <div class="info-label">Assigned Dentist</div>
                            <div class="info-value"><%= bill.getDentistName() %></div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">Treatment Type</div>
                            <div class="info-value"><%= bill.getTreatmentName() %></div>
                        </div>
                    </div>
                </div>

                <!-- Itemized Charges Table -->
                <div class="receipt-section-title">Itemized Charges</div>
                <table class="receipt-table">
                    <thead>
                        <tr>
                            <th>Description</th>
                            <th style="text-align: right;">Amount (LKR)</th>
                        </tr>
                    </thead>
                    <tbody>
                        <tr>
                            <td>
                                <strong><%= bill.getTreatmentName() %></strong>
                                <div style="font-size: 0.8rem; color: var(--text-muted);">Standard clinical treatment procedure</div>
                            </td>
                            <td class="amount-col">
                                <%= String.format("%,.2f", bill.getTreatmentCost()) %>
                            </td>
                        </tr>
                        <tr>
                            <td>
                                <strong>Consultation Fee</strong>
                                <div style="font-size: 0.8rem; color: var(--text-muted);">Standard practitioner consultation and examination fee</div>
                            </td>
                            <td class="amount-col">
                                <%= String.format("%,.2f", bill.getConsultationFee()) %>
                            </td>
                        </tr>
                        <tr class="total-row">
                            <td>TOTAL AMOUNT DUE</td>
                            <td class="amount-col" style="color: var(--primary-dark);">
                                LKR <%= String.format("%,.2f", bill.getTotalAmount()) %>
                            </td>
                        </tr>
                    </tbody>
                </table>

                <div class="receipt-footer-note">
                    Thank you for choosing Sunrise Dental Clinic. Please retain this receipt for your records.
                </div>

                <!-- Print Action Button (Hidden during print) -->
                <div class="no-print" style="margin-top: 2rem; display: flex; justify-content: flex-end; gap: 1rem;">
                    <button type="button" class="btn-print" id="btnPrint" onclick="window.print()">
                        Print Bill
                    </button>
                </div>
            </div>
        <% } %>
    </main>

    <!-- Page Footer -->
    <footer class="app-footer">
        &copy; <%= java.time.Year.now().getValue() %> Sunrise Dental Clinic Appointment System. Operator: <%= username %> (<%= role %>).
    </footer>
</body>
</html>