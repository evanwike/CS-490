package com.example.calendarapp;

import android.content.Intent;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.TextView;
import java.text.DateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity {
    TextView dateView;
    CalendarView calendarView;
    Button btnCreateEvent;
    int YEAR, MONTH, DAY;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dateView = findViewById(R.id.dateView);
        calendarView = findViewById(R.id.calendarView);
        btnCreateEvent = findViewById(R.id.btnCreateEvent);

        // Set calendar to current date
        calendarView.setDate(Calendar.getInstance().getTimeInMillis());

        // Set initial date string in dateView
        final DateFormat df = DateFormat.getDateInstance(DateFormat.LONG);
        dateView.setText(df.format(calendarView.getDate()));

        // Change date string in dateView when user selects a new date
        calendarView.setOnDateChangeListener(new CalendarView.OnDateChangeListener() {
            @Override
            public void onSelectedDayChange(@NonNull CalendarView view, int year, int month, int dayOfMonth) {
                // Create new date object from parameters for long date format
                Date date = new GregorianCalendar(year, month, dayOfMonth).getTime();
                YEAR = year;
                MONTH = month;
                DAY = dayOfMonth;

                dateView.setText(df.format(date));
            }
        });

        // Starts CreateEventActivity on btnCreateEvent click, passes selected date as extra
        btnCreateEvent.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, CreateEventActivity.class);
                intent.putExtra("year", YEAR);
                intent.putExtra("month", MONTH);
                intent.putExtra("day", DAY);
//                intent.putExtra("date", calendarView.getDate());

                startActivity(intent);
                finish();
            }
        });
    }
}
