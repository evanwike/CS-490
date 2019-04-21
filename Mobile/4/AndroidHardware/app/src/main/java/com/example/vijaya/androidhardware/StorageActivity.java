package com.example.vijaya.androidhardware;

import android.content.Context;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;

public class StorageActivity extends AppCompatActivity {
    EditText inputTxt;
    TextView outputTxt;
    String FILENAME = "MyAppStorage";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_storage);
        inputTxt = findViewById(R.id.id_txt_mycontent);
        outputTxt = findViewById(R.id.id_txt_display);
    }

    public void saveToFile(View v) throws IOException {
        // ICP Task4: Write the code to save the text
        FileOutputStream outputStream;

        try {
            outputStream = openFileOutput(FILENAME, Context.MODE_PRIVATE);
            outputStream.write(inputTxt.getText().toString().getBytes());
            outputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        // Clear text input, hide keyboard
        inputTxt.setText("");
        hideKeyboard(inputTxt);
    }

    public void retrieveFromFile(View v) throws IOException {
        // ICP Task4: Write the code to display the above saved text
        FileInputStream inputStream;
        StringBuilder sb = new StringBuilder();

        try {
            inputStream = openFileInput(FILENAME);
            InputStreamReader inputStreamReader = new InputStreamReader(inputStream);
            BufferedReader bufferedReader = new BufferedReader(inputStreamReader);
            String line;

            while ((line = bufferedReader.readLine()) != null)
                sb.append(line);

            outputTxt.setText(sb);
            outputTxt.setVisibility(View.VISIBLE);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    // Hides keyboard
    public void hideKeyboard(View v) {
        InputMethodManager iMM = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        iMM.hideSoftInputFromWindow(v.getWindowToken(), InputMethodManager.RESULT_UNCHANGED_SHOWN);
    }
}
