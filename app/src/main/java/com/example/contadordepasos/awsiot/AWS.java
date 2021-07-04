package com.example.contadordepasos.awsiot;

import android.content.Context;
import android.util.Log;
import android.view.View;

import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttManager;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttManager;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttQos;
import com.amazonaws.regions.Regions;

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;
import static com.example.contadordepasos.awsiot.AWSConnect.LOG_TAG;

public class AWS {

    private static final String CUSTOMER_SPECIFIC_ENDPOINT = "a1lmh1knpirix7-ats.iot.us-east-2.amazonaws.com";
    private static final String COGNITO_POOL_ID = "us-east-2:53ab0e7c-a9ec-4e0b-a446-373b2f51e5c9";
    private static final Regions MY_REGION = Regions.US_EAST_2;

    AWSIotMqttManager mqttManager;
    String clientId;
    CognitoCachingCredentialsProvider credentialsProvider;
    Context context;
    boolean enabled = true;
    String tvLastMessage = "";
    String topic_subscribe = "thing_steps_subscribe/<pk>";
    String topic_public = "thing_steps_public/<pk>";
    public AWS(Context context){
        context = context;
        setup();
    }
    private void setup(){
        clientId = UUID.randomUUID().toString();
        credentialsProvider = new CognitoCachingCredentialsProvider(
                context,
                COGNITO_POOL_ID,
                MY_REGION
        );
        mqttManager = new AWSIotMqttManager(clientId, CUSTOMER_SPECIFIC_ENDPOINT);
        new Thread(() -> runOnUiThread(() -> connect())).start();
    }

    public void connect(){
        enabled = true;
        try {
            mqttManager.connect(credentialsProvider, (status, throwable) -> {
                runOnUiThread(() -> {
                    if (status == AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus.Connecting) {
                        Log.i(LOG_TAG, "Connecting...");
                    } else if (status == AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus.Connected) {
                        Log.i(LOG_TAG, "Connected");
                    } else if (status == AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus.Reconnecting) {
                        Log.i(LOG_TAG, "Reconnecting");
                    } else if (status == AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus.ConnectionLost) {
                        if (throwable != null) {
                            throwable.printStackTrace();
                        }
                        Log.i(LOG_TAG, "Disconnected");
                    } else {
                        Log.i(LOG_TAG, "Disconnected");
                    }
                });
            });
        } catch (final Exception e) {
            Log.e(LOG_TAG, "Connection error.", e);
        }
    }

    public void subscribe(){
        final String topic = topic_subscribe;
        Log.d(LOG_TAG, "topic = " + topic);
        try {
            mqttManager.subscribeToTopic(topic, AWSIotMqttQos.QOS0,
                    (topic1, data) -> runOnUiThread(() -> {
                        String message = new String(data, StandardCharsets.UTF_8);
                        Log.d(LOG_TAG, "Message arrived:");
                        Log.d(LOG_TAG, "   Topic: " + topic1);
                        Log.d(LOG_TAG, " Message: " + message);
                        tvLastMessage = message;
                    }));
        } catch (Exception e) {
            Log.e(LOG_TAG, "Subscription error.", e);
        }
    }

    public void publicar(String message) {
           final String topic = topic_public;
           final String msg = message;
           try {
                mqttManager.publishString(msg, topic, AWSIotMqttQos.QOS0);
           } catch (Exception e) {
                Log.e(LOG_TAG, "Publish error.", e);
           }
    }

    public void disconned(){
           try {
                mqttManager.disconnect();
           } catch (Exception e) {
                Log.e(LOG_TAG, "Disconnect error.", e);
           }
    }

}
