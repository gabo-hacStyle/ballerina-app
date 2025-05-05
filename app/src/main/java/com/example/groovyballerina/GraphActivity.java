package com.example.groovyballerina;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;

public class GraphActivity extends AppCompatActivity {

    private Button btnRegresar;

    private LineChart oxygenChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.graph);

        oxygenChart = findViewById(R.id.oxygenChart);
        btnRegresar = findViewById(R.id.btnregresar);


        btnRegresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        // Simulacion de datos
        ArrayList<Entry> entries = new ArrayList<>();
        entries.add(new Entry(0, 6.2f));
        entries.add(new Entry(1, 6.4f));
        entries.add(new Entry(2, 6.8f));
        entries.add(new Entry(3, 7.0f));
        entries.add(new Entry(4, 6.5f));

        LineDataSet dataSet = new LineDataSet(entries, "Niveles de oxígeno (mg/L)");
        dataSet.setLineWidth(2);
        dataSet.setCircleRadius(4);
        dataSet.setValueTextSize(12f);

        LineData lineData = new LineData(dataSet);
        oxygenChart.setData(lineData);

        // Eje X
        XAxis xAxis = oxygenChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        oxygenChart.getDescription().setText("Histórico");
        oxygenChart.invalidate();
    }
}
