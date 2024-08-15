package com.app_mobile.campusexpensemanager;

import android.content.ContentValues;
import android.content.Intent;
import android.database.Cursor;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.EditText;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import com.app_mobile.campusexpensemanager.SQLite.ExpenseDb;

public class ExpenseDetailActivity extends AppCompatActivity {

    private EditText descriptionEditText, dateEditText, amountEditText;
    private Spinner categorySpinner;
    private Button saveButton, deleteButton;
    private ExpenseDb expenseDb;
    private long expenseId;
    private ImageView backButton;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense_detail);

        descriptionEditText = findViewById(R.id.descriptionEditText);
        dateEditText = findViewById(R.id.dateEditText);
        amountEditText = findViewById(R.id.amountEditText);
        categorySpinner = findViewById(R.id.categorySpinner);
        saveButton = findViewById(R.id.saveButton);
        deleteButton = findViewById(R.id.deleteButton);
        backButton = findViewById(R.id.backButton);

        expenseDb = new ExpenseDb(this);

        // Set up the Spinner with predefined categories
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);

        // Get the expense ID from the Intent
        expenseId = getIntent().getLongExtra("expense_id", -1);
        if (expenseId == -1) {
            // Handle error: Expense ID is missing
            finish(); // Close the activity
            return;
        }

        // Load expense details
        loadExpenseDetails(expenseId);

        // Set up click listeners
        saveButton.setOnClickListener(v -> saveExpenseDetails());

        deleteButton.setOnClickListener(v -> deleteExpense());

        backButton.setOnClickListener(v -> finish()); // Go back to the previous activity
    }

    private void loadExpenseDetails(long expenseId) {
        Cursor cursor = expenseDb.getExpenseById(expenseId);
        if (cursor != null && cursor.moveToFirst()) {
            descriptionEditText.setText(cursor.getString(cursor.getColumnIndex("description")));
            dateEditText.setText(cursor.getString(cursor.getColumnIndex("date")));
            amountEditText.setText(String.valueOf(cursor.getDouble(cursor.getColumnIndex("amount"))));

            // Set the spinner to the correct category
            String category = cursor.getString(cursor.getColumnIndex("category"));
            ArrayAdapter<CharSequence> adapter = (ArrayAdapter<CharSequence>) categorySpinner.getAdapter();
            int position = adapter.getPosition(category);
            categorySpinner.setSelection(position);

            cursor.close();
        }
    }


    private void saveExpenseDetails() {
        ContentValues values = new ContentValues();
        values.put("description", descriptionEditText.getText().toString());
        values.put("date", dateEditText.getText().toString());
        values.put("amount", Double.parseDouble(amountEditText.getText().toString()));
        values.put("category", categorySpinner.getSelectedItem().toString());

        int rowsUpdated = expenseDb.updateExpense(expenseId, values);
        if (rowsUpdated > 0) {
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }

    private void deleteExpense() {
        int rowsDeleted = expenseDb.deleteExpense(expenseId);
        if (rowsDeleted > 0) {
            setResult(RESULT_OK);
        } else {
            setResult(RESULT_CANCELED);
        }
        finish();
    }
}
