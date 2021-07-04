package com.example.contadordepasos.models;

public abstract class SensorUse {
    private String type_sensor;

    public SensorUse(String type_sensor) {
        this.type_sensor = type_sensor;
    }

    public String getType_sensor() {
        return type_sensor;
    }

    public void setType_sensor(String type_sensor) {
        this.type_sensor = type_sensor;
    }
}
