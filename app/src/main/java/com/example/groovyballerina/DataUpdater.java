package com.example.groovyballerina;

import android.graphics.Color;
import android.graphics.PorterDuff;
import android.util.Log;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.example.groovyballerina.mqtt.MqttDataListener;

import java.util.Map;

public class DataUpdater implements MqttDataListener {

    private Map<String, TextView> textViews;

    private ProgressBar progressBar;
    // Define the thresholds
    private final float THRESHOLD_RED = 10.0f;
    private final float THRESHOLD_YELLOW = 20.0f;
    private final float MAX_VALUE = 30.0f;
    private MainActivity mainActivity;

    public DataUpdater(Map<String, TextView> textViews, ProgressBar progressBar, MainActivity mainActivity) {
        this.textViews = textViews;
        this.progressBar = progressBar;
        this.mainActivity = mainActivity;
    }

    @Override
    public void onDataReceived(String topic, Float data) {
        Log.d("DataUpdater", "Received data: " + data + " from topic: " + topic);
        TextView textView = textViews.get(topic);
        if (textView != null) {
            textView.setText(String.valueOf(data));
        }


        mainActivity.resetCountdown();

        updateGauge(data);

    }

    private void updateGauge(float data) {
        // Calculate the percentage
        float percentage = (data / MAX_VALUE) * 100.0f;
        if (percentage<0){
            percentage=0;
        }
        if (percentage>100){
            percentage=100;
        }
        // Update the progress bar
        progressBar.setProgress((int) percentage);
        // Update the color
        updateGaugeColor(percentage);
    }

    private void updateGaugeColor(float percentage) {
        int color;
        if (percentage < THRESHOLD_RED) {
            color = Color.RED;
        } else if (percentage < THRESHOLD_YELLOW) {
            color = Color.YELLOW;
        } else {
            color = Color.GREEN;
        }
        progressBar.getProgressDrawable().setColorFilter(color, PorterDuff.Mode.SRC_IN);
    }

}
