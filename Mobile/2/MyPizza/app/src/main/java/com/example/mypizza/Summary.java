package com.example.mypizza;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import java.util.ArrayList;
import java.util.Locale;

public class Summary extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_summary);

        Intent intent = getIntent();
        String username = intent.getStringExtra("username");
        double sum = intent.getDoubleExtra("sum", 0.0);
        ArrayList<String> options = intent.getStringArrayListExtra("options");
        int qty = intent.getIntExtra("qty", 0);

        // Set username view
        TextView textView = findViewById(R.id.summaryUserView);
        textView.setText(username);

        // Set list view with order details
        ListView lv = findViewById(R.id.order);
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, options);

        lv.setAdapter(adapter);

        // Set subtotal view
        TextView subtotalView = findViewById(R.id.summarySubView);
        subtotalView.setText(String.format(Locale.US, "Subtotal: $%.2f (QTY. %d)", sum, qty));

        TextView totalView = findViewById(R.id.summaryTotalView);
        totalView.setText(String.format(Locale.US, "Total: $%.2f", sum * qty + (sum * qty * .07)));

        Button backBtn = findViewById(R.id.summaryBackBtn);
        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent redirect = new Intent(Summary.this, MainActivity.class);
                // Keep activity from being killed and recreated when going back and forth
                redirect.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(redirect);
            }
        });
    }
}
