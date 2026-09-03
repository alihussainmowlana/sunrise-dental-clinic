package com.sunrise.service;

import com.sunrise.dao.AppointmentDAO;
import com.sunrise.model.Appointment;
import com.sunrise.model.AppointmentDetails;
import com.sunrise.model.Patient;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.regex.Pattern;

/**
 * Service class encapsulating business rules and coordination for appointment registration and searching.
 */
public class AppointmentService {

    private static final Logger LOGGER = Logger.getLogger(AppointmentService.class.getName());
    private static final Pattern PHONE_PATTERN = Pattern.compile("^[+]?[0-9\\s-]{7,25}$");

    private final AppointmentDAO appointmentDAO;

    public AppointmentService() {
        this.appointmentDAO = new AppointmentDAO();
    }

    public AppointmentService(AppointmentDAO appointmentDAO) {
        this.appointmentDAO = appointmentDAO;
    }

    /**
     * Registers a new patient and appointment after validating all inputs and verifying dentist availability.
     *
     * @param patientName     the full name of the patient
     * @param address         the patient's address
     * @param contactNumber   the patient's contact phone number
     * @param dentistId       the selected dentist identifier
     * @param treatmentId     the selected treatment identifier
     * @param appointmentDate the requested appointment date
     * @param appointmentTime the requested appointment time
     * @return the generated appointment number (e.g. APT-A83F92C1) on successful registration
     * @throws IllegalArgumentException if any validation rule or availability check fails
     * @throws SQLException             if a database transaction error occurs
     */
    public String registerAppointment(String patientName, String address, String contactNumber,
                                      int dentistId, int treatmentId,
                                      LocalDate appointmentDate, LocalTime appointmentTime)
            throws SQLException {

        // 1. Validate Patient Details
        if (patientName == null || patientName.trim().isEmpty()) {
            throw new IllegalArgumentException("Patient name is required.");
        }
        if (address == null || address.trim().isEmpty()) {
            throw new IllegalArgumentException("Patient address is required.");
        }
        if (contactNumber == null || contactNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Contact number is required.");
        }
        String cleanContact = contactNumber.trim();
        if (!PHONE_PATTERN.matcher(cleanContact).matches()) {
            throw new IllegalArgumentException("Please provide a valid contact number (7-25 digits, optional '+' or hyphens).");
        }

        // 2. Validate Reference Identifiers
        if (dentistId <= 0) {
            throw new IllegalArgumentException("Please select a valid dentist.");
        }
        if (treatmentId <= 0) {
            throw new IllegalArgumentException("Please select a valid treatment.");
        }

        // 3. Validate Date and Time (Cannot be in the past)
        if (appointmentDate == null) {
            throw new IllegalArgumentException("Appointment date is required.");
        }
        if (appointmentTime == null) {
            throw new IllegalArgumentException("Appointment time is required.");
        }
        if (appointmentTime.getMinute() % 15 != 0 || appointmentTime.getSecond() != 0 || appointmentTime.getNano() != 0) {
            throw new IllegalArgumentException("Appointment time must be selected in 15-minute intervals.");
        }

        LocalDate today = LocalDate.now();
        if (appointmentDate.isBefore(today)) {
            throw new IllegalArgumentException("Appointment date cannot be in the past.");
        }
        if (appointmentDate.isEqual(today) && appointmentTime.isBefore(LocalTime.now())) {
            throw new IllegalArgumentException("Appointment time cannot be in the past for today's date.");
        }

        // 4. Validate Dentist Availability (Double Booking Prevention)
        boolean available = appointmentDAO.isDentistAvailable(dentistId, appointmentDate, appointmentTime);
        if (!available) {
            LOGGER.log(Level.WARNING, "Double-booking prevented: Dentist ID {0} is unavailable on {1} at {2}",
                    new Object[]{dentistId, appointmentDate, appointmentTime});
            throw new IllegalArgumentException("The selected dentist already has an appointment at this date and time. Please choose another time slot.");
        }

        // 5. Generate Unique Appointment Number
        String appointmentNumber = generateAppointmentNumber();

        // 6. Assemble Entities
        Patient patient = new Patient(patientName.trim(), address.trim(), cleanContact);
        Appointment appointment = new Appointment(
                appointmentNumber,
                0, // patientId will be populated during transactional insertion
                dentistId,
                treatmentId,
                appointmentDate,
                appointmentTime
        );

        // 7. Execute Transaction via DAO
        boolean registered = appointmentDAO.registerAppointment(patient, appointment);
        if (!registered) {
            throw new SQLException("Failed to complete appointment registration transaction.");
        }

        LOGGER.log(Level.INFO, "Successfully registered appointment {0} for patient ''{1}''",
                new Object[]{appointmentNumber, patient.getPatientName()});

        return appointmentNumber;
    }

    /**
     * Searches for complete appointment details by appointment number.
     * Validates input, normalizes whitespace and casing, and delegates lookup to AppointmentDAO.
     *
     * @param appointmentNumber the appointment reference number to search
     * @return AppointmentDetails if found, or null if no appointment matches the number
     * @throws IllegalArgumentException if the appointment number is null or blank
     * @throws SQLException             if a database access error occurs
     */
    public AppointmentDetails searchAppointment(String appointmentNumber) throws SQLException {
        if (appointmentNumber == null || appointmentNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Appointment number is required.");
        }

        String normalizedNumber = appointmentNumber.trim().toUpperCase();
        return appointmentDAO.findByAppointmentNumber(normalizedNumber);
    }

    /**
     * Generates a readable, unique appointment reference number format (e.g. APT-A83F92C1).
     */
    protected String generateAppointmentNumber() {
        String uuidSegment = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        return "APT-" + uuidSegment;
    }
}