package com.example.contadordepasos.awsiot;

import android.content.Context;
import android.util.Log;
import com.amazonaws.auth.CognitoCachingCredentialsProvider;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttManager;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttClientStatusCallback;
import com.amazonaws.mobileconnectors.iot.AWSIotMqttQos;
import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static com.amazonaws.mobile.auth.core.internal.util.ThreadUtils.runOnUiThread;

public class AWS {
    final String LOG_TAG = "AWS";
    AWSIotMqttManager mqttManager;
    String clientId;
    CognitoCachingCredentialsProvider credentialsProvider;
    Context context;
    boolean enabled = true;
    String tvLastMessage = "";
    String topic_subscribe = "thing_steps_subscribe/<pk>";
    String topic_public = "thing_steps_public/<pk>";
    public AWS(CognitoCachingCredentialsProvider credentialsProvider_){
        credentialsProvider = credentialsProvider_;
        setup();
    }
    private void setup(){
        clientId = UUID.randomUUID().toString();
        mqttManager = new AWSIotMqttManager(clientId, Config.CUSTOMER_SPECIFIC_ENDPOINT);
        new Thread(() -> runOnUiThread(() -> connect())).start();
    }

    public void connect(){
        try {
            mqttManager.connect(credentialsProvider, (status, throwable) -> {
                runOnUiThread(() -> {
                    if (status == AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus.Connecting) {
                        Log.i(LOG_TAG, "Connecting...");
                    } else if (status == AWSIotMqttClientStatusCallback.AWSIotMqttClientStatus.Connected) {
                        enabled = true;
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
        if(!enabled){
            return;
        }
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

    public void publicar(String message, String topic) {
        System.out.println("publicar...");
        if(!enabled){
            System.out.println("no publicar...");
            return;
        }
           final String msg = message;
           try {
               System.out.println("publicado");
                mqttManager.publishString(msg, topic, AWSIotMqttQos.QOS0);
           } catch (Exception e) {
                Log.e(LOG_TAG, "Publish error.", e);
           }
    }

    public void disconned(){
        if(!enabled){
            return;
        }
           try {
                mqttManager.disconnect();
           } catch (Exception e) {
                Log.e(LOG_TAG, "Disconnect error.", e);
           }
    }

}
