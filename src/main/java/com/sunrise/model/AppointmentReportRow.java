package com.sunrise.model;

import java.io.Serializable;
import java.time.LocalTime;

/**
 * Projection model representing a single appointment row in the Daily Appointment Schedule report.
 */
public class AppointmentReportRow implements Serializable {

    private static final long serialVersionUID = 1L;

    private String appointmentNumber;
    private String patientName;
    private String contactNumber;
    private String dentistName;
    private String treatmentName;
    private LocalTime appointmentTime;

    public AppointmentReportRow() {
    }

    public AppointmentReportRow(String appointmentNumber, String patientName, String contactNumber,
                                String dentistName, String treatmentName, LocalTime appointmentTime) {
        this.appointmentNumber = appointmentNumber;
        this.patientName = patientName;
        this.contactNumber = contactNumber;
        this.dentistName = dentistName;
        this.treatmentName = treatmentName;
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

    public LocalTime getAppointmentTime() {
        return appointmentTime;
    }

    public void setAppointmentTime(LocalTime appointmentTime) {
        this.appointmentTime = appointmentTime;
    }

    @Override
    public String toString() {
        return "AppointmentReportRow{" +
                "appointmentTime=" + appointmentTime +
                ", appointmentNumber='" + appointmentNumber + '\'' +
                ", patientName='" + patientName + '\'' +
                ", dentistName='" + dentistName + '\'' +
                ", treatmentName='" + treatmentName + '\'' +
                '}';
    }
}