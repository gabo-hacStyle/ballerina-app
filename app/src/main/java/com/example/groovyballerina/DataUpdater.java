package com.example.groovyballerina;

import android.util.Log;
import android.widget.TextView;

import com.example.groovyballerina.mqtt.MqttDataListener;

import java.util.Map;

public class DataUpdater implements MqttDataListener {

    private Map<String, TextView> textViews;
    public DataUpdater(Map<String, TextView> textViews) {
        this.textViews = textViews;
    }

    @Override
    public void onDataReceived(String topic, Float data) {

        TextView textView = textViews.get(topic);
        if (textView != null) {
            textView.setText(String.valueOf(data));
        }
    }


}
