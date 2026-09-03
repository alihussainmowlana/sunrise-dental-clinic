package com.sunrise.controller;

import com.sunrise.dao.DentistDAO;
import com.sunrise.dao.TreatmentDAO;
import com.sunrise.model.Dentist;
import com.sunrise.model.Treatment;
import com.sunrise.service.AppointmentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Servlet handling registration of new patient dental appointments.
 * Dispatches form data to AppointmentService and presents confirmation or validation feedback.
 */
@WebServlet("/appointments/register")
public class RegisterAppointmentServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(RegisterAppointmentServlet.class.getName());

    private final AppointmentService appointmentService;
    private final DentistDAO dentistDAO;
    private final TreatmentDAO treatmentDAO;

    public RegisterAppointmentServlet() {
        this.appointmentService = new AppointmentService();
        this.dentistDAO = new DentistDAO();
        this.treatmentDAO = new TreatmentDAO();
    }

    public RegisterAppointmentServlet(AppointmentService appointmentService, DentistDAO dentistDAO, TreatmentDAO treatmentDAO) {
        this.appointmentService = appointmentService;
        this.dentistDAO = dentistDAO;
        this.treatmentDAO = treatmentDAO;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        loadDropdownReferenceData(request);
        request.getRequestDispatcher("/register-appointment.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String patientName = request.getParameter("patientName");
        String address = request.getParameter("address");
        String contactNumber = request.getParameter("contactNumber");
        String dentistIdStr = request.getParameter("dentistId");
        String treatmentIdStr = request.getParameter("treatmentId");
        String dateStr = request.getParameter("appointmentDate");
        String timeStr = request.getParameter("appointmentTime");

        // Retain entered parameters in request scope for user convenience on error
        request.setAttribute("enteredPatientName", patientName != null ? patientName.trim() : "");
        request.setAttribute("enteredAddress", address != null ? address.trim() : "");
        request.setAttribute("enteredContactNumber", contactNumber != null ? contactNumber.trim() : "");
        request.setAttribute("enteredDentistId", dentistIdStr != null ? dentistIdStr.trim() : "");
        request.setAttribute("enteredTreatmentId", treatmentIdStr != null ? treatmentIdStr.trim() : "");
        request.setAttribute("enteredAppointmentDate", dateStr != null ? dateStr.trim() : "");
        request.setAttribute("enteredAppointmentTime", timeStr != null ? timeStr.trim() : "");

        int dentistId = 0;
        int treatmentId = 0;
        LocalDate appointmentDate = null;
        LocalTime appointmentTime = null;

        try {
            if (dentistIdStr != null && !dentistIdStr.trim().isEmpty()) {
                dentistId = Integer.parseInt(dentistIdStr.trim());
            }
            if (treatmentIdStr != null && !treatmentIdStr.trim().isEmpty()) {
                treatmentId = Integer.parseInt(treatmentIdStr.trim());
            }
            if (dateStr != null && !dateStr.trim().isEmpty()) {
                appointmentDate = LocalDate.parse(dateStr.trim());
            }
            if (timeStr != null && !timeStr.trim().isEmpty()) {
                // Support HH:mm or HH:mm:ss formats from standard HTML5 time inputs
                appointmentTime = LocalTime.parse(timeStr.trim());
            }
        } catch (NumberFormatException | DateTimeParseException e) {
            request.setAttribute("errorMessage", "Invalid format in selection fields: " + e.getMessage());
            loadDropdownReferenceData(request);
            request.getRequestDispatcher("/register-appointment.jsp").forward(request, response);
            return;
        }

        try {
            String appointmentNumber = appointmentService.registerAppointment(
                    patientName, address, contactNumber,
                    dentistId, treatmentId,
                    appointmentDate, appointmentTime
            );

            // Registration succeeded: clear preserved input fields and set confirmation
            request.removeAttribute("enteredPatientName");
            request.removeAttribute("enteredAddress");
            request.removeAttribute("enteredContactNumber");
            request.removeAttribute("enteredDentistId");
            request.removeAttribute("enteredTreatmentId");
            request.removeAttribute("enteredAppointmentDate");
            request.removeAttribute("enteredAppointmentTime");

            request.setAttribute("successMessage", "Appointment registered successfully!");
            request.setAttribute("successAppointmentNumber", appointmentNumber);

        } catch (IllegalArgumentException e) {
            // Validation or availability rejection
            request.setAttribute("errorMessage", e.getMessage());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error during appointment registration", e);
            request.setAttribute("errorMessage", "A database error occurred while registering the appointment. Please try again.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error during appointment registration", e);
            request.setAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
        }

        loadDropdownReferenceData(request);
        request.getRequestDispatcher("/register-appointment.jsp").forward(request, response);
    }

    private void loadDropdownReferenceData(HttpServletRequest request) {
        try {
            List<Dentist> dentists = dentistDAO.findAll();
            List<Treatment> treatments = treatmentDAO.findAll();
            request.setAttribute("dentists", dentists);
            request.setAttribute("treatments", treatments);
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Failed to load reference data for appointment form", e);
            request.setAttribute("errorMessage", "Unable to load dentists or treatment catalog from database.");
        }
    }
}