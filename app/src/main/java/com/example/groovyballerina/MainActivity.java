package com.example.groovyballerina;

import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;

import android.widget.Spinner;
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
import java.util.Locale;
import java.util.Map;

public class MainActivity extends AppCompatActivity {

    private MqttManager mqttManager;
    private MqttClient mqttClient;
    private DataUpdater dataUpdater;
    private TextView textView1;
    //private TextView textView2;
    private ProgressBar progressBar;
    private TextView label1;
    private TextView label2;
    private static final long COUNTDOWN_TIME = 5 * 60 * 1000; // 5 minutes in milliseconds
    private static final long COUNTDOWN_INTERVAL = 1000; // 1 second interval
    private long timeLeft = COUNTDOWN_TIME;
    private CountDownTimer countDownTimer;
    private Button btnsenddata;
    private ImageView btnSearch;
    private Spinner spinnerOptions;


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
        progressBar = findViewById(R.id.progressBar);
        label1 = findViewById(R.id.label1);
        label2 = findViewById(R.id.label2);
        btnsenddata = findViewById(R.id.btnsenddata);
        btnSearch = findViewById(R.id.btnSearch);
        spinnerOptions = findViewById(R.id.spinnerOptions);


        // Configuración del Spinner
        String[] items = {"Hace 24 horas", "Hace 3 dias", "Hace 7 dias"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, items);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spinnerOptions.setAdapter(adapter);


        // Configuración del botón de búsqueda
        btnSearch.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String selectedRange = spinnerOptions.getSelectedItem().toString();

                Intent intent = new Intent(MainActivity.this, GraphActivity.class);
                intent.putExtra("time_Range", selectedRange);
                startActivity(intent);
            }
        });


        Map<String, TextView> textViews = new HashMap<>();
        textViews.put("lorawan/temp", textView1);


        dataUpdater = new DataUpdater(textViews, progressBar, this);
        mqttManager = new MqttManager(
                this,
                "tcp://10.3.141.1:8883", // Replace
                mqttClient.generateClientId(),// Replace
                dataUpdater);

        // 🚀 Inicializar e iniciar MQTT
        mqttManager.connect("ballerina-client", "ballerina1234@");

        // Suscribirse
        mqttManager.subscribe("lorawan/temp");
        mqttManager.subscribe("lorawan/temp/now");

        // Set a click listener for the publish button
        btnsenddata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mqttManager.publish("lorawan/temp/now", "leer temperatura");
            }
        });

        startCountdown();
   }
   @Override
    protected void onDestroy() {
        super.onDestroy();
        mqttManager.disconnect();
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
    }

    private void startCountdown() {
        if (countDownTimer != null) {
            countDownTimer.cancel();
        }
        countDownTimer = new CountDownTimer(timeLeft, COUNTDOWN_INTERVAL) {
            @Override
            public void onTick(long millisUntilFinished) {
                timeLeft = millisUntilFinished;
                updateCountdownText();
            }

            @Override
            public void onFinish() {
                // Handle the next reading
                label1.setText("Dato tomado hace: 5 mins");
                timeLeft = COUNTDOWN_TIME;
                updateCountdownText();
                startCountdown();
            }
        }.start();
    }


    public void resetCountdown(){
        timeLeft = COUNTDOWN_TIME;
        startCountdown();
        updateLastDataLabel();
    }

    public void updateCountdownText() {
        int minutes = (int) (timeLeft / 1000) / 60;
        int seconds = (int) (timeLeft / 1000) % 60;
        String timeLeftFormatted = String.format(Locale.getDefault(), "%02d:%02d", minutes, seconds);
        label2.setText("Nueva lectura en: " + minutes + "mins");
    }

    public void updateLastDataLabel(){
        label1.setText("Dato tomado hace: 0 mins");
    }
}
