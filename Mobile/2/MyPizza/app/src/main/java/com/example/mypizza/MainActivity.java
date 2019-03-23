package com.example.mypizza;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Locale;


public class MainActivity extends AppCompatActivity {

    class Base {
        private int id;
        private String value;
        private double price;

        Base(int id, String value, double price) {
            this.id = id;
            this.value = value;
            this.price = price;
        }

        public int getId() { return id; }
        public double getPrice() { return price; }
        public String getDisplayStr() {
            return String.format(Locale.US, "%s (+$%.2f)", value, price);
        }
    }

    class Ingredient {
        private int id;
        private String value;
        private double price;

        Ingredient(int id, String value, double price) {
            this.id = id;
            this.value = value;
            this.price = price;
        }

        public int getId() { return id; }
        public double getPrice() { return price; }
        public boolean isChecked() {
            CheckBox option = findViewById(id);
            return option.isChecked();
        }
        public String getDisplayStr() {
            return String.format(Locale.US, "%s (+$%.2f)", value, price);
        }
    }

    ArrayList<Base> bases = initBases();
    ArrayList<Ingredient> ingredients = initIngredients();
    int qty = 0;

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
                qty = progress;
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
                // Get username
                EditText usernameCtrl = findViewById(R.id.username);
                String username = usernameCtrl.getText().toString();

                // Get sum of order
                double sum = getSum();

                // Get selected options as list of strings
                ArrayList<String> options = getOptions();

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

                // Body of email
                double sum = getSum();
                ArrayList<String> options = getOptions();
                StringBuilder body = new StringBuilder();

                body.append(String.format("Customer: %s\n\n", username));

                for (String option : options)
                    body.append(String.format("%s\n", option));

                body.append(String.format(Locale.US, "\nSubtotal: $%.2f (QTY. %d)\n", sum, qty));
                body.append(String.format(Locale.US, "Total: $%.2f", sum * qty + (sum * qty * .07)));

                email.putExtra(Intent.EXTRA_TEXT, body.toString());
                startActivity(Intent.createChooser(email, "Send order..."));
            }
        });
    }

    public double getSum() {
        double sum = 0.0;
        Base base = getSelectedBase();
        ArrayList<Ingredient> ingredients = getSelectedIngredients();

        sum += base.getPrice();

        for (Ingredient ingredient : ingredients)
            sum += ingredient.getPrice();

        return sum;
    }

    public ArrayList<String> getOptions() {
        ArrayList<String> options = new ArrayList<>();
        Base base = getSelectedBase();
        ArrayList<Ingredient> ingredients = getSelectedIngredients();

        options.add(base.getDisplayStr());

        for (Ingredient ingredient : ingredients)
            options.add(ingredient.getDisplayStr());

        return options;
    }

    public Base getSelectedBase() {
        RadioGroup basesGroup = findViewById(R.id.bases);
        int baseID = basesGroup.getCheckedRadioButtonId();
        Base base = new Base(0, "", 0.0);

        for (Base b : bases)
            if (b.getId() == baseID)
                base = b;

        return base;
    }

    public ArrayList<Ingredient> getSelectedIngredients() {
        ArrayList<Ingredient> selected = new ArrayList<>();

        for (Ingredient ingredient : ingredients) {
            if (ingredient.isChecked()) {
                selected.add(ingredient);
            }
        }

        return selected;
    }

    public ArrayList<Base> initBases() {
        ArrayList<Base> bases = new ArrayList<>();
        int[] ids = {R.id.base1, R.id.base2, R.id.base3};
        String[] options = {"Marinara", "Pesto", "BBQ"};
        double[] prices = {5.99, 5.99, 6.99};

        for (int i = 0; i < ids.length; i++) {
            Base base = new Base(ids[i], options[i], prices[i]);
            bases.add(base);
        }

        return bases;
    }

    public ArrayList<Ingredient> initIngredients() {
        ArrayList<Ingredient> ingredients = new ArrayList<>();
        int[] ids = {R.id.cheese1, R.id.cheese2, R.id.ing1, R.id.ing2, R.id.ing3, R.id.ing4};
        String[] options = {"Mozzarella", "Provolone", "Pepperoni", "Sausage", "Hamburger", "Bacon"};
        double[] prices = {1.99, 2.99, 1.99, 1.99, 1.99, 2.99};


        for (int i = 0; i < ids.length; i++) {
            Ingredient ingredient = new Ingredient(ids[i], options[i], prices[i]);
            ingredients.add(ingredient);
        }

        return ingredients;
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


