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
    <title>Dashboard - Sunrise Dental Clinic Appointment System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/clinic-theme.css">
</head>
<body>
    <header class="header-navbar">
        <div class="brand-container">
            <div class="brand-icon">SDC</div>
            <div>
                <div class="brand-title">Sunrise Dental Clinic</div>
                <div class="brand-subtitle">Appointment System</div>
            </div>
        </div>

        <div class="user-badge-container">
            <span class="user-greeting">Welcome, <strong><%= username %></strong></span>
            <span class="role-badge <%= role %>"><%= role %></span>
            <a href="${pageContext.request.contextPath}/logout" class="btn-logout-nav" id="navLogout">
                <span>Logout</span>
            </a>
        </div>
    </header>

    <main class="dashboard-container">
        <div class="dashboard-hero">
            <div>
                <h2>Sunrise Dental Clinic Appointment System</h2>
                <p>Welcome to the staff management console. Select an action below to proceed.</p>
            </div>
        </div>

        <div class="dashboard-grid">
            <!-- 1. Register Appointment (Active Feature) -->
            <div class="nav-card">
                <div>
                    <div class="nav-card-header">
                        <div class="nav-card-icon">1</div>
                    </div>
                    <h3>Register Appointment</h3>
                    <p>Register new patient details and schedule an appointment with an assigned dentist and treatment.</p>
                </div>
                <a href="${pageContext.request.contextPath}/appointments/register" class="btn-card btn-primary" id="btnRegisterAppt" style="text-decoration: none; text-align: center;">Register Now</a>
            </div>

            <!-- 2. Search Appointment (Active Feature) -->
            <div class="nav-card">
                <div>
                    <div class="nav-card-header">
                        <div class="nav-card-icon">2</div>
                    </div>
                    <h3>Search Appointment</h3>
                    <p>Search and display a registered appointment using its appointment number.</p>
                </div>
                <a href="${pageContext.request.contextPath}/appointments/search" class="btn-card btn-primary" id="btnSearchAppt" style="text-decoration: none; text-align: center;">Search Now</a>
            </div>

            <!-- 3. Calculate Bill (Active Feature) -->
            <div class="nav-card">
                <div>
                    <div class="nav-card-header">
                        <div class="nav-card-icon">3</div>
                    </div>
                    <h3>Calculate Bill</h3>
                    <p>Calculate treatment and consultation charges and print a patient bill.</p>
                </div>
                <a href="${pageContext.request.contextPath}/billing" class="btn-card btn-primary" id="btnCalculateBill" style="text-decoration: none; text-align: center;">Calculate Bill</a>
            </div>

            <!-- 4. Operational Reports (Active Feature) -->
            <div class="nav-card">
                <div>
                    <div class="nav-card-header">
                        <div class="nav-card-icon">4</div>
                    </div>
                    <h3>Reports</h3>
                    <p>Generate daily appointment and billing reports.</p>
                </div>
                <a href="${pageContext.request.contextPath}/reports" class="btn-card btn-primary" id="btnReports" style="text-decoration: none; text-align: center;">View Reports</a>
            </div>

            <!-- 5. Help & Guidelines (Active Feature) -->
            <div class="nav-card">
                <div>
                    <div class="nav-card-header">
                        <div class="nav-card-icon">5</div>
                    </div>
                    <h3>Help &amp; Guidelines</h3>
                    <p>View step-by-step instructions for using the Sunrise Dental Clinic Appointment System.</p>
                </div>
                <a href="${pageContext.request.contextPath}/help" class="btn-card btn-primary" id="btnHelp" style="text-decoration: none; text-align: center;">View Guide</a>
            </div>

            <!-- 6. Logout / Exit (Active Feature) -->
            <div class="nav-card">
                <div>
                    <div class="nav-card-header">
                        <div class="nav-card-icon" style="background-color: #fee2e2; color: #ef4444;">6</div>
                    </div>
                    <h3>Secure Exit / Logout</h3>
                    <p>End your authenticated session safely and return to the clinic login portal.</p>
                </div>
                <a href="${pageContext.request.contextPath}/logout" class="btn-card btn-card-logout" id="btnCardLogout">Logout Now</a>
            </div>
        </div>
    </main>

    <footer class="app-footer">
        &copy; <%= java.time.Year.now().getValue() %> Sunrise Dental Clinic. Authenticated as <%= username %> (<%= role %>).
    </footer>
</body>
</html>