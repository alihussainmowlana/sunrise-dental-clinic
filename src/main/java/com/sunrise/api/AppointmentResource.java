package com.sunrise.api;

import com.sunrise.model.AppointmentDetails;
import com.sunrise.service.AppointmentService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import jakarta.ws.rs.GET;
import jakarta.ws.rs.Path;
import jakarta.ws.rs.PathParam;
import jakarta.ws.rs.Produces;
import jakarta.ws.rs.core.Context;
import jakarta.ws.rs.core.MediaType;
import jakarta.ws.rs.core.Response;

import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Jakarta REST Resource exposing read-only appointment lookup endpoints.
 */
@Path("/appointments")
public class AppointmentResource {

    private static final Logger LOGGER = Logger.getLogger(AppointmentResource.class.getName());

    private final AppointmentService appointmentService;

    public AppointmentResource() {
        this.appointmentService = new AppointmentService();
    }

    public AppointmentResource(AppointmentService appointmentService) {
        this.appointmentService = appointmentService;
    }

    /**
     * Retrieves complete appointment details by unique appointment number as JSON.
     * Enforces authenticated session security.
     *
     * @param appointmentNumber the appointment reference number
     * @param request           the HTTP request context for session inspection
     * @return HTTP 200 with JSON on success, 400 on invalid input, 401 on unauthenticated access,
     *         404 when appointment not found, or 500 on database failure
     */
    @GET
    @Path("/{appointmentNumber}")
    @Produces(MediaType.APPLICATION_JSON)
    public Response getAppointment(@PathParam("appointmentNumber") String appointmentNumber,
                                   @Context HttpServletRequest request) {

        // 1. Authenticated Session Verification
        if (request == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"Unauthorized access. Please log in first.\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        HttpSession session = request.getSession(false);
        if (session == null || session.getAttribute("loggedUser") == null) {
            return Response.status(Response.Status.UNAUTHORIZED)
                    .entity("{\"error\":\"Unauthorized access. Please log in first.\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        // 2. Input Parameter Validation
        if (appointmentNumber == null || appointmentNumber.trim().isEmpty()) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"Appointment number is required\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }

        // 3. Service Lookup Delegation
        try {
            AppointmentDetails details = appointmentService.searchAppointment(appointmentNumber);

            if (details == null) {
                return Response.status(Response.Status.NOT_FOUND)
                        .entity("{\"error\":\"Appointment not found\"}")
                        .type(MediaType.APPLICATION_JSON)
                        .build();
            }

            // Build exact JSON response
            String jsonResponse = buildAppointmentJson(details);
            return Response.ok(jsonResponse, MediaType.APPLICATION_JSON).build();

        } catch (IllegalArgumentException e) {
            return Response.status(Response.Status.BAD_REQUEST)
                    .entity("{\"error\":\"" + escapeJson(e.getMessage()) + "\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } catch (SQLException e) {
            LOGGER.log(Level.SEVERE, "Database error in REST endpoint for appointment: " + appointmentNumber, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Internal server error\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();

        } catch (Exception e) {
            LOGGER.log(Level.SEVERE, "Unexpected error in REST endpoint for appointment: " + appointmentNumber, e);
            return Response.status(Response.Status.INTERNAL_SERVER_ERROR)
                    .entity("{\"error\":\"Internal server error\"}")
                    .type(MediaType.APPLICATION_JSON)
                    .build();
        }
    }

    private String buildAppointmentJson(AppointmentDetails details) {
        StringBuilder sb = new StringBuilder();
        sb.append("{\n");
        sb.append("  \"appointmentNumber\": \"").append(escapeJson(details.getAppointmentNumber())).append("\",\n");
        sb.append("  \"patientName\": \"").append(escapeJson(details.getPatientName())).append("\",\n");
        sb.append("  \"address\": \"").append(escapeJson(details.getAddress())).append("\",\n");
        sb.append("  \"contactNumber\": \"").append(escapeJson(details.getContactNumber())).append("\",\n");
        sb.append("  \"dentistName\": \"").append(escapeJson(details.getDentistName())).append("\",\n");
        sb.append("  \"treatmentName\": \"").append(escapeJson(details.getTreatmentName())).append("\",\n");
        sb.append("  \"treatmentCost\": ").append(details.getTreatmentCost()).append(",\n");
        sb.append("  \"appointmentDate\": \"").append(details.getAppointmentDate()).append("\",\n");
        sb.append("  \"appointmentTime\": \"").append(details.getAppointmentTime()).append("\"\n");
        sb.append("}");
        return sb.toString();
    }

    private String escapeJson(String raw) {
        if (raw == null) {
            return "";
        }
        return raw.replace("\\", "\\\\")
                  .replace("\"", "\\\"")
                  .replace("\b", "\\b")
                  .replace("\f", "\\f")
                  .replace("\n", "\\n")
                  .replace("\r", "\\r")
                  .replace("\t", "\\t");
    }
}