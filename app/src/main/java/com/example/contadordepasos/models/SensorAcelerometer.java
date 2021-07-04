package com.example.contadordepasos.models;

public class SensorAcelerometer extends SensorUse{
    private Float x;
    private Float y;
    private Float z;

    public SensorAcelerometer(String type) {
        super(type);
    }

    public SensorAcelerometer(String type, Float x, Float y, Float z) {
        super(type);
        this.x = x;
        this.y = y;
        this.z = z;
    }

    public Float getX() {
        return x;
    }

    public void setX(Float x) {
        this.x = x;
    }

    public Float getY() {
        return y;
    }

    public void setY(Float y) {
        this.y = y;
    }

    public Float getZ() {
        return z;
    }

    public void setZ(Float z) {
        this.z = z;
    }
}
