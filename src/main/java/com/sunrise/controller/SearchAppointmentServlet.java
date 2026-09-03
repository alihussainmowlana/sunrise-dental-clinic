package com.sunrise.controller;

import com.sunrise.model.AppointmentDetails;
import com.sunrise.service.AppointmentService;
import jakarta.servlet.ServletException;
import jakarta.servlet.annotation.WebServlet;
import jakarta.servlet.http.HttpServlet;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

import java.io.IOException;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Controller handling search requests for appointment and patient details by appointment number.
 */
@WebServlet("/appointments/search")
public class SearchAppointmentServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(SearchAppointmentServlet.class.getName());

    private final AppointmentService appointmentService;

    public SearchAppointmentServlet() {
        this.appointmentService = new AppointmentService();
    }

    public SearchAppointmentServlet(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/search-appointment.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String appointmentNumber = request.getParameter("appointmentNumber");
        String trimmedNumber = appointmentNumber != null ? appointmentNumber.trim() : "";
        request.setAttribute("enteredAppointmentNumber", trimmedNumber);

        try {
            AppointmentDetails details = appointmentService.searchAppointment(appointmentNumber);

            if (details != null) {
                request.setAttribute("appointmentDetails", details);
            } else {
                request.setAttribute("errorMessage", "No appointment was found with this appointment number.");
            }

        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", e.getMessage());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error occurred while searching for appointment number: " + trimmedNumber, e);
            request.setAttribute("errorMessage", "A database error occurred while searching for the appointment. Please try again.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error occurred during appointment search", e);
            request.setAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
        }

        request.getRequestDispatcher("/search-appointment.jsp").forward(request, response);
    }
}