package com.vijaya.sqlite;

import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.databinding.DataBindingUtil;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.vijaya.sqlite.databinding.ActivityEmployerBinding;

import java.text.SimpleDateFormat;
import java.util.Calendar;

public class EmployerActivity extends AppCompatActivity {

    private static final String TAG = "EmployerActivity";
    private ActivityEmployerBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.activity_employer);

        binding.recycleView.setLayoutManager(new LinearLayoutManager(this));
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
        binding.deleteButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deleteFromDB();
            }
        });
        binding.employerEditBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                editDB();
            }
        });

        Button back = findViewById(R.id.employerBackBtn);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(EmployerActivity.this, MainActivity.class);
                startActivity(intent);
                finish();
            }
        });
    }

    private void saveToDB() {
        SQLiteDatabase database = new SampleDBSQLiteHelper(this).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SampleDBContract.Employer.COLUMN_NAME, binding.nameEditText.getText().toString());
        values.put(SampleDBContract.Employer.COLUMN_DESCRIPTION, binding.descEditText.getText().toString());
        EditText name = findViewById(R.id.nameEditText);
        EditText desc = findViewById(R.id.descEditText);
        EditText founded = findViewById(R.id.foundedEditText);

        if (name.length() == 0 || desc.length() == 0) {
            Toast.makeText(this, "Please fill in all fields.", Toast.LENGTH_SHORT).show();
            return;
        }

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((new SimpleDateFormat("dd/MM/yyyy")).parse(
                    binding.foundedEditText.getText().toString()));
            long date = calendar.getTimeInMillis();
            values.put(SampleDBContract.Employer.COLUMN_FOUNDED_DATE, date);
        } catch (Exception e) {
            Log.e(TAG, "Error", e);
            Toast.makeText(this, "Date is in the wrong format", Toast.LENGTH_LONG).show();
            return;
        }
        long newRowId = database.insert(SampleDBContract.Employer.TABLE_NAME, null, values);

        // Reset text input fields and hide keyboard
        clearInput();

        Toast.makeText(this, "Success! Employer added to row " + newRowId, Toast.LENGTH_SHORT).show();
    }

    private void readFromDB() {
        String name = binding.nameEditText.getText().toString();
        String desc = binding.descEditText.getText().toString();
        long date = 0;

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((new SimpleDateFormat("dd/MM/yyyy")).parse(
                    binding.foundedEditText.getText().toString()));
            date = calendar.getTimeInMillis();
        } catch (Exception e) {
        }

        SQLiteDatabase database = new SampleDBSQLiteHelper(this).getReadableDatabase();

        String[] projection = {
                SampleDBContract.Employer._ID,
                SampleDBContract.Employer.COLUMN_NAME,
                SampleDBContract.Employer.COLUMN_DESCRIPTION,
                SampleDBContract.Employer.COLUMN_FOUNDED_DATE
        };

        String selection =
                SampleDBContract.Employer.COLUMN_NAME + " like ? and " +
//                        SampleDBContract.Employer.COLUMN_FOUNDED_DATE + " > ? and " +
                        SampleDBContract.Employer.COLUMN_DESCRIPTION + " like ?";

//        String[] selectionArgs = {"%" + name + "%", date + "", "%" + desc + "%"};
        String[] selectionArgs = {"%" + name + "%", "%" + desc + "%"};

        Cursor cursor = database.query(
                SampleDBContract.Employer.TABLE_NAME,     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                             // don't group the rows
                null,                              // don't filter by row groups
                null                              // don't sort
        );

        binding.recycleView.setAdapter(new SampleRecyclerViewCursorAdapter(this, cursor));
    }

    public void deleteFromDB() {
        String name = binding.nameEditText.getText().toString();
        String desc = binding.descEditText.getText().toString();

        if (name.length() == 0 || desc.length() == 0) {
            Toast.makeText(this, "Please provide a name and description for the Employer you wish to delete.", Toast.LENGTH_SHORT).show();
            return;
        }

        SQLiteDatabase database = new SampleDBSQLiteHelper(this).getReadableDatabase();

        String[] projection = { SampleDBContract.Employer._ID };

        String selection =
                SampleDBContract.Employer.COLUMN_NAME + " like ? and " +
                        SampleDBContract.Employer.COLUMN_DESCRIPTION + " like ?";

//        String[] selectionArgs = {"%" + name + "%", date + "", "%" + desc + "%"};
        String[] selectionArgs = {"%" + name + "%", "%" + desc + "%"};

        Cursor cursor = database.query(
                SampleDBContract.Employer.TABLE_NAME,     // The table to query
                projection,                               // The columns to return
                selection,                                // The columns for the WHERE clause
                selectionArgs,                            // The values for the WHERE clause
                null,                             // don't group the rows
                null,                              // don't filter by row groups
                null                              // don't sort
        );

        if (cursor.getCount() > 0) {
            cursor.moveToPosition(0);
            database.delete(SampleDBContract.Employer.TABLE_NAME, "_id=?", new String[] {cursor.getString(
                    cursor.getColumnIndexOrThrow(SampleDBContract.Employer._ID))});

            Toast.makeText(this, "Success! Employer deleted.", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
    }

    private void editDB() {
        String name = binding.nameEditText.getText().toString();
        String desc = binding.descEditText.getText().toString();
        long date = 0;

        if (name.length() == 0) {
            Toast.makeText(this, "Name and description fields cannot be empty", Toast.LENGTH_SHORT).show();
            return;
        }
        SQLiteDatabase database = new SampleDBSQLiteHelper(this).getReadableDatabase();

        String[] projection = {
                SampleDBContract.Employer._ID,
                SampleDBContract.Employer.COLUMN_NAME,
                SampleDBContract.Employer.COLUMN_DESCRIPTION,
                SampleDBContract.Employer.COLUMN_FOUNDED_DATE
        };

        String selection =
                SampleDBContract.Employer.COLUMN_NAME + " like ? and " +
                        SampleDBContract.Employer.COLUMN_DESCRIPTION + " like ?";

        String[] selectionArgs = {"%" + name + "%", "%" + desc + "%"};

        Cursor cursor = database.query(
                SampleDBContract.Employer.TABLE_NAME,     // The table to query
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

            Intent intent = new Intent(this, EditEmployer.class);
            intent.putExtra("id", cursor.getString(cursor.getColumnIndexOrThrow(SampleDBContract.Employer._ID)));
            intent.putExtra("name", cursor.getString(cursor.getColumnIndexOrThrow(SampleDBContract.Employer.COLUMN_NAME)));
            intent.putExtra("desc", cursor.getString(cursor.getColumnIndexOrThrow(SampleDBContract.Employer.COLUMN_DESCRIPTION)));
            intent.putExtra("date", cursor.getLong(cursor.getColumnIndexOrThrow(SampleDBContract.Employer.COLUMN_FOUNDED_DATE)));
            startActivity(intent);
        } else {
            Toast.makeText(this, "No Employer entries found by that name and description", Toast.LENGTH_SHORT).show();
        }

        cursor.close();
    }

    public void clearInput() {
        EditText name = findViewById(R.id.nameEditText);
        EditText desc = findViewById(R.id.descEditText);
        EditText founded = findViewById(R.id.foundedEditText);

        name.setText("");
        desc.setText("");
        founded.setText("");

        InputMethodManager iMM = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        iMM.hideSoftInputFromWindow(founded.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }
}