package com.sunrise.service;

import com.sunrise.dao.BillDAO;
import com.sunrise.model.AppointmentBillingInfo;
import com.sunrise.model.Bill;
import com.sunrise.model.BillDetails;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class BillingServiceTest {

    private BillingService billingService;
    private StubBillDAO stubDAO;

    // In-memory Test Double / Stub for BillDAO
    static class StubBillDAO extends BillDAO {
        private final Map<String, AppointmentBillingInfo> appointments = new HashMap<>();
        private final Map<String, BillDetails> billedRecords = new HashMap<>();
        private boolean simulateInsertFailure = false;
        private int createBillCallCount = 0;

        public StubBillDAO() {
            // Seed known appointment 1
            appointments.put("APT-FE26B49D", new AppointmentBillingInfo(
                    1,
                    "APT-FE26B49D",
                    "Saman Kumara",
                    "077-9876543",
                    "Dr. Perera",
                    "Dental Cleaning",
                    new BigDecimal("40.00"),
                    LocalDate.of(2026, 9, 9),
                    LocalTime.of(10, 30)
            ));

            // Seed known appointment 2
            appointments.put("APT-RC200", new AppointmentBillingInfo(
                    2,
                    "APT-RC200",
                    "Nimal Silva",
                    "071-1122334",
                    "Dr. Silva",
                    "Root Canal Treatment",
                    new BigDecimal("250.00"),
                    LocalDate.of(2026, 9, 15),
                    LocalTime.of(14, 0)
            ));
        }

        @Override
        public BillDetails findByAppointmentNumber(String appointmentNumber) throws SQLException {
            return billedRecords.get(appointmentNumber);
        }

        @Override
        public AppointmentBillingInfo findAppointmentForBilling(String appointmentNumber) throws SQLException {
            return appointments.get(appointmentNumber);
        }

        @Override
        public boolean createBill(Bill bill) throws SQLException {
            if (simulateInsertFailure) {
                return false;
            }
            createBillCallCount++;
            bill.setBillId(100 + createBillCallCount);
            bill.setGeneratedAt(LocalDateTime.now());

            // Find corresponding appointment to populate BillDetails
            for (AppointmentBillingInfo info : appointments.values()) {
                if (info.getAppointmentId() == bill.getAppointmentId()) {
                    BillDetails details = new BillDetails(
                            bill.getBillNumber(),
                            info.getAppointmentNumber(),
                            info.getPatientName(),
                            info.getContactNumber(),
                            info.getDentistName(),
                            info.getTreatmentName(),
                            info.getAppointmentDate(),
                            info.getAppointmentTime(),
                            bill.getConsultationFee(),
                            bill.getTreatmentCost(),
                            bill.getTotalAmount(),
                            bill.getGeneratedAt()
                    );
                    billedRecords.put(info.getAppointmentNumber(), details);
                    break;
                }
            }
            return true;
        }

        public void setSimulateInsertFailure(boolean fail) {
            this.simulateInsertFailure = fail;
        }

        public int getCreateBillCallCount() {
            return createBillCallCount;
        }
    }

    @BeforeEach
    void setUp() {
        stubDAO = new StubBillDAO();
        billingService = new BillingService(stubDAO);
    }

    @Test
    @DisplayName("1. Existing appointment generates a bill successfully")
    void testGenerateBillSuccess() throws SQLException {
        BillDetails bill = billingService.generateOrGetBill("APT-FE26B49D");

        assertNotNull(bill);
        assertEquals("APT-FE26B49D", bill.getAppointmentNumber());
        assertEquals("Saman Kumara", bill.getPatientName());
        assertEquals("Dr. Perera", bill.getDentistName());
        assertEquals("Dental Cleaning", bill.getTreatmentName());
        assertEquals(1, stubDAO.getCreateBillCallCount());
    }

    @Test
    @DisplayName("2. Treatment cost is retained correctly from appointment")
    void testTreatmentCostRetained() throws SQLException {
        BillDetails bill = billingService.generateOrGetBill("APT-FE26B49D");

        assertNotNull(bill);
        assertEquals(new BigDecimal("40.00"), bill.getTreatmentCost());
    }

    @Test
    @DisplayName("3. Consultation fee is exactly LKR 1000.00")
    void testConsultationFeeExact() throws SQLException {
        BillDetails bill = billingService.generateOrGetBill("APT-FE26B49D");

        assertNotNull(bill);
        assertEquals(new BigDecimal("1000.00"), bill.getConsultationFee());
        assertEquals(new BigDecimal("1000.00"), BillingService.CONSULTATION_FEE);
    }

    @Test
    @DisplayName("4. Total amount equals treatment cost plus consultation fee")
    void testTotalEqualsTreatmentCostPlusConsultationFee() throws SQLException {
        // Dental Cleaning (40.00) + Consultation (1000.00) = 1040.00
        BillDetails bill1 = billingService.generateOrGetBill("APT-FE26B49D");
        assertNotNull(bill1);
        assertEquals(new BigDecimal("1040.00"), bill1.getTotalAmount());

        // Root Canal (250.00) + Consultation (1000.00) = 1250.00
        BillDetails bill2 = billingService.generateOrGetBill("APT-RC200");
        assertNotNull(bill2);
        assertEquals(new BigDecimal("1250.00"), bill2.getTotalAmount());
    }

    @Test
    @DisplayName("5. Bill number has BILL- prefix and follows standard format")
    void testBillNumberPrefix() throws SQLException {
        BillDetails bill = billingService.generateOrGetBill("APT-FE26B49D");

        assertNotNull(bill);
        assertTrue(bill.getBillNumber().startsWith("BILL-"));
        assertTrue(bill.getBillNumber().matches("^BILL-[A-F0-9]{8}$"));
    }

    @Test
    @DisplayName("6. Blank appointment number is rejected")
    void testBlankAppointmentNumberRejected() {
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () ->
                billingService.generateOrGetBill(""));
        assertEquals("Appointment number is required.", ex1.getMessage());

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () ->
                billingService.generateOrGetBill("   "));
        assertEquals("Appointment number is required.", ex2.getMessage());
    }

    @Test
    @DisplayName("7. Null appointment number is rejected")
    void testNullAppointmentNumberRejected() {
        IllegalArgumentException ex = assertThrows(IllegalArgumentException.class, () ->
                billingService.generateOrGetBill(null));
        assertEquals("Appointment number is required.", ex.getMessage());
    }

    @Test
    @DisplayName("8. Unknown appointment returns null")
    void testUnknownAppointmentReturnsNull() throws SQLException {
        BillDetails bill = billingService.generateOrGetBill("APT-UNKNOWN");
        assertNull(bill);
        assertEquals(0, stubDAO.getCreateBillCallCount());
    }

    @Test
    @DisplayName("9. Lowercase appointment number is normalized and generates bill")
    void testLowercaseAppointmentNumberNormalized() throws SQLException {
        BillDetails bill = billingService.generateOrGetBill("apt-fe26b49d");

        assertNotNull(bill);
        assertEquals("APT-FE26B49D", bill.getAppointmentNumber());
    }

    @Test
    @DisplayName("10. Existing bill is returned instead of creating a duplicate")
    void testExistingBillReturnedWithoutDuplicateCreation() throws SQLException {
        // First billing request creates bill
        BillDetails firstBill = billingService.generateOrGetBill("APT-FE26B49D");
        assertNotNull(firstBill);
        String firstBillNumber = firstBill.getBillNumber();
        assertEquals(1, stubDAO.getCreateBillCallCount());

        // Second billing request returns the same bill without executing insert
        BillDetails secondBill = billingService.generateOrGetBill("APT-FE26B49D");
        assertNotNull(secondBill);
        assertEquals(firstBillNumber, secondBill.getBillNumber());
        assertEquals(1, stubDAO.getCreateBillCallCount()); // createBill was NOT called again
    }

    @Test
    @DisplayName("11. DAO insertion failure throws SQLException")
    void testDaoInsertionFailureThrowsSQLException() {
        stubDAO.setSimulateInsertFailure(true);

        assertThrows(SQLException.class, () ->
                billingService.generateOrGetBill("APT-FE26B49D"));
    }
}