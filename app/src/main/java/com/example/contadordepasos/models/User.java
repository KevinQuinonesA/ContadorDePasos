package com.example.contadordepasos.models;

import java.util.Date;

public class User {

    private String name;
    private String last_name;
    private int age;
    private String date_party;
    private float weight;

    public User(String name, String last_name,  String date_party, int age, float weight) {
        this.name = name;
        this.last_name = last_name;
        this.age = age;
        this.date_party = date_party;
        this.weight = weight;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLast_name() {
        return last_name;
    }

    public void setLast_name(String last_name) {
        this.last_name = last_name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public String getDate_party() {
        return date_party;
    }

    public void setDate_party(String date_party) {
        this.date_party = date_party;
    }

    public float getWeight() {
        return weight;
    }

    public void setWeight(float weight) {
        this.weight = weight;
    }
}
