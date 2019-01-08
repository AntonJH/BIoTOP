package com.example.anton.biotop;

import java.io.Serializable;

public class Animal implements Serializable {
    String id, type, healthStatus, bodyTemperature, pulse, bloodPressureSystolic, bloodPressureDiastolic, healthDesc;

    public Animal(String id, String type, String healthStatus, String bodyTemperature, String pulse, String bloodPressureSystolic, String bloodPressureDiastolic) {
        this.id = id;
        this.type = type;
        this.healthStatus = healthStatus;
        this.bodyTemperature = bodyTemperature;
        this.pulse = pulse;
        this.bloodPressureSystolic = bloodPressureSystolic;
        this.bloodPressureDiastolic = bloodPressureDiastolic;
    }

    public String getID() {
        return id;
    }

    public String getType() {
        return type;
    }

    public String getHealthStatus() {
        return healthStatus;
    }

    public void setHealthStatus(String healthStatus) {
        this.healthStatus = healthStatus;
    }

    public String getBodyTemperature() {
        return bodyTemperature;
    }

    public void setBodyTemperature(String bodyTemperature) {
        this.bodyTemperature = bodyTemperature;
    }

    public String getPulse() {
        return pulse;
    }

    public void setPulse(String pulse) {
        this.pulse = pulse;
    }

    public String getBloodPressureSystolic() {
        return bloodPressureSystolic;
    }

    public void setBloodPressureSystolic(String bloodPressureSystolic) {
        this.bloodPressureSystolic = bloodPressureSystolic;
    }

    public String getBloodPressureDiastolic() {
        return bloodPressureDiastolic;
    }

    public void setBloodPressureDiastolic(String bloodPressureDiastolic) {
        this.bloodPressureDiastolic = bloodPressureDiastolic;
    }

    public String getHealthDesc() {
        return healthDesc;
    }

    public void setHealthDesc(String healthDesc) {
        this.healthDesc = healthDesc;
    }
}