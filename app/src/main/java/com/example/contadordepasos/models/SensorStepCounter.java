package com.example.contadordepasos.models;

public class SensorStepCounter extends SensorUse{
    private Float stepcount;

    public SensorStepCounter(String type_sensor) {
        super(type_sensor);
    }

    public SensorStepCounter(String type_sensor, Float stepcount) {
        super(type_sensor);
        this.stepcount = stepcount;
    }

    public Float getStepcount() {
        return stepcount;
    }

    public void setStepcount(Float stepcount) {
        this.stepcount = stepcount;
    }
}
