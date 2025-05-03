package com.example.groovyballerina;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.example.groovyballerina.mqtt.MqttDataListener;
import com.example.groovyballerina.mqtt.MqttManager;

import org.eclipse.paho.client.mqttv3.MqttClient;

import java.util.HashMap;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private MqttManager mqttManager;
    private MqttClient mqttClient;
    private DataUpdater dataUpdater;
    private TextView textView1;
    //private TextView textView2;

    TextView lblconectado;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
        textView1 = findViewById(R.id.textFloat);




        Map<String, TextView> textViews = new HashMap<>();
        textViews.put("lorawan/temp", textView1);


        dataUpdater = new DataUpdater(textViews);
        mqttManager = new MqttManager(
                this,
                "tcp://10.3.141.1:8883", // Replace
                mqttClient.generateClientId(),// Replace
                dataUpdater);

        // 🚀 Inicializar e iniciar MQTT
        mqttManager.connect("ballerina-client", "ballerina1234@");

        // Suscribirse y publicar ejemplo (puedes cambiar el topic y el mensaje)
        mqttManager.subscribe("lorawan/temp");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        mqttManager.disconnect();
    }
}
