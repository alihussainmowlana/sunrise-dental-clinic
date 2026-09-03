package com.sunrise.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

/**
 * Domain projection model representing complete bill and appointment receipt details
 * for displaying and printing client receipts.
 */
public class BillDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    private String billNumber;
    private String appointmentNumber;
    private String patientName;
    private String contactNumber;
    private String dentistName;
    private String treatmentName;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;
    private BigDecimal consultationFee;
    private BigDecimal treatmentCost;
    private BigDecimal totalAmount;
    private LocalDateTime generatedAt;

    public BillDetails() {
    }

    public BillDetails(String billNumber, String appointmentNumber, String patientName,
                       String contactNumber, String dentistName, String treatmentName,
                       LocalDate appointmentDate, LocalTime appointmentTime,
                       BigDecimal consultationFee, BigDecimal treatmentCost,
                       BigDecimal totalAmount, LocalDateTime generatedAt) {
        this.billNumber = billNumber;
        this.appointmentNumber = appointmentNumber;
        this.patientName = patientName;
        this.contactNumber = contactNumber;
        this.dentistName = dentistName;
        this.treatmentName = treatmentName;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
        this.consultationFee = consultationFee;
        this.treatmentCost = treatmentCost;
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

    public String getContactNumber() {
        return contactNumber;
    }

    public void setContactNumber(String contactNumber) {
        this.contactNumber = contactNumber;
    }

    public String getDentistName() {
        return dentistName;
    }

    public void setDentistName(String dentistName) {
        this.dentistName = dentistName;
    }

    public String getTreatmentName() {
        return treatmentName;
    }

    public void setTreatmentName(String treatmentName) {
        this.treatmentName = treatmentName;
    }

    public LocalDate getAppointmentDate() {
        return appointmentDate;
    }

    public void setAppointmentDate(LocalDate appointmentDate) {
        this.appointmentDate = appointmentDate;
    }

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalTime appointmentTime) {
        this.appointmentTime = appointmentTime;
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
        return "BillDetails{" +
                "billNumber='" + billNumber + '\'' +
                ", appointmentNumber='" + appointmentNumber + '\'' +
                ", patientName='" + patientName + '\'' +
                ", totalAmount=" + totalAmount +
                ", generatedAt=" + generatedAt +
                '}';
    }
}