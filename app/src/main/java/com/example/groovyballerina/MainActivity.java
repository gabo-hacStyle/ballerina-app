package com.example.groovyballerina;

import android.os.Bundle;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private MQTTManager mqttManager;

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


        // 🚀 Inicializar e iniciar MQTT
        mqttManager = new MQTTManager(this);
        mqttManager.connect();

        // Suscribirse y publicar ejemplo (puedes cambiar el topic y el mensaje)
        mqttManager.subscribe("lorawan/temp");
        mqttManager.publish("test/topic", "¡Hola desde GroovyBallerina!");
    }
}
