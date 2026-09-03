<%@ page language="java" contentType="text/html; charset=UTF-8" pageEncoding="UTF-8"%>
<!DOCTYPE html>
<html lang="en">
<head>
    <meta charset="UTF-8">
    <meta name="viewport" content="width=device-width, initial-scale=1.0">
    <title>Login - Sunrise Dental Clinic Appointment System</title>
    <link rel="stylesheet" href="${pageContext.request.contextPath}/css/clinic-theme.css">
</head>
<body>
    <div class="login-wrapper">
        <div class="login-card">
            <div class="login-header">
                <div class="clinic-logo">SDC</div>
                <h1>Sunrise Dental Clinic</h1>
                <p>Appointment Management System</p>
            </div>

            <% String errorMessage = (String) request.getAttribute("errorMessage"); %>
            <% if (errorMessage != null && !errorMessage.trim().isEmpty()) { %>
                <div class="alert-banner" role="alert">
                    <span><%= errorMessage %></span>
                </div>
            <% } %>

            <form action="${pageContext.request.contextPath}/login" method="post" autocomplete="off">
                <div class="form-group">
                    <label for="username" class="form-label">Username</label>
                    <input type="text" 
                           id="username" 
                           name="username" 
                           class="form-control" 
                           placeholder="Enter your username" 
                           value="<%= request.getAttribute("enteredUsername") != null ? request.getAttribute("enteredUsername") : "" %>"
                           required 
                           autofocus>
                </div>

                <div class="form-group">
                    <label for="password" class="form-label">Password</label>
                    <input type="password" 
                           id="password" 
                           name="password" 
                           class="form-control" 
                           placeholder="Enter your password" 
                           required>
                </div>

                <button type="submit" class="btn-primary" id="btnLogin">Sign In</button>
            </form>
        </div>
    </div>

    <footer class="app-footer">
        &copy; <%= java.time.Year.now().getValue() %> Sunrise Dental Clinic. Authorized Staff Access Only.
    </footer>
</body>
</html>