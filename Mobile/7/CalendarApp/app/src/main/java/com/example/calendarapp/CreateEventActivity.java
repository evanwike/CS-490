package com.example.calendarapp;

import android.content.Intent;
import android.provider.CalendarContract;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.Locale;

public class CreateEventActivity extends AppCompatActivity implements TimePickerFragment.TimeSelectedListener {
    EditText editTitle, editDesc, editLocation;
    TextView startTimeView, endTimeView;
    CheckBox chkAllDay;
    LinearLayout createLayout, timeSelect;
    Intent intent;
    int year, month, day, timeSelectBtnId;
    Date selectedDate;
    long startTime, endTime;
    boolean allDayEvent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        editTitle = findViewById(R.id.eventTitle);
        editDesc = findViewById(R.id.eventDesc);
        startTimeView = findViewById(R.id.startTimeView);
        endTimeView = findViewById(R.id.endTimeView);
        editLocation = findViewById(R.id.eventLocation);
        chkAllDay = findViewById(R.id.chkAllDay);
        createLayout = findViewById(R.id.createLayout);     // Parent view
        timeSelect = findViewById(R.id.timeSelect);         // Layout containing time select views

        intent = getIntent();
        year = intent.getIntExtra("year",0);
        month = intent.getIntExtra("month",0);
        day = intent.getIntExtra("day",0);
        selectedDate = new GregorianCalendar(year, month, day).getTime();
        startTime = 0;
        endTime = 0;
        
        // Remove time select buttons when "All Day Event" checkbox is checked
        chkAllDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    createLayout.removeView(timeSelect);
                    allDayEvent = true;
                }
                else {
                    createLayout.addView(timeSelect, 3);
                    allDayEvent = false;
                }
            }
        });
    }

    // Go back to MainActivity
    public void back(View v) {
        Intent intent = new Intent(CreateEventActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // Creates new Calendar Events intent (Google Calendar app) and passes 'form' data as extras
    public void createEvent(View v) {
        if (!allDayEvent && (startTime == 0 || endTime == 0)) {
            Toast.makeText(this, "Select a start and end time for the event", Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI);

        intent.putExtra(CalendarContract.Events.TITLE, editTitle.getText().toString());
        intent.putExtra(CalendarContract.Events.DESCRIPTION, editDesc.getText().toString());
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, editLocation.getText().toString());

        if (allDayEvent) {
            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, selectedDate.getTime());
            intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);
        }
        else {
            intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, startTime);
            intent.putExtra(CalendarContract.EXTRA_EVENT_END_TIME, endTime);
            intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, false);
        }

        startActivity(intent);
        finish();
    }

    // Interface method from TimePickerFragment
    // Sets start/end time and its corresponding text view after time selected in dialog
    @Override
    public void onTimeSelected(int hours, int mins) {
        final Date date = new GregorianCalendar(year, month, day, hours, mins).getTime();
        final DateFormat df = new SimpleDateFormat("h:mm a", Locale.US);

        switch (timeSelectBtnId) {
            case R.id.btnStartTime: {
                startTime = date.getTime();
                startTimeView.setText(df.format(date));
            } break;
            case R.id.btnEndTime: {
                endTime = date.getTime();
                endTimeView.setText(df.format(date));
            } break;
        }
    }

    // Displays TimePicker Dialog when either start time or end time buttons are pressed
    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        timeSelectBtnId = v.getId();

        newFragment.show(getSupportFragmentManager(), "timePicker");
    }
}