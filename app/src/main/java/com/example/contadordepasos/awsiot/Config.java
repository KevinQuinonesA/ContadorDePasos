package com.example.contadordepasos.awsiot;

import com.amazonaws.regions.Regions;

public class Config {

    public static String CUSTOMER_SPECIFIC_ENDPOINT = "a1lmh1knpirix7-ats.iot.us-east-2.amazonaws.com";
    public static final String COGNITO_POOL_ID = "us-east-2:53ab0e7c-a9ec-4e0b-a446-373b2f51e5c9";
    public static final Regions MY_REGION = Regions.US_EAST_2;

    public static String topic_step_sensor_publicar = "sensorstepcounterpublicar";
    public static String topic_step_sensor_subscribe = "sensorstepcountersubscribir";

    public static String topic_acelerometer_publicar = "sensoracelerometerpublicar";
    public static String topic_acelerometer_subscribe  = "sensoracelerometersubscribir";

}
