package com.vijaya.sqlite;

import android.content.ContentValues;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EditEmployee extends AppCompatActivity {
    EditText editFName, editLName, editDOB, editDesc, editDate;
    String id, fName, lName, DOBText, desc, dateText;
    long DOB, date;
    Intent intent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_employee);

        intent = getIntent();

        id = intent.getStringExtra("id");
        fName = intent.getStringExtra("fName");
        lName = intent.getStringExtra("lName");
        DOB = intent.getLongExtra("dob", 0);
        desc = intent.getStringExtra("desc");
        date = intent.getLongExtra("date", 0);

        DOBText = "";
        dateText = "";

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(DOB);
            DOBText = new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime());

            calendar.setTimeInMillis(date);
            dateText = new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime());

        } catch (Exception e) {
            Toast.makeText(this, "Invalid date format", Toast.LENGTH_SHORT).show();
            return;
        }

        // Populate fields with Employee data from DB
        editFName = findViewById(R.id.empEditFName);
        editLName = findViewById(R.id.empEditLName);
        editDOB = findViewById(R.id.empEditDOB);
        editDesc = findViewById(R.id.empEditDesc);
        editDate = findViewById(R.id.empEditDate);

        editFName.setText(fName);
        editLName.setText(lName);
        editDOB.setText(DOBText);
        editDesc.setText(desc);
        editDate.setText(dateText);
    }

    // Gets edited values from form and updates the Employee DB
    public void updateDB(View v) {
        SQLiteDatabase database = new SampleDBSQLiteHelper(this).getWritableDatabase();
        ContentValues values = new ContentValues();

        values.put(SampleDBContract.Employee.COLUMN_FIRSTNAME, editFName.getText().toString());
        values.put(SampleDBContract.Employee.COLUMN_LASTNAME, editLName.getText().toString());
        values.put(SampleDBContract.Employee.COLUMN_JOB_DESCRIPTION, editDesc.getText().toString());

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((new SimpleDateFormat("dd/MM/yyyy")).parse(editDOB.getText().toString()));
            values.put(SampleDBContract.Employee.COLUMN_DATE_OF_BIRTH, calendar.getTimeInMillis());

            calendar.setTime((new SimpleDateFormat("dd/MM/yyyy")).parse(editDate.getText().toString()));
            values.put(SampleDBContract.Employee.COLUMN_EMPLOYED_DATE, calendar.getTimeInMillis());
        } catch (Exception e) {
            Toast.makeText(this, "Incorrect date format", Toast.LENGTH_LONG).show();
        }

        database.update(SampleDBContract.Employee.TABLE_NAME, values, "employer_id=?", new String[] {id});
        Toast.makeText(this, "Success! Employee updated", Toast.LENGTH_SHORT).show();
        goBack();
    }

    private void goBack() {
        Intent intent = new Intent(this, EmployeeActivity.class);
        startActivity(intent);
    }
}
