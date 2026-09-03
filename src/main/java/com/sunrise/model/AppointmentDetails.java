package com.sunrise.model;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Domain projection model representing complete appointment and patient details
 * retrieved from joined database tables for search and display.
 */
public class AppointmentDetails implements Serializable {

    private static final long serialVersionUID = 1L;

    private String appointmentNumber;
    private String patientName;
    private String address;
    private String contactNumber;
    private String dentistName;
    private String treatmentName;
    private BigDecimal treatmentCost;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;

    public AppointmentDetails() {
    }

    public AppointmentDetails(String appointmentNumber, String patientName, String address,
                              String contactNumber, String dentistName, String treatmentName,
                              BigDecimal treatmentCost, LocalDate appointmentDate, LocalTime appointmentTime) {
        this.appointmentNumber = appointmentNumber;
        this.patientName = patientName;
        this.address = address;
        this.contactNumber = contactNumber;
        this.dentistName = dentistName;
        this.treatmentName = treatmentName;
        this.treatmentCost = treatmentCost;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
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

    @Override
    public String toString() {
        return "AppointmentDetails{" +
                "appointmentNumber='" + appointmentNumber + '\'' +
                ", patientName='" + patientName + '\'' +
                ", dentistName='" + dentistName + '\'' +
                ", treatmentName='" + treatmentName + '\'' +
                ", treatmentCost=" + treatmentCost +
                ", appointmentDate=" + appointmentDate +
                ", appointmentTime=" + appointmentTime +
                '}';
    }
}