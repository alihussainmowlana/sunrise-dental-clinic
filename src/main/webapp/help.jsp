<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<%@ page import="com.sunrise.model.User" %>
<%
    User loggedUser = (User) session.getAttribute("loggedUser");
    if (loggedUser == null) {
        response.sendRedirect(request.getContextPath() + "/login");
        return;
    }
    String username = loggedUser.getUsername();
    String role = loggedUser.getRole() != null ? loggedUser.getRole() : "STAFF";
%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Help &amp; System Guide - Sunrise Dental Clinic</title>
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
    <main class="help-container">
        <!-- Hero Header -->
        <div class="help-hero">
            <div>
                <h2>Help &amp; System Guide</h2>
                <p>Comprehensive step-by-step operating instructions for authorized clinic staff.</p>
            </div>
            <div>
                <a href="${pageContext.request.contextPath}/dashboard" class="btn-secondary" id="btnBackDashboard">
                    Back to Dashboard
                </a>
            </div>
        </div>

        <!-- Section 1: Logging In -->
        <div class="help-section-card">
            <div class="help-section-header">
                <span class="help-badge-number">1</span>
                <div class="help-section-title">Logging In to the System</div>
            </div>
            <ol class="help-steps">
                <li>Open the Sunrise Dental Clinic Appointment System in your web browser.</li>
                <li>Enter your authorized staff username in the Username field.</li>
                <li>Enter your secure password in the Password field.</li>
                <li>Select the <strong>Sign In</strong> button to authenticate.</li>
                <li>Upon successful authentication, the main management Dashboard appears.</li>
            </ol>
            <div class="help-callout">
                <strong>Access Note:</strong> Only authorized clinic staff members may access protected system functions. Unauthenticated requests are automatically redirected to the login portal.
            </div>
        </div>

        <!-- Section 2: Registering a New Appointment -->
        <div class="help-section-card">
            <div class="help-section-header">
                <span class="help-badge-number">2</span>
                <div class="help-section-title">Registering a New Appointment</div>
            </div>
            <ol class="help-steps">
                <li>From the Dashboard, select <strong>Register Appointment</strong>.</li>
                <li>Enter the patient's full name in the Patient Name field.</li>
                <li>Enter the patient's residential address in the Address field.</li>
                <li>Enter the patient's telephone contact number in the Contact Number field.</li>
                <li>Select an assigned practitioner from the Dentist dropdown list.</li>
                <li>Select the required clinical treatment procedure from the Treatment dropdown list.</li>
                <li>Select the scheduled date from the Appointment Date field (cannot be in the past).</li>
                <li>Select the scheduled time from the Appointment Time field. Appointment times are scheduled in 15-minute intervals.</li>
                <li>Select the <strong>Register Appointment</strong> button to confirm the booking.</li>
                <li>Record the generated appointment number (e.g. <code>APT-XXXXXXXX</code>) displayed on the confirmation badge.</li>
            </ol>
            <div class="help-callout">
                <strong>Automatic Numbering &amp; Interval:</strong> The appointment reference number is generated automatically by the system upon registration. Appointment times are scheduled in 15-minute intervals.
            </div>
            <div class="help-callout-warning">
                <strong>Double-Booking Prevention:</strong> If the selected dentist already has an appointment scheduled at that exact date and time, the system will reject the booking. The staff member must select an alternate date or time slot.
            </div>
        </div>

        <!-- Section 3: Searching for an Appointment -->
        <div class="help-section-card">
            <div class="help-section-header">
                <span class="help-badge-number">3</span>
                <div class="help-section-title">Searching for an Appointment</div>
            </div>
            <ol class="help-steps">
                <li>From the Dashboard, select <strong>Search Appointment</strong>.</li>
                <li>Enter the unique appointment number (e.g. <code>APT-FE26B49D</code>) in the search field.</li>
                <li>Select the <strong>Search Appointment</strong> button.</li>
                <li>Review the complete patient details (name, contact, address) and appointment details (dentist, treatment, scheduled date and time).</li>
            </ol>
            <div class="help-callout">
                <strong>Lookup Rule:</strong> Appointment search is performed strictly using the unique appointment number.
            </div>
        </div>

        <!-- Section 4: Calculating a Bill -->
        <div class="help-section-card">
            <div class="help-section-header">
                <span class="help-badge-number">4</span>
                <div class="help-section-title">Calculating a Bill</div>
            </div>
            <ol class="help-steps">
                <li>From the Dashboard, select <strong>Calculate Bill</strong>.</li>
                <li>Enter the unique appointment number for the completed or scheduled visit.</li>
                <li>Select the <strong>Generate Bill</strong> button.</li>
                <li>Review the itemized financial breakdown:
                    <ul style="margin-top: 0.5rem; padding-left: 1.5rem;">
                        <li><strong>Treatment Cost:</strong> The standard fee corresponding to the procedure.</li>
                        <li><strong>Consultation Fee:</strong> The fixed clinic consultation fee of LKR 1,000.00.</li>
                        <li><strong>Total Amount:</strong> The combined total payable by the patient.</li>
                    </ul>
                </li>
            </ol>
            <div class="help-callout">
                <strong>Billing Formula:</strong> Total Amount = Treatment Cost + Consultation Fee. If a bill has already been calculated for the appointment, the existing bill is displayed safely without duplicate generation.
            </div>
        </div>

        <!-- Section 5: Printing a Bill -->
        <div class="help-section-card">
            <div class="help-section-header">
                <span class="help-badge-number">5</span>
                <div class="help-section-title">Printing a Bill / Receipt</div>
            </div>
            <ol class="help-steps">
                <li>Generate or retrieve the bill from the Calculate Bill screen.</li>
                <li>Select the <strong>Print Bill</strong> button located below the charges breakdown.</li>
                <li>The native browser print dialog will appear automatically.</li>
                <li>Select your connected clinic receipt printer or preferred print destination.</li>
                <li>Select <strong>Print</strong> to produce the paper receipt for the patient.</li>
            </ol>
            <div class="help-callout">
                <strong>Print Layout:</strong> The print stylesheet automatically hides application navigation bars, search inputs, and buttons, ensuring a clean and professional paper receipt.
            </div>
        </div>

        <!-- Section 6: Logging Out / Exiting Safely -->
        <div class="help-section-card">
            <div class="help-section-header">
                <span class="help-badge-number">6</span>
                <div class="help-section-title">Logging Out / Exiting Safely</div>
            </div>
            <ol class="help-steps">
                <li>Select <strong>Logout</strong> from the top navigation bar or the <strong>Secure Exit / Logout</strong> card on the Dashboard.</li>
                <li>The system immediately invalidates your authenticated session.</li>
                <li>You are safely redirected to the Login page.</li>
            </ol>
            <div class="help-callout">
                <strong>Security Practice:</strong> Logging out is the application's safe exit mechanism. Always log out when stepping away from the clinic workstation to protect patient data.
            </div>
        </div>

        <div style="text-align: center; margin: 2rem 0;">
            <a href="${pageContext.request.contextPath}/dashboard" class="btn-secondary" style="padding: 0.625rem 1.75rem;">
                Back to Dashboard
            </a>
        </div>
    </main>

    <!-- Page Footer -->
    <footer class="app-footer">
        &copy; <%= java.time.Year.now().getValue() %> Sunrise Dental Clinic Appointment System. Operator: <%= username %> (<%= role %>).
    </footer>
</body>
</html>