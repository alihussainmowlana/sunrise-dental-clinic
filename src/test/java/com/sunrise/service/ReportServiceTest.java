package com.sunrise.service;

import com.sunrise.dao.ReportDAO;
import com.sunrise.model.AppointmentReportRow;
import com.sunrise.model.BillingReportRow;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class ReportServiceTest {

    private ReportService reportService;
    private StubReportDAO stubDAO;

    static class StubReportDAO extends ReportDAO {
        private final LocalDate populatedDate = LocalDate.of(2026, 9, 10);

        @Override
        public List<AppointmentReportRow> findAppointmentsByDate(LocalDate date) throws SQLException {
            if (populatedDate.equals(date)) {
                List<AppointmentReportRow> list = new ArrayList<>();
                list.add(new AppointmentReportRow(
                        "APT-001", "Kamal Perera", "0771234567", "Dr. Silva", "Dental Cleaning", LocalTime.of(9, 0)
                ));
                list.add(new AppointmentReportRow(
                        "APT-002", "Nimal Fernando", "0719876543", "Dr. Perera", "Consultation", LocalTime.of(9, 30)
                ));
                return list;
            }
            return Collections.emptyList();
        }

        @Override
        public List<BillingReportRow> findBillsByDate(LocalDate date) throws SQLException {
            if (populatedDate.equals(date)) {
                List<BillingReportRow> list = new ArrayList<>();
                list.add(new BillingReportRow(
                        "BILL-001", "APT-001", "Kamal Perera", "Dental Cleaning",
                        new BigDecimal("5000.00"), new BigDecimal("1000.00"), new BigDecimal("6000.00"),
                        LocalDateTime.of(2026, 9, 10, 10, 0)
                ));
                list.add(new BillingReportRow(
                        "BILL-002", "APT-002", "Nimal Fernando", "Consultation",
                        new BigDecimal("1500.00"), new BigDecimal("1000.00"), new BigDecimal("2500.00"),
                        LocalDateTime.of(2026, 9, 10, 11, 30)
                ));
                return list;
            }
            return Collections.emptyList();
        }
    }

    @BeforeEach
    void setUp() {
        stubDAO = new StubReportDAO();
        reportService = new ReportService(stubDAO);
    }

    @Test
    @DisplayName("1. Appointment report returns rows for scheduled date")
    void testAppointmentReportReturnsRows() throws SQLException {
        LocalDate date = LocalDate.of(2026, 9, 10);
        List<AppointmentReportRow> rows = reportService.getDailyAppointmentSchedule(date);

        assertNotNull(rows);
        assertEquals(2, rows.size());
        assertEquals("APT-001", rows.get(0).getAppointmentNumber());
        assertEquals(LocalTime.of(9, 0), rows.get(0).getAppointmentTime());
    }

    @Test
    @DisplayName("2. Empty appointment report returns empty list")
    void testEmptyAppointmentReportReturnsEmptyList() throws SQLException {
        LocalDate date = LocalDate.of(2026, 9, 15);
        List<AppointmentReportRow> rows = reportService.getDailyAppointmentSchedule(date);

        assertNotNull(rows);
        assertTrue(rows.isEmpty());
    }

    @Test
    @DisplayName("3. Billing report returns rows for populated date")
    void testBillingReportReturnsRows() throws SQLException {
        LocalDate date = LocalDate.of(2026, 9, 10);
        List<BillingReportRow> rows = reportService.getDailyBillingSummary(date);

        assertNotNull(rows);
        assertEquals(2, rows.size());
        assertEquals("BILL-001", rows.get(0).getBillNumber());
        assertEquals(new BigDecimal("6000.00"), rows.get(0).getTotalAmount());
    }

    @Test
    @DisplayName("4. Daily billing total sums correctly using BigDecimal")
    void testDailyBillingTotalSumsCorrectlyUsingBigDecimal() throws SQLException {
        LocalDate date = LocalDate.of(2026, 9, 10);
        List<BillingReportRow> rows = reportService.getDailyBillingSummary(date);

        BigDecimal total = reportService.calculateDailyBillingTotal(rows);

        // 6000.00 + 2500.00 = 8500.00
        assertNotNull(total);
        assertEquals(new BigDecimal("8500.00"), total);
    }

    @Test
    @DisplayName("5. Null report date is rejected with IllegalArgumentException")
    void testNullDateRejected() {
        IllegalArgumentException ex1 = assertThrows(IllegalArgumentException.class, () ->
                reportService.getDailyAppointmentSchedule(null));
        assertEquals("Report date is required.", ex1.getMessage());

        IllegalArgumentException ex2 = assertThrows(IllegalArgumentException.class, () ->
                reportService.getDailyBillingSummary(null));
        assertEquals("Report date is required.", ex2.getMessage());
    }

    @Test
    @DisplayName("6. Empty billing report total is zero")
    void testEmptyBillingReportTotalIsZero() {
        BigDecimal total1 = reportService.calculateDailyBillingTotal(Collections.emptyList());
        assertEquals(BigDecimal.ZERO, total1);

        BigDecimal total2 = reportService.calculateDailyBillingTotal(null);
        assertEquals(BigDecimal.ZERO, total2);
    }
}