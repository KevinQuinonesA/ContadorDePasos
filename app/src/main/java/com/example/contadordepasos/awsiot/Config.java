package com.example.contadordepasos.awsiot;

import com.amazonaws.regions.Regions;

public class Config {

    public static String CUSTOMER_SPECIFIC_ENDPOINT = "a1lmh1knpirix7-ats.iot.us-east-2.amazonaws.com";
    public static final String COGNITO_POOL_ID = "us-east-2:53ab0e7c-a9ec-4e0b-a446-373b2f51e5c9";
    public static final Regions MY_REGION = Regions.US_EAST_2;

    String topic_subscribe = "thing_steps_subscribe/<pk>";
    String topic_public = "thing_steps_public/<pk>";

    String topic_step_sensor_publicar = "sensor/stepcounter/publicar";
    String topic_step_sensor_subscribe = "sensor/stepcounter/subscribir";

    String topic_acelerometer_publicar = "sensor/acelerometer/publicar";
    String topic_acelerometer_subscribe  = "sensor/acelerometer/subscribir";

}
