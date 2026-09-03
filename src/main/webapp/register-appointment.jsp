<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="java.util.List" %>
<%@ page import="java.time.LocalDate" %>
<%@ page import="com.sunrise.model.User" %>
<%@ page import="com.sunrise.model.Dentist" %>
<%@ page import="com.sunrise.model.Treatment" %>
<%
    User loggedUser = (User) session.getAttribute("loggedUser");
    if (loggedUser == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    String username = loggedUser.getUsername();
    String role = loggedUser.getRole() != null ? loggedUser.getRole() : "STAFF";

    List<Dentist> dentists = (List<Dentist>) request.getAttribute("dentists");
    List<Treatment> treatments = (List<Treatment>) request.getAttribute("treatments");
    String errorMessage = (String) request.getAttribute("errorMessage");
    String successMessage = (String) request.getAttribute("successMessage");
    String successAppointmentNumber = (String) request.getAttribute("successAppointmentNumber");

    String enteredPatientName = (String) request.getAttribute("enteredPatientName");
    String enteredContactNumber = (String) request.getAttribute("enteredContactNumber");
    String enteredAddress = (String) request.getAttribute("enteredAddress");
    String enteredDentistId = (String) request.getAttribute("enteredDentistId");
    String enteredTreatmentId = (String) request.getAttribute("enteredTreatmentId");
    String enteredAppointmentDate = (String) request.getAttribute("enteredAppointmentDate");
    String enteredAppointmentTime = (String) request.getAttribute("enteredAppointmentTime");

    String todayDate = LocalDate.now().toString();
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Register Appointment - Sunrise Dental Clinic</title>
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

    <!-- Main Registration Form -->
    <main class="form-container">
        <div class="form-card">
            <div class="form-section-header">
                <h2>Register New Appointment</h2>
                <p>Enter patient details and select an available practitioner and treatment slot.</p>
            </div>

            <% if (successMessage != null && !successMessage.trim().isEmpty()) { %>
                <div class="alert-banner-success" role="alert">
                    <div class="success-title">
                        <%= successMessage %>
                    </div>
                    <% if (successAppointmentNumber != null) { %>
                        <div>
                            Appointment Number: <span class="appointment-badge"><%= successAppointmentNumber %></span>
                        </div>
                    <% } %>
                </div>
            <% } %>

            <% if (errorMessage != null && !errorMessage.trim().isEmpty()) { %>
                <div class="alert-banner" role="alert">
                    <span><%= errorMessage %></span>
                </div>
            <% } %>

            <form action="${pageContext.request.contextPath}/appointments/register" method="post" autocomplete="off">
                <!-- Section 1: Patient Information -->
                <div class="section-divider">
                    Patient Details
                </div>

                <div class="form-group">
                    <label for="patientName" class="form-label">Patient Full Name <span style="color: var(--danger-color);">*</span></label>
                    <input type="text"
                           id="patientName"
                           name="patientName"
                           class="form-control"
                           placeholder="e.g. Johnathan Silva"
                           value="<%= enteredPatientName != null ? enteredPatientName : "" %>"
                           required
                           autofocus>
                </div>

                <div class="form-row">
                    <div class="form-col">
                        <label for="contactNumber" class="form-label">Contact Number <span style="color: var(--danger-color);">*</span></label>
                        <input type="text"
                               id="contactNumber"
                               name="contactNumber"
                               class="form-control"
                               placeholder="e.g. 077-1234567"
                               value="<%= enteredContactNumber != null ? enteredContactNumber : "" %>"
                               required>
                        <div class="help-text">Primary telephone contact for patient verification.</div>
                    </div>

                    <div class="form-col">
                        <label for="address" class="form-label">Residential Address <span style="color: var(--danger-color);">*</span></label>
                        <input type="text"
                               id="address"
                               name="address"
                               class="form-control"
                               placeholder="e.g. 45 Galle Road, Colombo"
                               value="<%= enteredAddress != null ? enteredAddress : "" %>"
                               required>
                    </div>
                </div>

                <!-- Section 2: Appointment Information -->
                <div class="section-divider">
                    Appointment Details
                </div>

                <div class="form-row">
                    <div class="form-col">
                        <label for="dentistId" class="form-label">Assigned Dentist <span style="color: var(--danger-color);">*</span></label>
                        <select id="dentistId" name="dentistId" class="form-control" required>
                            <option value="">-- Select Dentist --</option>
                            <% if (dentists != null) {
                                for (Dentist d : dentists) {
                                    boolean selected = enteredDentistId != null && enteredDentistId.equals(String.valueOf(d.getDentistId()));
                            %>
                                <option value="<%= d.getDentistId() %>" <%= selected ? "selected" : "" %>>
                                    <%= d.getDentistName() %>
                                </option>
                            <%  }
                               } %>
                        </select>
                    </div>

                    <div class="form-col">
                        <label for="treatmentId" class="form-label">Treatment Type <span style="color: var(--danger-color);">*</span></label>
                        <select id="treatmentId" name="treatmentId" class="form-control" required>
                            <option value="">-- Select Treatment --</option>
                            <% if (treatments != null) {
                                for (Treatment t : treatments) {
                                    boolean selected = enteredTreatmentId != null && enteredTreatmentId.equals(String.valueOf(t.getTreatmentId()));
                            %>
                                <option value="<%= t.getTreatmentId() %>" <%= selected ? "selected" : "" %>>
                                    <%= t.getTreatmentName() %> (LKR <%= t.getTreatmentCost() %>)
                                </option>
                            <%  }
                               } %>
                        </select>
                    </div>
                </div>

                <div class="form-row">
                    <div class="form-col">
                        <label for="appointmentDate" class="form-label">Appointment Date <span style="color: var(--danger-color);">*</span></label>
                        <input type="date"
                               id="appointmentDate"
                               name="appointmentDate"
                               class="form-control"
                               min="<%= todayDate %>"
                               value="<%= enteredAppointmentDate != null ? enteredAppointmentDate : "" %>"
                               required>
                    </div>

                    <div class="form-col">
                        <label for="appointmentTime" class="form-label">Appointment Time <span style="color: var(--danger-color);">*</span></label>
                        <input type="time"
                               id="appointmentTime"
                               name="appointmentTime"
                               class="form-control"
                               step="900"
                               value="<%= enteredAppointmentTime != null ? enteredAppointmentTime : "" %>"
                               required>
                    </div>
                </div>

                <!-- Form Action Buttons -->
                <div class="form-actions">
                    <a href="${pageContext.request.contextPath}/dashboard" class="btn-secondary" id="btnCancel">Back to Dashboard</a>
                    <button type="submit" class="btn-submit" id="btnSubmitAppointment">Register Appointment</button>
                </div>
            </form>
        </div>
    </main>

    <!-- Page Footer -->
    <footer class="app-footer">
        &copy; <%= java.time.Year.now().getValue() %> Sunrise Dental Clinic Appointment System. Operator: <%= username %> (<%= role %>).
    </footer>
</body>
</html>