package com.sunrise.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Data transfer model representing appointment information and treatment cost
 * needed for calculating and generating a bill.
 */
public class AppointmentBillingInfo implements Serializable {

    private static final long serialVersionUID = 1L;

    private int appointmentId;
    private String appointmentNumber;
    private String patientName;
    private String contactNumber;
    private String dentistName;
    private String treatmentName;
    private BigDecimal treatmentCost;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;

    public AppointmentBillingInfo() {
    }

    public AppointmentBillingInfo(int appointmentId, String appointmentNumber, String patientName,
                                  String contactNumber, String dentistName, String treatmentName,
                                  BigDecimal treatmentCost, LocalDate appointmentDate, LocalTime appointmentTime) {
        this.appointmentId = appointmentId;
        this.appointmentNumber = appointmentNumber;
        this.patientName = patientName;
        this.contactNumber = contactNumber;
        this.dentistName = dentistName;
        this.treatmentName = treatmentName;
        this.treatmentCost = treatmentCost;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(int appointmentId) {
        this.appointmentId = appointmentId;
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

    public BigDecimal getTreatmentCost() {
        return treatmentCost;
    }

    public void setTreatmentCost(BigDecimal treatmentCost) {
        this.treatmentCost = treatmentCost;
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
}