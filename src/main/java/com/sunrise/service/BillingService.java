package com.sunrise.service;

import com.sunrise.dao.BillDAO;
import com.sunrise.model.AppointmentBillingInfo;
import com.sunrise.model.Bill;
import com.sunrise.model.BillDetails;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.UUID;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Service class encapsulating business rules, fee calculations, and receipt generation.
 */
public class BillingService {

    private static final Logger LOGGER = Logger.getLogger(BillingService.class.getName());

    /**
     * Documented system assumption: Fixed clinic consultation fee of LKR 1,000.00.
     */
    public static final BigDecimal CONSULTATION_FEE = new BigDecimal("1000.00");

    private final BillDAO billDAO;

    public BillingService() {
        this.billDAO = new BillDAO();
    }

    public BillingService(BillDAO billDAO) {
        this.billDAO = billDAO;
    }

    /**
     * Calculates the bill or returns the existing bill for the specified appointment.
     * Prevents duplicate bills by returning existing BillDetails if already generated.
     *
     * @param appointmentNumber the appointment reference number
     * @return BillDetails representing the complete receipt, or null if appointment does not exist
     * @throws IllegalArgumentException if the appointment number is null or blank
     * @throws SQLException             if a database access or insertion error occurs
     */
    public BillDetails generateOrGetBill(String appointmentNumber) throws SQLException {
        if (appointmentNumber == null || appointmentNumber.trim().isEmpty()) {
            throw new IllegalArgumentException("Appointment number is required.");
        }

        String normalizedNumber = appointmentNumber.trim().toUpperCase();

        // 1. Check for existing bill (duplicate protection)
        BillDetails existingBill = billDAO.findByAppointmentNumber(normalizedNumber);
        if (existingBill != null) {
            LOGGER.log(Level.INFO, "Existing bill found for appointment {0}: {1}",
                    new Object[]{normalizedNumber, existingBill.getBillNumber()});
            return existingBill;
        }

        // 2. Find appointment and treatment details
        AppointmentBillingInfo apptInfo = billDAO.findAppointmentForBilling(normalizedNumber);
        if (apptInfo == null) {
            LOGGER.log(Level.WARNING, "Appointment {0} not found for billing.", normalizedNumber);
            return null;
        }

        // 3. Compute charges: Total = Treatment Cost + Consultation Fee
        BigDecimal treatmentCost = apptInfo.getTreatmentCost();
        BigDecimal consultationFee = CONSULTATION_FEE;
        BigDecimal totalAmount = treatmentCost.add(consultationFee);

        // 4. Generate unique bill number
        String billNumber = generateBillNumber();

        // 5. Persist Bill record
        Bill bill = new Bill(
                billNumber,
                apptInfo.getAppointmentId(),
                consultationFee,
                treatmentCost,
                totalAmount
        );

        boolean created = billDAO.createBill(bill);
        if (!created) {
            throw new SQLException("Failed to insert bill record into database.");
        }

        LOGGER.log(Level.INFO, "Generated new bill {0} for appointment {1}. Total: LKR {2}",
                new Object[]{billNumber, normalizedNumber, totalAmount});

        // 6. Return complete BillDetails (retrieving the persisted record with timestamp)
        BillDetails generatedDetails = billDAO.findByAppointmentNumber(normalizedNumber);
        if (generatedDetails != null) {
            return generatedDetails;
        }

        // Fallback in-memory assembly if direct read fails
        return new BillDetails(
                billNumber,
                apptInfo.getAppointmentNumber(),
                apptInfo.getPatientName(),
                apptInfo.getContactNumber(),
                apptInfo.getDentistName(),
                apptInfo.getTreatmentName(),
                apptInfo.getAppointmentDate(),
                apptInfo.getAppointmentTime(),
                consultationFee,
                treatmentCost,
                totalAmount,
                java.time.LocalDateTime.now()
        );
    }

    /**
     * Generates a readable, unique bill reference number format (e.g. BILL-A48C91F2).
     */
    protected String generateBillNumber() {
        String uuidSegment = UUID.randomUUID().toString().replace("-", "").substring(0, 8).toUpperCase();
        return "BILL-" + uuidSegment;
    }
}