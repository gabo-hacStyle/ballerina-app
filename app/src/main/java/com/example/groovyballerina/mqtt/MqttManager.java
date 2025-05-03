package com.example.groovyballerina.mqtt;

import android.content.Context;
import android.util.Log;


import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

public class MqttManager {

    private static final String TAG = "MqttManager";
    private MqttClient mqttClient;
    private MqttDataListener listener;


    public MqttManager(Context context, String serverUri, String clientId, MqttDataListener listener) {

        this.listener = listener;
        try {
            MemoryPersistence persistence = new MemoryPersistence();
            mqttClient = new MqttClient(serverUri, clientId, persistence);
        } catch (MqttException e) {
            Log.e(TAG, "Error creating MqttClient: " + e.getMessage());
            e.printStackTrace();
        }
    }

    public void connect(String username, String pass) {
        if (mqttClient == null) {
            Log.e(TAG, "MqttClient is null. Cannot connect.");
            return;
        }
        try {
            MqttConnectOptions mqttConnectOptions = new MqttConnectOptions();
            mqttConnectOptions.setAutomaticReconnect(true);
            mqttConnectOptions.setCleanSession(false);
            mqttConnectOptions.setUserName(username);
            mqttConnectOptions.setPassword(pass.toCharArray());

            mqttClient.connect(mqttConnectOptions);
            Log.d(TAG, "Connection success");


        } catch (MqttException e) {
        Log.d(TAG, "Connection failure: " + e.getMessage());
        e.printStackTrace();
    }
    }

    public void subscribe(String topic, int qos) {
        if (mqttClient == null) {
            Log.e(TAG, "MqttClient is null. Cannot subscribe.");
            return;
        }
        try {
            mqttClient.subscribe(topic, qos);
            Log.d(TAG, "Subscribed to topic: " + topic);

            mqttClient.setCallback(new MqttCallback() {
                @Override
                public void connectionLost(Throwable cause) {
                    Log.d(TAG, "Connection lost " + cause.getMessage());
                }

                @Override
                public void messageArrived(String topic, MqttMessage message) {
                    String payload = new String(message.getPayload());
                    if (payload != null) {
                        try {
                            float floatValue = Float.parseFloat(payload);
                            listener.onDataReceived(topic, floatValue);
                        } catch (Exception e) {
                            Log.d(TAG, "Invalid message format: " + e.getMessage());
                        }
                    }
                }

                @Override
                public void deliveryComplete(IMqttDeliveryToken token) {
                    Log.d(TAG, "Delivery complete");
                }
            });
        } catch (MqttException e) {
            Log.d(TAG, "Subscription exception: " + e.getMessage());
            e.printStackTrace();
        }




    }

    public void disconnect() {
        if (mqttClient == null) {
            Log.e(TAG, "MqttClient is null. Cannot disconnect.");
            return;
        }
        try {
            mqttClient.disconnect();
            Log.d(TAG, "Disconnected");
        } catch (MqttException e) {
            Log.d(TAG, "Disconnect exception: " + e.getMessage());
            e.printStackTrace();
        }
    }
    public void subscribe(String topic){
        subscribe(topic,1);
    }
}
