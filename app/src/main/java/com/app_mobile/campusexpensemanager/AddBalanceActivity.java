package com.app_mobile.campusexpensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.app_mobile.campusexpensemanager.SQLite.BalanceDb;

public class AddBalanceActivity extends AppCompatActivity {

    private EditText balanceEditText;
    private Button addButton, backButton;
    private BalanceDb balanceDb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_balance);

        balanceEditText = findViewById(R.id.balanceEditText);
        addButton = findViewById(R.id.addButton);
        backButton = findViewById(R.id.backButton);
        balanceDb = new BalanceDb(this);

        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addBalance();
            }
        });
        backButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AddBalanceActivity.this, ExpenseListActivity.class);
                finish();
            }
        });
    }

    private void addBalance() {
        double newBalance = Double.parseDouble(balanceEditText.getText().toString());
        double currentBalance = balanceDb.getBalance();
        balanceDb.setBalance(currentBalance + newBalance);
        setResult(RESULT_OK); // Indicate successful operation
        finish();
    }
}
