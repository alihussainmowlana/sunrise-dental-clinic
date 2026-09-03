package com.sunrise.controller;

import com.sunrise.model.BillDetails;
import com.sunrise.service.BillingService;
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
 * Controller handling billing calculation, receipt generation, and lookup requests.
 */
@WebServlet("/billing")
public class BillingServlet extends HttpServlet {

    private static final long serialVersionUID = 1L;
    private static final Logger LOGGER = Logger.getLogger(BillingServlet.class.getName());

    private final BillingService billingService;

    public BillingServlet() {
        this.billingService = new BillingService();
    }

    public BillingServlet(BillingService billingService) {
        this.billingService = billingService;
    }

    @Override
    protected void doGet(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {
        request.getRequestDispatcher("/billing.jsp").forward(request, response);
    }

    @Override
    protected void doPost(HttpServletRequest request, HttpServletResponse response)
            throws ServletException, IOException {

        String appointmentNumber = request.getParameter("appointmentNumber");
        String trimmedNumber = appointmentNumber != null ? appointmentNumber.trim() : "";
        request.setAttribute("enteredAppointmentNumber", trimmedNumber);

        try {
            BillDetails billDetails = billingService.generateOrGetBill(appointmentNumber);

            if (billDetails != null) {
                request.setAttribute("billDetails", billDetails);
            } else {
                request.setAttribute("errorMessage", "No appointment was found with this appointment number.");
            }

        } catch (IllegalArgumentException e) {
            request.setAttribute("errorMessage", e.getMessage());
        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error occurred during billing for appointment: " + trimmedNumber, e);
            request.setAttribute("errorMessage", "A database error occurred while processing the bill. Please try again.");
        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error occurred during billing", e);
            request.setAttribute("errorMessage", "An unexpected error occurred: " + e.getMessage());
        }

        request.getRequestDispatcher("/billing.jsp").forward(request, response);
    }
}