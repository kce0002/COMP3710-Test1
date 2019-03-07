package com.example.comp3710_test1;

import android.content.SharedPreferences;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView balance;
    EditText date;
    EditText money;
    EditText description;
    Button add;
    Button subtract;
    ScrollView history;
    LinearLayout histView;
    double currentBalance;
    ArrayList<String> historyList;
    DecimalFormat df;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        df = new DecimalFormat("0.00");
        historyList = new ArrayList<>();
        int historySize = 0;
        String historyElement = "";

        // UI Elements:
        balance = findViewById(R.id.balanceText);
        date = findViewById(R.id.dateBox);
        money = findViewById(R.id.moneyBox);
        description = findViewById(R.id.descriptionBox);
        add = findViewById(R.id.plusButton);
        subtract = findViewById(R.id.minusButton);
        history = findViewById(R.id.scrollView);
        histView = findViewById(R.id.historyView);

        SharedPreferences prefs = this.getSharedPreferences("myPrefsFile", 0);
        currentBalance = prefs.getFloat("balanceKey", (float) currentBalance);
        balance.setText("Current Balance: $" + df.format(currentBalance));
        for (int i = 0; i < prefs.getInt("historySize", historySize); i++) {
            TextView tv = new TextView(MainActivity.this);
            String s = prefs.getString("history" + i, historyElement);
            tv.setText(s);
            histView.addView(tv);
            historyList.add(s);
        }
    }

    @Override
    protected void onStop() {
        SharedPreferences prefs = this.getSharedPreferences("myPrefsFile", 0);
        SharedPreferences.Editor editor = prefs.edit();
        editor.putFloat("balanceKey", (float) currentBalance);
        editor.putInt("historySize", historyList.size());
        for (int i = 0; i < historyList.size(); i++) {
            editor.putString("history" + i, historyList.get(i));
        }
        editor.commit();

        super.onStop();
    }


    public void addAction(View v) {
        String dateString = date.getText().toString();
        double amount = Double.parseDouble(money.getText().toString());
        String desc = description.getText().toString();

        String result = "Added $" + df.format(amount) + " on " + dateString + " from " + desc;
        historyList.add(result);
        currentBalance += amount;

        //setContentView(R.layout.activity_main);
        TextView tv = new TextView(MainActivity.this);
        tv.setText(result);

        histView.addView(tv);
        balance.setText("Current Balance: $" + df.format(currentBalance));
    }

    public void subtractAction(View v) {
        String dateString = date.getText().toString();
        double amount = Double.parseDouble(money.getText().toString());
        String desc = description.getText().toString();

        String result = "Spent $" + df.format(amount) + " on " + dateString + " for " + desc;
        historyList.add(result);
        currentBalance -= amount;

        //setContentView(R.layout.activity_main);
        TextView tv = new TextView(MainActivity.this);
        tv.setText(result);

        histView.addView(tv);
        balance.setText("Current Balance: $" + df.format(currentBalance));
    }

}
