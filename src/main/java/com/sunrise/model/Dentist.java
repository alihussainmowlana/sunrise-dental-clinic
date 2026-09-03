package com.sunrise.model;

import java.io.Serializable;

/**
 * Dentist reference domain entity matching the dentists table.
 */
public class Dentist implements Serializable {

    private static final long serialVersionUID = 1L;

    private int dentistId;
    private String dentistName;

    public Dentist() {
    }

    public Dentist(int dentistId, String dentistName) {
        this.dentistId = dentistId;
        this.dentistName = dentistName;
    }

    public int getDentistId() {
        return dentistId;
    }

    public void setDentistId(int dentistId) {
        this.dentistId = dentistId;
    }

    public String getDentistName() {
        return dentistName;
    }

    public void setDentistName(String dentistName) {
        this.dentistName = dentistName;
    }

    @Override
    public String toString() {
        return "Dentist{" +
                "dentistId=" + dentistId +
                ", dentistName='" + dentistName + '\'' +
                '}';
    }
}