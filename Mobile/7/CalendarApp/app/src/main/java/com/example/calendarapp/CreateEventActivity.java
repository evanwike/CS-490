package com.example.calendarapp;

import android.app.Dialog;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.provider.CalendarContract;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TimePicker;
import java.util.Calendar;

public class CreateEventActivity extends AppCompatActivity {
    EditText editTitle, editDesc, editLocation;
    CheckBox chkAllDay;
    LinearLayout createLayout, timeSelectBtns;
    Intent intent;
    Calendar date;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        intent = getIntent();
        date = Calendar.getInstance();

        editTitle = findViewById(R.id.eventTitle);
        editDesc = findViewById(R.id.eventDesc);
        editLocation = findViewById(R.id.eventLocation);
        chkAllDay = findViewById(R.id.chkAllDay);
        createLayout = findViewById(R.id.createLayout);         // Parent view
        timeSelectBtns = findViewById(R.id.timeSelectBtns);     // Layout containing time select buttons

        date.setTimeInMillis(intent.getLongExtra("date", 0));

        // Remove time select buttons when "All Day Event" checkbox is checked
        chkAllDay.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    createLayout.removeView(timeSelectBtns);
                else
                    createLayout.addView(timeSelectBtns, 3);
            }
        });
    }

    // Go back to MainActivity
    public void back(View v) {
        Intent intent = new Intent(CreateEventActivity.this, MainActivity.class);
        startActivity(intent);
        finish();
    }

    // Create new event in the Calendar app
    public void createEvent(View v) {
        Intent intent = new Intent(Intent.ACTION_INSERT, CalendarContract.Events.CONTENT_URI);

        intent.putExtra(CalendarContract.Events.TITLE, editTitle.getText().toString());
        intent.putExtra(CalendarContract.Events.DESCRIPTION, editDesc.getText().toString());
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, editLocation.getText().toString());
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, date.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

        startActivity(intent);
        finish();
    }

    public static class TimePickerFragment extends DialogFragment
            implements TimePickerDialog.OnTimeSetListener {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), this, hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }

        public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
            // TODO: Do something with selected time
        }
    }

    public void showTimePickerDialog(View v) {
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getSupportFragmentManager(), "timePicker");
    }
}