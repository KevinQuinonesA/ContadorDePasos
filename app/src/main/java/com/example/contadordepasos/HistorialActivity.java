package com.example.contadordepasos;


import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.AnimationDrawable;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.TextView;

import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import android.widget.Button;
import android.widget.TextView;

public class HistorialActivity extends AppCompatActivity  {
    private TextView thedate;
    private Button btngocalendar;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_debug);
        thedate = (TextView) findViewById(R.id.date);
        btngocalendar = (Button) findViewById(R.id.btngocalendar);

        Intent incoming = getIntent();
        String date = incoming.getStringExtra("date");
        thedate.setText(date);

        btngocalendar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HistorialActivity.this,CalendarActivity.class);
                startActivity(intent);
            }
        });
    }

}




