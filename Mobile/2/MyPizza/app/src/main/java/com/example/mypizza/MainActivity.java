package com.example.mypizza;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private int[] bases = {R.id.base1, R.id.base2, R.id.base3};
    private double[] basePrices = {5.99, 5.99, 6.99};

    private int[] ingredients = {R.id.cheese1, R.id.cheese2, R.id.ing1, R.id.ing2, R.id.ing3, R.id.ing4};
    private double[] ingredientsPrices = {1.99, 2.99, 1.99, 1.99, 1.99, 2.99};

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Quantity seek bar
        SeekBar seekBar = findViewById(R.id.seek_qty);
        final TextView displayQty = findViewById(R.id.qty);

        // Set initial quantity
        displayQty.setText(String.format(Locale.US, "%d", seekBar.getProgress()));

        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                String valString = Integer.toString(progress);
                displayQty.setText(valString);
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) { }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) { }
        });

        // Summary
        Button summary = findViewById(R.id.btn_summary);
        summary.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                double sum = 0;
                ArrayList<String> options = new ArrayList<>();

                // Get username
                EditText usernameCtrl = findViewById(R.id.username);
                String username = usernameCtrl.getText().toString();

                // Get base
                RadioGroup basesGroup = findViewById(R.id.bases);
                int baseID = basesGroup.getCheckedRadioButtonId();
                RadioButton base = findViewById(baseID);

                for (int i = 0; i < bases.length; i++) {
                    if (bases[i] == baseID) {
                        sum += basePrices[i];
                        options.add(base.getText().toString());
                    }
                }

                // Get options
                for (int i = 0; i < ingredients.length; i++) {
                    CheckBox option = findViewById(ingredients[i]);

                    if (option.isChecked()) {
                        sum += ingredientsPrices[i];
                        options.add(option.getText().toString());
                    }
                }

                SeekBar seekBar = findViewById(R.id.seek_qty);
                int qty = seekBar.getProgress();

                redirectToSummary(v, username, sum, options, qty);
            }
        });

        Button order = findViewById(R.id.btn_order);
        order.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent email = new Intent(Intent.ACTION_SENDTO, Uri.fromParts(
                        "mailto", "evwxtd@mail.umkc.edu", null));

                EditText usernameCtrl = findViewById(R.id.username);
                String username = usernameCtrl.getText().toString();
                email.putExtra(Intent.EXTRA_SUBJECT, String.format("MyPizza - Order from %s!", username));
                email.putExtra(Intent.EXTRA_TEXT, "Body");
                startActivity(Intent.createChooser(email, "Send order..."));
            }
        });
    }

    public void redirectToSummary(View v, String username, double sum, ArrayList<String> options, int qty) {
        Intent redirect = new Intent(MainActivity.this, Summary.class);
        redirect.putExtra("username", username);
        redirect.putExtra("sum", sum);
        redirect.putExtra("options", options);
        redirect.putExtra("qty", qty);
        // Keep activity from being killed and recreated when going back and forth
        redirect.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
        startActivity(redirect);
    }
}


