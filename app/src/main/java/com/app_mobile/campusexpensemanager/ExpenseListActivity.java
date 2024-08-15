package com.app_mobile.campusexpensemanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView;
import android.widget.TextView;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.app_mobile.campusexpensemanager.Model.User;
import com.app_mobile.campusexpensemanager.SQLite.BalanceDb;
import com.app_mobile.campusexpensemanager.SQLite.ExpenseDb;
import com.app_mobile.campusexpensemanager.SQLite.UserDb;
import android.view.MenuItem;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import java.util.ArrayList;
import java.util.List;


public class ExpenseListActivity extends AppCompatActivity {

    private static final int REQUEST_CODE_ADD_BALANCE = 1;
    private static final int REQUEST_CODE_EXPENSE_DETAILS = 2;
    private ListView expenseListView;
    private TextView balanceTextView;
    private TextView usernameTextView;
    private ExpenseDb expenseDb;
    private BalanceDb balanceDb;
    private UserDb userDb;
    private Button addBalanceButton, addExpenseButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_list);

        expenseListView = findViewById(R.id.expenseListView);
        balanceTextView = findViewById(R.id.balanceTextView);
        usernameTextView = findViewById(R.id.usernameTextView);
        addBalanceButton = findViewById(R.id.addBalanceButton);
        addExpenseButton = findViewById(R.id.addExpenseButton);

        expenseDb = new ExpenseDb(this);
        balanceDb = new BalanceDb(this);
        userDb = new UserDb(this);

        updateProfileAndBalance();
        loadExpenses();

        addBalanceButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExpenseListActivity.this, AddBalanceActivity.class);
                startActivityForResult(intent, REQUEST_CODE_ADD_BALANCE);
            }
        });

        addExpenseButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExpenseListActivity.this, ExpenseActivity.class);
                startActivity(intent);
            }
        });

        expenseListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(ExpenseListActivity.this, ExpenseDetailActivity.class);
                intent.putExtra("expense_id", id);
                startActivityForResult(intent, REQUEST_CODE_EXPENSE_DETAILS);
            }
        });
    }

    private void updateProfileAndBalance() {
        String username = getIntent().getStringExtra("username"); // Retrieve the username from the Intent
        if (username == null) {
            // Handle the case where username is not available
            usernameTextView.setText("Username not available");
            return;
        }

        // Retrieve user data from the database
        User user = userDb.getSingleUser(username, ""); // Empty password as we don't need it for this purpose

        if (user != null) {
            usernameTextView.setText("Account: " + user.getUsername());
        } else {
            usernameTextView.setText("User not found");
        }


        double balance = balanceDb.getBalance();
        balanceTextView.setText("Balance: $" + String.format("%.2f", balance));
    }

    private void loadExpenses() {
        List<String> expenseList = new ArrayList<>();
        final List<Long> expenseIds = new ArrayList<>(); // List to store expense IDs

        Cursor cursor = expenseDb.getAllExpenses();

        if (cursor.moveToFirst()) { // Start from the first record
            do {
                @SuppressLint("Range") long id = cursor.getLong(cursor.getColumnIndex("id")); // Get the expense ID
                @SuppressLint("Range") String description = cursor.getString(cursor.getColumnIndex("description"));
                @SuppressLint("Range") String date = cursor.getString(cursor.getColumnIndex("date"));
                @SuppressLint("Range") double amount = cursor.getDouble(cursor.getColumnIndex("amount"));
                @SuppressLint("Range") String category = cursor.getString(cursor.getColumnIndex("category"));

                String expense = "Description: " + description + "\nDate: " + date +
                        "\nAmount: $" + amount + "\nCategory: " + category;

                expenseList.add(expense);
                expenseIds.add(id); // Store the ID
            } while (cursor.moveToNext()); // Move to the next record
        }

        cursor.close();

        ArrayAdapter<String> adapter = new ArrayAdapter<>(
                this,
                android.R.layout.simple_list_item_1,
                expenseList
        );

        expenseListView.setAdapter(adapter);

        // Set the item click listener to pass the correct expense ID
        expenseListView.setOnItemClickListener((parent, view, position, id) -> {
            long expenseId = expenseIds.get(position); // Get the expense ID for the clicked item
            Intent intent = new Intent(ExpenseListActivity.this, ExpenseDetailActivity.class);
            intent.putExtra("expense_id", expenseId);
            startActivity(intent);
        });
    }




    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_CODE_ADD_BALANCE && resultCode == RESULT_OK) {
            // Update balance after adding a new balance
            updateProfileAndBalance();
        } else if (requestCode == REQUEST_CODE_EXPENSE_DETAILS && resultCode == RESULT_OK) {
            // Reload expenses to reflect any updates or deletions
            loadExpenses();
        }
    }
}
