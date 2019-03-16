package com.example.loginapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    public void validateCredentials(View v) {
        EditText usernameCtrl = findViewById(R.id.usernameText);
        EditText passwordCtrl = findViewById(R.id.passwordText);
        String username = usernameCtrl.getText().toString();
        String password = passwordCtrl.getText().toString();

        boolean validationFlag = false;

        if (!username.isEmpty() && !password.isEmpty()) {
            if (username.equals("Admin") && password.equals("Admin")) {
                validationFlag = true;
            }
        }

        if (!validationFlag) {
            Log.i("invalidCredentials", "Incorrect username or password.");
            Toast.makeText(MainActivity.this, "You entered wrong credentials! Please try again", Toast.LENGTH_LONG).show();
        }
        else {
            redirectToHomePage(v, username);
        }
    }

    public void redirectToHomePage(View v, String username) {
        Intent redirect = new Intent(MainActivity.this, HomeActivity.class);
        redirect.putExtra("username", username);
        startActivity(redirect);
    }
}
