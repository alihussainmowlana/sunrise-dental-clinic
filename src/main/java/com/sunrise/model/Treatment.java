package com.sunrise.model;

import java.io.Serializable;
import java.math.BigDecimal;

/**
 * Treatment reference domain entity matching the treatments table.
 */
public class Treatment implements Serializable {

    private static final long serialVersionUID = 1L;

    private int treatmentId;
    private String treatmentName;
    private BigDecimal treatmentCost;

    public Treatment() {
    }

    public Treatment(int treatmentId, String treatmentName, BigDecimal treatmentCost) {
        this.treatmentId = treatmentId;
        this.treatmentName = treatmentName;
        this.treatmentCost = treatmentCost;
    }

    public int getTreatmentId() {
        return treatmentId;
    }

    public void setTreatmentId(int treatmentId) {
        this.treatmentId = treatmentId;
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

    @Override
    public String toString() {
        return "Treatment{" +
                "treatmentId=" + treatmentId +
                ", treatmentName='" + treatmentName + '\'' +
                ", treatmentCost=" + treatmentCost +
                '}';
    }
}