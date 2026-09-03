# Sunrise Dental Clinic Appointment System

A Java/Jakarta EE web application designed to manage patient appointments, dental treatments, billing, and operational reporting for Sunrise Dental Clinic.

## Overview

The Sunrise Dental Clinic Appointment System is a structured, layered web application built with Jakarta EE and PostgreSQL, following standard enterprise design patterns (MVC, DAO, Service Layer) to provide a reliable clinic management platform.

## Development Status

- Project foundation completed (Java 17, Jakarta EE 10, Maven build setup)
- PostgreSQL database schema designed (6 core tables)
- Seed data scripts prepared
- JDBC connection utility and externalized configuration created
- Authentication module implemented (BCrypt password hashing, UserDAO, AuthService, Login/Logout servlets, AuthFilter, Dashboard)
- Appointment registration implemented
- Double-booking prevention implemented
- Appointment search/display implemented
- Billing calculation implemented
- Printable receipt implemented
- Help section implemented
- All required core application features implemented
- REST appointment web service implemented
- Daily appointment report implemented
- Daily billing report implemented

## Technology Stack

- **Platform:** Java SE 17
- **Enterprise Standard:** Jakarta EE 10 (Servlet API 6.0, JAX-RS 3.1)
- **Database:** PostgreSQL (Driver 42.7.3)
- **Security:** BCrypt Hashing (`org.mindrot:jbcrypt:0.4`)
- **Data Access:** Standard JDBC with `PreparedStatement` & externalized `db.properties`
- **Build Tool:** Apache Maven
- **Testing:** JUnit 5 (jupiter 5.10.2)

## Database

- **Database Name:** `sunrise_dental_db`
- **Target Engine:** PostgreSQL

### Core Database Tables

1. **`users`**: System operators (Admin, Receptionist) authorized for system access.
2. **`patients`**: Patient personal and contact records for appointment scheduling.
3. **`dentists`**: Dental practitioners available for appointment assignment.
4. **`treatments`**: Standard dental treatment catalog with standardized fees.
5. **`appointments`**: Core transactional entity linking patient, dentist, treatment, date, and time slot.
6. **`bills`**: Invoices generated for scheduled/completed appointments with consultation and treatment totals.

### Core Entity Relationships

- **Patient (1) $\rightarrow$ Appointment (0..*)**: A registered patient can book multiple appointments over time.
- **Dentist (1) $\rightarrow$ Appointment (0..*)**: A dentist is assigned to multiple non-overlapping appointment slots.
- **Treatment (1) $\rightarrow$ Appointment (0..*)**: A treatment procedure is selected for each appointment.
- **Appointment (1) $\rightarrow$ Bill (0..1)**: An appointment generates at most one invoice record.

## REST API

The system provides a read-only RESTful web service for distributed application integration.

- **Endpoint:** `GET /api/appointments/{appointmentNumber}`
- **Example:** `GET /api/appointments/APT-XXXXXXXX`
- **Description:** Returns complete appointment and patient details formatted as JSON for authenticated clinic staff sessions. Unauthenticated requests receive HTTP `401 Unauthorized`.

## Project Structure

```
sunrise-dental-clinic/
├── pom.xml
├── README.md
├── database/
│   ├── schema.sql
│   └── seed.sql
├── docs/
│   ├── database-design.md
│   ├── authentication-design.md
│   ├── appointment-registration-design.md
│   ├── appointment-search-design.md
│   ├── billing-design.md
│   ├── help-design.md
│   ├── web-service-design.md
│   └── report-design.md
└── src/
    ├── main/
    │   ├── java/
    │   │   └── com/
    │   │       └── sunrise/
    │   │           ├── api/
    │   │           │   ├── AppointmentResource.java
    │   │           │   └── RestApplication.java
    │   │           ├── controller/
    │   │           │   ├── AuthFilter.java
    │   │           │   ├── BillingServlet.java
    │   │           │   ├── DashboardServlet.java
    │   │           │   ├── HelpServlet.java
    │   │           │   ├── LoginServlet.java
    │   │           │   ├── LogoutServlet.java
    │   │           │   ├── RegisterAppointmentServlet.java
    │   │           │   ├── ReportServlet.java
    │   │           │   └── SearchAppointmentServlet.java
    │   │           ├── dao/
    │   │           │   ├── AppointmentDAO.java
    │   │           │   ├── BillDAO.java
    │   │           │   ├── DentistDAO.java
    │   │           │   ├── ReportDAO.java
    │   │           │   ├── TreatmentDAO.java
    │   │           │   └── UserDAO.java
    │   │           ├── model/
    │   │           │   ├── Appointment.java
    │   │           │   ├── AppointmentBillingInfo.java
    │   │           │   ├── AppointmentDetails.java
    │   │           │   ├── AppointmentReportRow.java
    │   │           │   ├── Bill.java
    │   │           │   ├── BillDetails.java
    │   │           │   ├── BillingReportRow.java
    │   │           │   ├── Dentist.java
    │   │           │   ├── Patient.java
    │   │           │   ├── Treatment.java
    │   │           │   └── User.java
    │   │           ├── service/
    │   │           │   ├── AppointmentService.java
    │   │           │   ├── AuthService.java
    │   │           │   ├── BillingService.java
    │   │           │   └── ReportService.java
    │   │           └── util/
    │   │               ├── DBConnection.java
    │   │               ├── PasswordUtil.java
    │   │               ├── SeedAdminUser.java
    │   │               └── TestConnection.java
    │   ├── resources/
    │   │   ├── db.properties
    │   │   └── db.properties.example
    │   └── webapp/
    │       ├── css/
    │       │   └── clinic-theme.css
    │       ├── billing.jsp
    │       ├── dashboard.jsp
    │       ├── help.jsp
    │       ├── login.jsp
    │       ├── register-appointment.jsp
    │       ├── reports.jsp
    │       └── search-appointment.jsp
    └── test/
        └── java/
            └── com/
                └── sunrise/
                    ├── service/
                    │   ├── AppointmentServiceTest.java
                    │   ├── AuthServiceTest.java
                    │   ├── BillingServiceTest.java
                    │   └── ReportServiceTest.java
                    └── util/
                        └── PasswordUtilTest.java
```