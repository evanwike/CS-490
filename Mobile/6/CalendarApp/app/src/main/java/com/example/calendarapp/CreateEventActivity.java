package com.example.calendarapp;

import android.content.Intent;
import android.provider.CalendarContract;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import java.util.Calendar;

public class CreateEventActivity extends AppCompatActivity {
    EditText editTitle, editDesc, editLocation;
    Calendar date;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_event);

        date = Calendar.getInstance();
        intent = getIntent();

        editTitle = findViewById(R.id.eventTitle);
        editDesc = findViewById(R.id.eventDesc);
        editLocation = findViewById(R.id.eventLocation);

        date.setTimeInMillis(intent.getLongExtra("date", 0));
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

        intent.putExtra(CalendarContract.Events.TITLE, editTitle.getText());
        intent.putExtra(CalendarContract.Events.DESCRIPTION, editDesc.getText());
        intent.putExtra(CalendarContract.Events.EVENT_LOCATION, editLocation.getText());
        intent.putExtra(CalendarContract.EXTRA_EVENT_BEGIN_TIME, date.getTimeInMillis());
        intent.putExtra(CalendarContract.EXTRA_EVENT_ALL_DAY, true);

        startActivity(intent);
        finish();
    }
}