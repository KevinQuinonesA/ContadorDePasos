package com.example.contadordepasos.models;
import java.util.Date;

public class DataSteps {

    private String type;
    private String timestep;
    private float step_total;
    private SensorUse sensor;

    public DataSteps(String type, String timestep, float step_total, SensorUse sensor) {
        this.type = type;
        this.timestep = timestep;
        this.step_total = step_total;
        this.sensor = sensor;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTimestep() {
        return timestep;
    }

    public void setTimestep(String timestep) {
        this.timestep = timestep;
    }

    public float getStep_total() {
        return step_total;
    }

    public void setStep_total(float step_total) {
        this.step_total = step_total;
    }

    public SensorUse getSensor() {
        return sensor;
    }

    public void setSensor(SensorUse sensor) {
        this.sensor = sensor;
    }
}
