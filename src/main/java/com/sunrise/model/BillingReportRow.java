package com.sunrise.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Projection model representing a single bill row in the Daily Billing Summary report.
 */
public class BillingReportRow implements Serializable {

    private static final long serialVersionUID = 1L;

    private String billNumber;
    private String appointmentNumber;
    private String patientName;
    private String treatmentName;
    private BigDecimal treatmentCost;
    private BigDecimal consultationFee;
    private BigDecimal totalAmount;
    private LocalDateTime generatedAt;

    public BillingReportRow() {
    }

    public BillingReportRow(String billNumber, String appointmentNumber, String patientName,
                            String treatmentName, BigDecimal treatmentCost, BigDecimal consultationFee,
                            BigDecimal totalAmount, LocalDateTime generatedAt) {
        this.billNumber = billNumber;
        this.appointmentNumber = appointmentNumber;
        this.patientName = patientName;
        this.treatmentName = treatmentName;
        this.treatmentCost = treatmentCost;
        this.consultationFee = consultationFee;
        this.totalAmount = totalAmount;
        this.generatedAt = generatedAt;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public String getAppointmentNumber() {
        return appointmentNumber;
    }

    public void setAppointmentNumber(String appointmentNumber) {
        this.appointmentNumber = appointmentNumber;
    }

    public String getPatientName() {
        return patientName;
    }

    public void setPatientName(String patientName) {
        this.patientName = patientName;
    }

    public String getTreatmentName() {
        return treatmentName;
    }

    public void setTreatmentName(String treatmentName) {
        this.treatmentName = treatmentName;
    }

    public BigDecimal getTreatmentCost() {
        return treatmentCost;
    }

    public void setTreatmentCost(BigDecimal treatmentCost) {
        this.treatmentCost = treatmentCost;
    }

    public BigDecimal getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(BigDecimal consultationFee) {
        this.consultationFee = consultationFee;
    }

    public BigDecimal getTotalAmount() {
        return totalAmount;
    }

    public void setTotalAmount(BigDecimal totalAmount) {
        this.totalAmount = totalAmount;
    }

    public LocalDateTime getGeneratedAt() {
        return generatedAt;
    }

    public void setGeneratedAt(LocalDateTime generatedAt) {
        this.generatedAt = generatedAt;
    }

    @Override
    public String toString() {
        return "BillingReportRow{" +
                "billNumber='" + billNumber + '\'' +
                ", appointmentNumber='" + appointmentNumber + '\'' +
                ", patientName='" + patientName + '\'' +
                ", totalAmount=" + totalAmount +
                ", generatedAt=" + generatedAt +
                '}';
    }
}