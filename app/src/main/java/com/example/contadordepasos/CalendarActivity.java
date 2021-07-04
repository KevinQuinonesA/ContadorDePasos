package com.example.contadordepasos;

import android.content.Intent;
import android.os.Bundle;
import androidx.appcompat.app.AppCompatActivity;
import android.util.Log;
import android.widget.CalendarView;

public class CalendarActivity extends AppCompatActivity {

    private  static final String TAG = "CalendarActivity";
    private CalendarView mCalendarView;
    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_calendar);
        mCalendarView = findViewById(R.id.calendarView);
        mCalendarView.setOnDateChangeListener((CalendarView, year, month, dayOfMonth) -> {
            String date = year + "/" + month + "/"+ dayOfMonth ;
            Log.d(TAG, "onSelectedDayChange: yyyy/mm/dd:" + date);
            Intent intent = new Intent(CalendarActivity.this,HistorialActivity.class);
            intent.putExtra("date",date);
            startActivity(intent);

        });
    }
}

