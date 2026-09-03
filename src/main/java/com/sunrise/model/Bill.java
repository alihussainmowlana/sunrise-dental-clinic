package com.sunrise.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDateTime;

/**
 * Domain entity matching the database bills table.
 */
public class Bill implements Serializable {

    private static final long serialVersionUID = 1L;

    private int billId;
    private String billNumber;
    private int appointmentId;
    private BigDecimal consultationFee;
    private BigDecimal treatmentCost;
    private BigDecimal totalAmount;
    private LocalDateTime generatedAt;

    public Bill() {
    }

    public Bill(int billId, String billNumber, int appointmentId,
                BigDecimal consultationFee, BigDecimal treatmentCost,
                BigDecimal totalAmount, LocalDateTime generatedAt) {
        this.billId = billId;
        this.billNumber = billNumber;
        this.appointmentId = appointmentId;
        this.consultationFee = consultationFee;
        this.treatmentCost = treatmentCost;
        this.totalAmount = totalAmount;
        this.generatedAt = generatedAt;
    }

    public Bill(String billNumber, int appointmentId,
                BigDecimal consultationFee, BigDecimal treatmentCost,
                BigDecimal totalAmount) {
        this.billNumber = billNumber;
        this.appointmentId = appointmentId;
        this.consultationFee = consultationFee;
        this.treatmentCost = treatmentCost;
        this.totalAmount = totalAmount;
    }

    public int getBillId() {
        return billId;
    }

    public void setBillId(int billId) {
        this.billId = billId;
    }

    public String getBillNumber() {
        return billNumber;
    }

    public void setBillNumber(String billNumber) {
        this.billNumber = billNumber;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
    }

    public BigDecimal getConsultationFee() {
        return consultationFee;
    }

    public void setConsultationFee(BigDecimal consultationFee) {
        this.consultationFee = consultationFee;
    }

    public BigDecimal getTreatmentCost() {
        return treatmentCost;
    }

    public void setTreatmentCost(BigDecimal treatmentCost) {
        this.treatmentCost = treatmentCost;
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
        return "Bill{" +
                "billId=" + billId +
                ", billNumber='" + billNumber + '\'' +
                ", appointmentId=" + appointmentId +
                ", totalAmount=" + totalAmount +
                ", generatedAt=" + generatedAt +
                '}';
    }
}