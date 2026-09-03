package com.sunrise.model;

import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * Appointment domain entity matching the appointments table.
 */
public class Appointment implements Serializable {

    private static final long serialVersionUID = 1L;

    private int appointmentId;
    private String appointmentNumber;
    private int patientId;
    private int dentistId;
    private int treatmentId;
    private LocalDate appointmentDate;
    private LocalTime appointmentTime;

    public Appointment() {
    }

    public Appointment(int appointmentId, String appointmentNumber, int patientId, int dentistId,
                       int treatmentId, LocalDate appointmentDate, LocalTime appointmentTime) {
        this.appointmentId = appointmentId;
        this.appointmentNumber = appointmentNumber;
        this.patientId = patientId;
        this.dentistId = dentistId;
        this.treatmentId = treatmentId;
        this.appointmentDate = appointmentDate;
        this.appointmentTime = appointmentTime;
    }

    public Appointment(String appointmentNumber, int patientId, int dentistId,
                       int treatmentId, LocalDate appointmentDate, LocalTime appointmentTime) {
        this.appointmentNumber = appointmentNumber;
        this.patientId = patientId;
        this.dentistId = dentistId;
        this.treatmentId = treatmentId;
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

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
    }

    public int getDentistId() {
        return dentistId;
    }

    public void setDentistId(int dentistId) {
        this.dentistId = dentistId;
    }

    public int getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(int treatmentId) {
        this.treatmentId = treatmentId;
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
        return "Appointment{" +
                "appointmentId=" + appointmentId +
                ", appointmentNumber='" + appointmentNumber + '\'' +
                ", patientId=" + patientId +
                ", dentistId=" + dentistId +
                ", treatmentId=" + treatmentId +
                ", appointmentDate=" + appointmentDate +
                ", appointmentTime=" + appointmentTime +
                '}';
    }
}