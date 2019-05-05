package com.vijaya.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.support.v4.widget.SimpleCursorAdapter;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vijaya.sqlite.databinding.ActivityEmployeeBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EmployeeActivity extends AppCompatActivity {

    private ActivityEmployeeBinding binding;
    private static final String TAG = "EmployeeActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_employee);

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));

        String[] queryCols = new String[]{"_id", SampleDBContract.Employer.COLUMN_NAME};
        String[] adapterCols = new String[]{SampleDBContract.Employer.COLUMN_NAME};
        int[] adapterRowViews = new int[]{android.R.id.text1};

        SQLiteDatabase database = new SampleDBSQLiteHelper(this).getReadableDatabase();
        Cursor cursor = database.query(
                SampleDBContract.Employer.TABLE_NAME,     // The table to query
                queryCols,                                // The columns to return
                null,                             // The columns for the WHERE clause
                null,                          // The values for the WHERE clause
                null,                             // don't group the rows
                null,                              // don't filter by row groups
                null                              // don't sort
        );

        SimpleCursorAdapter cursorAdapter = new SimpleCursorAdapter(
                this, android.R.layout.simple_spinner_item, cursor, adapterCols, adapterRowViews, 0);
        cursorAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        binding.employerSpinner.setAdapter(cursorAdapter);
        binding.saveButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveToDB();
            }
        });
        binding.searchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                readFromDB();
            }
        });
        binding.employeeEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editDB();
            }
        });
        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deleteFromDB();
            }
        });
        binding.empBackBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployeeActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void saveToDB() {
        SQLiteDatabase database = new SampleDBSQLiteHelper(this).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SampleDBContract.Employee.COLUMN_FIRSTNAME, binding.firstnameEditText.getText().toString());
        values.put(SampleDBContract.Employee.COLUMN_LASTNAME, binding.lastnameEditText.getText().toString());
        values.put(SampleDBContract.Employee.COLUMN_JOB_DESCRIPTION, binding.jobDescEditText.getText().toString());
        values.put(SampleDBContract.Employee.COLUMN_EMPLOYER_ID,
                ((Cursor) binding.employerSpinner.getSelectedItem()).getInt(0));

        Log.d("getINT", ((Cursor) binding.employerSpinner.getSelectedItem()).getInt(0) + "");
        Log.d("getColumnName", ((Cursor) binding.employerSpinner.getSelectedItem()).getColumnName(0));

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((new SimpleDateFormat("dd/MM/yyyy")).parse(
                    binding.dobEditText.getText().toString()));
            long date = calendar.getTimeInMillis();
            values.put(SampleDBContract.Employee.COLUMN_DATE_OF_BIRTH, date);

            calendar.setTime((new SimpleDateFormat("dd/MM/yyyy")).parse(
                    binding.employedEditText.getText().toString()));
            date = calendar.getTimeInMillis();
            values.put(SampleDBContract.Employee.COLUMN_EMPLOYED_DATE, date);
        } catch (Exception e) {
            Log.e(TAG, "Error", e);
            Toast.makeText(this, "Date is in the wrong format", Toast.LENGTH_LONG).show();
            return;
        }
        long newRowId = database.insert(SampleDBContract.Employee.TABLE_NAME, null, values);

        Toast.makeText(this, "The new Row Id is " + newRowId, Toast.LENGTH_LONG).show();
    }

    private void readFromDB() {
        String firstname = binding.firstnameEditText.getText().toString();
        String lastname = binding.lastnameEditText.getText().toString();

        SQLiteDatabase database = new SampleDBSQLiteHelper(this).getReadableDatabase();

        String[] selectionArgs = {"%" + firstname + "%", "%" + lastname + "%"};

        Cursor cursor = database.rawQuery(SampleDBContract.SELECT_EMPLOYEE_WITH_EMPLOYER, selectionArgs);
        binding.recycleView.setAdapter(new SampleJoinRecyclerViewCursorAdapter(this, cursor));
    }

    private void editDB() {
        String fName = binding.firstnameEditText.getText().toString();
        String lName = binding.lastnameEditText.getText().toString();
        String desc = binding.jobDescEditText.getText().toString();
        long DOB = 0;
        long date = 0;

        if (fName.length() == 0 || lName.length() == 0) {
            Toast.makeText(this, "First and last name fields cannot be empty.", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase database = new SampleDBSQLiteHelper(this).getReadableDatabase();

        String[] projection = {
                SampleDBContract.Employee.COLUMN_EMPLOYER_ID,
                SampleDBContract.Employee.COLUMN_FIRSTNAME,
                SampleDBContract.Employee.COLUMN_LASTNAME,
                SampleDBContract.Employee.COLUMN_DATE_OF_BIRTH,
                SampleDBContract.Employee.COLUMN_JOB_DESCRIPTION,
                SampleDBContract.Employee.COLUMN_EMPLOYED_DATE
        };

        String selection =
                SampleDBContract.Employee.COLUMN_FIRSTNAME + " like ? and " +
                        SampleDBContract.Employee.COLUMN_LASTNAME + " like ?";

        String[] selectionArgs = {"%" + fName + "%", "%" + lName + "%"};

        Cursor cursor = database.query(
                SampleDBContract.Employee.TABLE_NAME,     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                             // don't group the rows
                null,                              // don't filter by row groups
                null                              // don't sort
        );

        clearInput();

        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);

            Intent intent = new Intent(this, EditEmployee.class);
            intent.putExtra("id", cursor.getString(cursor.getColumnIndexOrThrow(SampleDBContract.Employee.COLUMN_EMPLOYER_ID)));
            intent.putExtra("fName", cursor.getString(cursor.getColumnIndexOrThrow(SampleDBContract.Employee.COLUMN_FIRSTNAME)));
            intent.putExtra("lName", cursor.getString(cursor.getColumnIndexOrThrow(SampleDBContract.Employee.COLUMN_LASTNAME)));
            intent.putExtra("dob", cursor.getString(cursor.getColumnIndexOrThrow(SampleDBContract.Employee.COLUMN_DATE_OF_BIRTH)));
            intent.putExtra("desc", cursor.getString(cursor.getColumnIndexOrThrow(SampleDBContract.Employee.COLUMN_JOB_DESCRIPTION)));
            intent.putExtra("date", cursor.getString(cursor.getColumnIndexOrThrow(SampleDBContract.Employee.COLUMN_EMPLOYED_DATE)));
            cursor.close();
            startActivity(intent);
        } else {
            Toast.makeText(this, "No Employer entries found by that name and description", Toast.LENGTH_SHORT).show();
        }
    }

    private void deleteFromDB() {
        String fName = binding.firstnameEditText.getText().toString();
        String lName = binding.lastnameEditText.getText().toString();

        if (fName.length() == 0 || lName.length() == 0) {
            Toast.makeText(this, "Please provide a first and last name of the employee you wish to remove from the database.", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase database = new SampleDBSQLiteHelper(this).getReadableDatabase();

        String[] projection = { SampleDBContract.Employee.COLUMN_FIRSTNAME, SampleDBContract.Employee.COLUMN_LASTNAME, SampleDBContract.Employee.COLUMN_EMPLOYER_ID };

        String selection =
                SampleDBContract.Employee.COLUMN_FIRSTNAME + " like ? and " +
                        SampleDBContract.Employee.COLUMN_LASTNAME + " like ?";

//        String[] selectionArgs = {"%" + name + "%", date + "", "%" + desc + "%"};
        String[] selectionArgs = {"%" + fName + "%", "%" + lName + "%"};

        Cursor cursor = database.query(
                SampleDBContract.Employee.TABLE_NAME,     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                             // don't group the rows
                null,                              // don't filter by row groups
                null                              // don't sort
        );

        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            database.delete(SampleDBContract.Employee.TABLE_NAME, "lastname=? AND firstname=? AND employer_id=?", new String[] {cursor.getString(
                    cursor.getColumnIndexOrThrow(SampleDBContract.Employee.COLUMN_LASTNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SampleDBContract.Employee.COLUMN_FIRSTNAME)),
                    cursor.getString(cursor.getColumnIndexOrThrow(SampleDBContract.Employee.COLUMN_EMPLOYER_ID))
            });

            Toast.makeText(this, "Success! Employee deleted.", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
    }

    public void clearInput() {
        EditText fName = findViewById(R.id.firstnameEditText);
        EditText lName = findViewById(R.id.lastnameEditText);
        EditText DOB = findViewById(R.id.dobEditText);
        EditText desc = findViewById(R.id.jobDescEditText);
        EditText employed = findViewById(R.id.employedEditText);

        fName.setText("");
        lName.setText("");
        DOB.setText("");
        desc.setText("");
        employed.setText("");

        InputMethodManager iMM = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        iMM.hideSoftInputFromWindow(employed.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }
}