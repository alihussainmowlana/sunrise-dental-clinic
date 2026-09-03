<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.sunrise.model.User" %>
<%@ page import="com.sunrise.model.AppointmentDetails" %>
<%
    User loggedUser = (User) session.getAttribute("loggedUser");
    if (loggedUser == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    String username = loggedUser.getUsername();
    String role = loggedUser.getRole() != null ? loggedUser.getRole() : "STAFF";

    AppointmentDetails details = (AppointmentDetails) request.getAttribute("appointmentDetails");
    String errorMessage = (String) request.getAttribute("errorMessage");
    String enteredAppointmentNumber = (String) request.getAttribute("enteredAppointmentNumber");
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Search Appointment - Sunrise Dental Clinic</title>
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
        <!-- Search Form Card -->
        <div class="search-card">
            <div class="form-section-header">
                <h2>Search Appointment</h2>
                <p>Enter a unique appointment number to retrieve complete patient and appointment details.</p>
            </div>

            <% if (errorMessage != null && !errorMessage.trim().isEmpty()) { %>
                <div class="alert-banner" role="alert">
                    <span><%= errorMessage %></span>
                </div>
            <% } %>

            <form action="${pageContext.request.contextPath}/appointments/search" method="post" autocomplete="off">
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
                        <button type="submit" class="btn-primary" id="btnSearch" style="width: auto; padding: 0.625rem 1.5rem; white-space: nowrap;">
                            Search Appointment
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

        <!-- Appointment Details Result Display -->
        <% if (details != null) { %>
            <div class="details-card">
                <div class="details-header">
                    <div class="details-title-group">
                        <h3>Appointment Information</h3>
                        <p>Showing verified record from clinic database</p>
                    </div>
                    <div>
                        <span class="appointment-badge" style="font-size: 1.1rem; padding: 0.35rem 0.75rem;">
                            <%= details.getAppointmentNumber() %>
                        </span>
                    </div>
                </div>

                <div class="details-grid">
                    <!-- Patient Details Section -->
                    <div class="details-section">
                        <div class="details-section-title">
                            Patient Details
                        </div>
                        <div class="info-item">
                            <div class="info-label">Patient Name</div>
                            <div class="info-value"><%= details.getPatientName() %></div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">Contact Number</div>
                            <div class="info-value"><%= details.getContactNumber() %></div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">Residential Address</div>
                            <div class="info-value"><%= details.getAddress() %></div>
                        </div>
                    </div>

                    <!-- Appointment Details Section -->
                    <div class="details-section">
                        <div class="details-section-title">
                            Appointment Details
                        </div>
                        <div class="info-item">
                            <div class="info-label">Assigned Dentist</div>
                            <div class="info-value info-value-highlight"><%= details.getDentistName() %></div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">Treatment Type</div>
                            <div class="info-value"><%= details.getTreatmentName() %></div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">Treatment Standard Cost</div>
                            <div class="info-value">
                                <span class="badge-cost">LKR <%= details.getTreatmentCost() %></span>
                            </div>
                        </div>
                        <div class="info-item">
                            <div class="info-label">Scheduled Date &amp; Time</div>
                            <div class="info-value">
                                <%= details.getAppointmentDate() %> at <%= details.getAppointmentTime() %>
                            </div>
                        </div>
                    </div>
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