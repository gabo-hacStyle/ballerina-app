package com.example.groovyballerina;

import android.util.Log;
import android.widget.TextView;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MQTTManager {
    private MqttClient client;

    public MQTTManager(MainActivity mainActivity) {
    }
    TextView lblconectado;

    public void connect() {
        try {
            client = new MqttClient("tcp://10.3.141.1:8883", MqttClient.generateClientId(), null);
            MqttConnectOptions options = new MqttConnectOptions();
            options.setUserName("ballerina-client");
            options.setPassword("ballerina1234@".toCharArray());
            client.connect(options);
            System.out.println("Conectado al broker MQTT");
            Log.d("Conectado","Se ha Conectado Broker");
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void subscribe(String topic) {
        try {
            client.subscribe(topic, (t, message) -> {
                System.out.println("Mensaje recibido: " + new String(message.getPayload()));
                Log.d("Mensaje Recibido:",""+ new String(message.getPayload()));
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    public void publish(String topic, String payload) {
        try {
            MqttMessage message = new MqttMessage(payload.getBytes());
            client.publish(topic, message);
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
