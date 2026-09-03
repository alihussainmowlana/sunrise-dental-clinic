package com.sunrise.service;

import com.sunrise.dao.AppointmentDAO;
import com.sunrise.model.Appointment;
import com.sunrise.model.AppointmentDetails;
import com.sunrise.model.Patient;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.*;

public class AppointmentServiceTest {

    private AppointmentService appointmentService;
    private StubAppointmentDAO stubDAO;

    // In-memory Test Double / Stub for AppointmentDAO
    static class StubAppointmentDAO extends AppointmentDAO {
        private boolean simulateRegistrationFailure = false;
        private int registeredCount = 0;

        @Override
        public boolean isDentistAvailable(int dentistId, LocalDate date, LocalTime time) throws SQLException {
            // Dentist 1 is already booked on tomorrow at 10:00 AM
            LocalDate tomorrow = LocalDate.now().plusDays(1);
            if (dentistId == 1 && date.equals(tomorrow) && time.equals(LocalTime.of(10, 0))) {
                return false;
            }
            return true;
        }

        @Override
        public boolean registerAppointment(Patient patient, Appointment appointment) throws SQLException {
            if (simulateRegistrationFailure) {
                return false;
            }
            registeredCount++;
            patient.setPatientId(100 + registeredCount);
            appointment.setAppointmentId(200 + registeredCount);
            return true;
        }

        @Override
        public AppointmentDetails findByAppointmentNumber(String appointmentNumber) throws SQLException {
            if ("APT-FE26B49D".equals(appointmentNumber)) {
                return new AppointmentDetails(
                        "APT-FE26B49D",
                        "Saman Kumara",
                        "77 Galle Face Road, Colombo 03",
                        "077-9876543",
                        "Dr. Perera",
                        "Dental Cleaning",
                        new BigDecimal("40.00"),
                        LocalDate.of(2026, 9, 9),
                        LocalTime.of(10, 30)
                );
            }
            return null;
        }

        public void setSimulateRegistrationFailure(boolean fail) {
            this.simulateRegistrationFailure = fail;
        }

        public int getRegisteredCount() {
            return registeredCount;
        }
    }

    @BeforeEach
    void setUp() {
        stubDAO = new StubAppointmentDAO();
        appointmentService = new AppointmentService(stubDAO);
    }

    // =========================================================================
    // Registration Tests (Phase 4 preserved)
    // =========================================================================

    @Test
    @DisplayName("1. Successful valid appointment registration returns generated appointment number")
    void testRegisterAppointmentSuccess() throws SQLException {
        LocalDate futureDate = LocalDate.now().plusDays(2);
        LocalTime apptTime = LocalTime.of(14, 30);

        String apptNumber = appointmentService.registerAppointment(
                "Kamal Gunaratne",
                "123 Galle Road, Colombo",
                "077-1234567",
                1,
                2,
                futureDate,
                apptTime
        );

        assertNotNull(apptNumber);
        assertTrue(apptNumber.startsWith("APT-"));
        assertEquals(1, stubDAO.getRegisteredCount());
    }

    @Test
    @DisplayName("2. Missing or blank patient name is rejected")
    void testRegisterAppointmentMissingPatientName() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        LocalTime apptTime = LocalTime.of(11, 0);

        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () ->
                appointmentService.registerAppointment(
                        null,
                        "123 Galle Road",
                        "077-1234567",
                        1, 1, futureDate, apptTime
                ));
        assertEquals("Patient name is required.", ex1.getMessage());

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () ->
                appointmentService.registerAppointment(
                        "   ",
                        "123 Galle Road",
                        "077-1234567",
                        1, 1, futureDate, apptTime
                ));
        assertEquals("Patient name is required.", ex2.getMessage());
    }

    @Test
    @DisplayName("3. Missing or blank patient address is rejected")
    void testRegisterAppointmentMissingAddress() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        LocalTime apptTime = LocalTime.of(11, 0);

        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () ->
                appointmentService.registerAppointment(
                        "Nimal Perera",
                        null,
                        "077-1234567",
                        1, 1, futureDate, apptTime
                ));
        assertEquals("Patient address is required.", ex1.getMessage());

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () ->
                appointmentService.registerAppointment(
                        "Nimal Perera",
                        "  ",
                        "077-1234567",
                        1, 1, futureDate, apptTime
                ));
        assertEquals("Patient address is required.", ex2.getMessage());
    }

    @Test
    @DisplayName("4. Invalid contact number is rejected")
    void testRegisterAppointmentInvalidContactNumber() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        LocalTime apptTime = LocalTime.of(11, 0);

        // Blank
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () ->
                appointmentService.registerAppointment(
                        "Nimal Perera",
                        "45 Kandy Road",
                        "",
                        1, 1, futureDate, apptTime
                ));
        assertEquals("Contact number is required.", ex1.getMessage());

        // Too short / invalid characters
        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () ->
                appointmentService.registerAppointment(
                        "Nimal Perera",
                        "45 Kandy Road",
                        "123",
                        1, 1, futureDate, apptTime
                ));
        assertTrue(ex2.getMessage().contains("Please provide a valid contact number"));

        // Letters in phone
        IllegalArgumentException ex3 = assertThrows(IllegalArgumentException.class, () ->
                appointmentService.registerAppointment(
                        "Nimal Perera",
                        "45 Kandy Road",
                        "077-ABC-1234",
                        1, 1, futureDate, apptTime
                ));
        assertTrue(ex3.getMessage().contains("Please provide a valid contact number"));
    }

    @Test
    @DisplayName("5. Invalid dentist ID is rejected")
    void testRegisterAppointmentInvalidDentistId() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        LocalTime apptTime = LocalTime.of(11, 0);

        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () ->
                appointmentService.registerAppointment(
                        "Nimal Perera",
                        "45 Kandy Road",
                        "077-1234567",
                        0, 1, futureDate, apptTime
                ));
        assertEquals("Please select a valid dentist.", ex1.getMessage());

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () ->
                appointmentService.registerAppointment(
                        "Nimal Perera",
                        "45 Kandy Road",
                        "077-1234567",
                        -5, 1, futureDate, apptTime
                ));
        assertEquals("Please select a valid dentist.", ex2.getMessage());
    }

    @Test
    @DisplayName("6. Invalid treatment ID is rejected")
    void testRegisterAppointmentInvalidTreatmentId() {
        LocalDate futureDate = LocalDate.now().plusDays(1);
        LocalTime apptTime = LocalTime.of(11, 0);

        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () ->
                appointmentService.registerAppointment(
                        "Nimal Perera",
                        "45 Kandy Road",
                        "077-1234567",
                        1, 0, futureDate, apptTime
                ));
        assertEquals("Please select a valid treatment.", ex1.getMessage());

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () ->
                appointmentService.registerAppointment(
                        "Nimal Perera",
                        "45 Kandy Road",
                        "077-1234567",
                        1, -1, futureDate, apptTime
                ));
        assertEquals("Please select a valid treatment.", ex2.getMessage());
    }

    @Test
    @DisplayName("7. Past appointment date or time is rejected")
    void testRegisterAppointmentPastDateOrTime() {
        LocalDate pastDate = LocalDate.now().minusDays(1);
        LocalTime apptTime = LocalTime.of(10, 0);

        // Date in past
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () ->
                appointmentService.registerAppointment(
                        "Nimal Perera",
                        "45 Kandy Road",
                        "077-1234567",
                        1, 1, pastDate, apptTime
                ));
        assertEquals("Appointment date cannot be in the past.", ex1.getMessage());

        // Null date
        assertThrows(IllegalArgumentException.class, () ->
                appointmentService.registerAppointment(
                        "Nimal Perera",
                        "45 Kandy Road",
                        "077-1234567",
                        1, 1, null, apptTime
                ));

        // Null time
        assertThrows(IllegalArgumentException.class, () ->
                appointmentService.registerAppointment(
                        "Nimal Perera",
                        "45 Kandy Road",
                        "077-1234567",
                        1, 1, LocalDate.now().plusDays(1), null
                ));
    }

    @Test
    @DisplayName("8. Dentist double booking is rejected with informative message")
    void testRegisterAppointmentDoubleBookingRejected() {
        LocalDate tomorrow = LocalDate.now().plusDays(1);
        LocalTime bookedTime = LocalTime.of(10, 0);

        // Attempting to book Dentist 1 on tomorrow at 10:00 (which is mocked as already booked)
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                appointmentService.registerAppointment(
                        "Sunil Shantha",
                        "10 Temple Road, Matara",
                        "071-9876543",
                        1, // Dentist 1
                        2, // Treatment 2
                        tomorrow,
                        bookedTime
                ));

        assertEquals("The selected dentist already has an appointment at this date and time. Please choose another time slot.",
                ex.getMessage());
    }

    @Test
    @DisplayName("9. Successful registration returns an appointment number with APT- prefix")
    void testAppointmentNumberFormat() throws SQLException {
        LocalDate futureDate = LocalDate.now().plusDays(5);
        LocalTime apptTime = LocalTime.of(15, 0);

        String apptNumber = appointmentService.registerAppointment(
                "Anura Kumara",
                "89 Main Street, Kurunegala",
                "+94 77 123 4567",
                2,
                3,
                futureDate,
                apptTime
        );

        assertNotNull(apptNumber);
        assertTrue(apptNumber.matches("^APT-[A-F0-9]{8}$"));
    }

    @Test
    @DisplayName("10. Database transaction failure throws SQLException")
    void testRegistrationDatabaseFailure() {
        stubDAO.setSimulateRegistrationFailure(true);
        LocalDate futureDate = LocalDate.now().plusDays(3);
        LocalTime apptTime = LocalTime.of(9, 30);

        assertThrows(SQLException.class, () ->
                appointmentService.registerAppointment(
                        "Test Patient",
                        "Test Address",
                        "077-1112233",
                        1,
                        1,
                        futureDate,
                        apptTime
                ));
    }

    // =========================================================================
    // Search Tests (Phase 5)
    // =========================================================================

    @Test
    @DisplayName("11. Existing appointment number returns complete AppointmentDetails")
    void testSearchAppointmentFound() throws SQLException {
        AppointmentDetails details = appointmentService.searchAppointment("APT-FE26B49D");

        assertNotNull(details);
        assertEquals("APT-FE26B49D", details.getAppointmentNumber());
        assertEquals("Saman Kumara", details.getPatientName());
        assertEquals("77 Galle Face Road, Colombo 03", details.getAddress());
        assertEquals("077-9876543", details.getContactNumber());
        assertEquals("Dr. Perera", details.getDentistName());
        assertEquals("Dental Cleaning", details.getTreatmentName());
        assertEquals(new BigDecimal("40.00"), details.getTreatmentCost());
        assertEquals(LocalDate.of(2026, 9, 9), details.getAppointmentDate());
        assertEquals(LocalTime.of(10, 30), details.getAppointmentTime());
    }

    @Test
    @DisplayName("12. Unknown appointment number returns null")
    void testSearchAppointmentNotFound() throws SQLException {
        AppointmentDetails details = appointmentService.searchAppointment("APT-UNKNOWN0");
        assertNull(details);
    }

    @Test
    @DisplayName("13. Null appointment number throws IllegalArgumentException")
    void testSearchAppointmentNullRejected() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                appointmentService.searchAppointment(null));
        assertEquals("Appointment number is required.", ex.getMessage());
    }

    @Test
    @DisplayName("14. Blank or whitespace appointment number throws IllegalArgumentException")
    void testSearchAppointmentBlankRejected() {
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () ->
                appointmentService.searchAppointment(""));
        assertEquals("Appointment number is required.", ex1.getMessage());

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () ->
                appointmentService.searchAppointment("   "));
        assertEquals("Appointment number is required.", ex2.getMessage());
    }

    @Test
    @DisplayName("15. Leading and trailing whitespace is trimmed during search")
    void testSearchAppointmentWhitespaceTrimmed() throws SQLException {
        AppointmentDetails details = appointmentService.searchAppointment("   APT-FE26B49D   ");
        assertNotNull(details);
        assertEquals("APT-FE26B49D", details.getAppointmentNumber());
    }

    @Test
    @DisplayName("16. Lowercase appointment number finds matching uppercase record via normalization")
    void testSearchAppointmentLowercaseNormalized() throws SQLException {
        AppointmentDetails details = appointmentService.searchAppointment("apt-fe26b49d");
        assertNotNull(details);
        assertEquals("APT-FE26B49D", details.getAppointmentNumber());
    }

    @Test
    @DisplayName("17. Appointment time must be selected in 15-minute intervals")
    void testAppointmentTime15MinuteIntervalValidation() throws SQLException {
        LocalDate futureDate = LocalDate.now().plusDays(2);

        // Valid intervals (:00, :15, :30, :45) succeed
        String appt1 = appointmentService.registerAppointment(
                "Nimal Perera", "123 Main St", "077-1234567", 1, 1, futureDate, LocalTime.of(9, 0)
        );
        assertNotNull(appt1);

        String appt2 = appointmentService.registerAppointment(
                "Nimal Perera", "123 Main St", "077-1234567", 1, 1, futureDate, LocalTime.of(9, 15)
        );
        assertNotNull(appt2);

        // Invalid intervals (e.g. 10:07, 10:01, 10:22, non-zero seconds) are rejected
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () ->
                appointmentService.registerAppointment(
                        "Nimal Perera", "123 Main St", "077-1234567", 1, 1, futureDate, LocalTime.of(10, 7)
                ));
        assertEquals("Appointment time must be selected in 15-minute intervals.", ex1.getMessage());

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () ->
                appointmentService.registerAppointment(
                        "Nimal Perera", "123 Main St", "077-1234567", 1, 1, futureDate, LocalTime.of(10, 1)
                ));
        assertEquals("Appointment time must be selected in 15-minute intervals.", ex2.getMessage());

        IllegalArgumentException ex3 = assertThrows(IllegalArgumentException.class, () ->
                appointmentService.registerAppointment(
                        "Nimal Perera", "123 Main St", "077-1234567", 1, 1, futureDate, LocalTime.of(10, 22)
                ));
        assertEquals("Appointment time must be selected in 15-minute intervals.", ex3.getMessage());

        IllegalArgumentException ex4 = assertThrows(IllegalArgumentException.class, () ->
                appointmentService.registerAppointment(
                        "Nimal Perera", "123 Main St", "077-1234567", 1, 1, futureDate, LocalTime.of(10, 0, 15)
                ));
        assertEquals("Appointment time must be selected in 15-minute intervals.", ex4.getMessage());
    }
}