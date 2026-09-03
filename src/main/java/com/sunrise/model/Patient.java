package com.sunrise.model;

import java.io.Serializable;

/**
 * Patient domain entity matching the database patients table.
 */
public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;

    private int patientId;
    private String patientName;
    private String address;
    private String contactNumber;

    public Patient() {
    }

    public Patient(int patientId, String patientName, String address, String contactNumber) {
        this.patientId = patientId;
        this.patientName = patientName;
        this.address = address;
        this.contactNumber = contactNumber;
    }

    public Patient(String patientName, String address, String contactNumber) {
        this.patientName = patientName;
        this.address = address;
        this.contactNumber = contactNumber;
    }

    public int getPatientId() {
        return patientId;
    }

    public void setPatientId(int patientId) {
        this.patientId = patientId;
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

    @Override
    public String toString() {
        return "Patient{" +
                "patientId=" + patientId +
                ", patientName='" + patientName + '\'' +
                ", contactNumber='" + contactNumber + '\'' +
                '}';
    }
}