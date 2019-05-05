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

public class EditEmployer extends AppCompatActivity {
    EditText editName;
    EditText editDesc;
    EditText editFounded;

    Intent intent;

    String id;
    String name;
    String desc;
    Long founded;
    String date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_employer);

        intent = getIntent();

        id = intent.getStringExtra("id");
        name = intent.getStringExtra("name");
        desc = intent.getStringExtra("desc");
        founded = intent.getLongExtra("date", 0);

        date = "";

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTimeInMillis(founded);
            date = new SimpleDateFormat("dd/MM/yyyy").format(calendar.getTime());

        } catch (Exception e) {}

        editName = findViewById(R.id.nameEditText3);
        editDesc = findViewById(R.id.descEditText3);
        editFounded = findViewById(R.id.foundedEditText3);

        editName.setText(name);
        editDesc.setText(desc);
        editFounded.setText(date);
    }

    public void updateDB(View v) {
        SQLiteDatabase database = new SampleDBSQLiteHelper(this).getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(SampleDBContract.Employer.COLUMN_NAME, editName.getText().toString());
        values.put(SampleDBContract.Employer.COLUMN_DESCRIPTION, editDesc.getText().toString());

        try {
            Calendar calendar = Calendar.getInstance();
            calendar.setTime((new SimpleDateFormat("dd/MM/yyyy")).parse(editFounded.getText().toString()));
            long newDate = calendar.getTimeInMillis();
            values.put(SampleDBContract.Employer.COLUMN_FOUNDED_DATE, newDate);
        } catch (Exception e) {
            Toast.makeText(this, "Incorrect date format", Toast.LENGTH_LONG).show();
        }

        database.update(SampleDBContract.Employer.TABLE_NAME, values, "_id=?", new String[] {id});
        Toast.makeText(this, "Success! Employer updated", Toast.LENGTH_SHORT).show();
        goBack();
    }

    private void goBack() {
        Intent intent = new Intent(this, EmployerActivity.class);
        startActivity(intent);
    }
}
