package com.app_mobile.campusexpensemanager;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Toast;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import androidx.appcompat.app.AppCompatActivity;
import com.app_mobile.campusexpensemanager.SQLite.ExpenseDb;
import java.util.Calendar;

public class ExpenseActivity extends AppCompatActivity {

    private EditText descriptionEditText, dateEditText, amountEditText;
    private Spinner categorySpinner;
    private Button addButton, backButtonAdd;
    private ExpenseDb expenseDb;
    private Calendar calendar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_expense);

        // Initialize views
        categorySpinner = findViewById(R.id.categorySpinner);
        descriptionEditText = findViewById(R.id.descriptionEditText);
        dateEditText = findViewById(R.id.dateEditText);
        amountEditText = findViewById(R.id.amountEditText);
        addButton = findViewById(R.id.addButton);
        backButtonAdd = findViewById(R.id.backButtonAdd);

        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.category_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        categorySpinner.setAdapter(adapter);


        // Initialize calendar
        calendar = Calendar.getInstance();

        // Initialize database
        expenseDb = new ExpenseDb(this);

        // Set date picker dialog
        dateEditText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int year = calendar.get(Calendar.YEAR);
                int month = calendar.get(Calendar.MONTH);
                int day = calendar.get(Calendar.DAY_OF_MONTH);

                DatePickerDialog datePickerDialog = new DatePickerDialog(
                        ExpenseActivity.this,
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                                dateEditText.setText(dayOfMonth + "/" + (month + 1) + "/" + year);
                            }
                        }, year, month, day);
                datePickerDialog.show();
            }
        });

        // Set button click listener
        addButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addExpense();
                Intent intent = new Intent(ExpenseActivity.this, ExpenseListActivity.class);
                startActivity(intent);
                finish();
            }
        });

        backButtonAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ExpenseActivity.this, ExpenseListActivity.class);
                finish();
            }
        });

    }

    private void addExpense() {
        String description = descriptionEditText.getText().toString();
        String date = dateEditText.getText().toString();
        double amount = Double.parseDouble(amountEditText.getText().toString());
        String category = categorySpinner.getSelectedItem().toString();


        long result = expenseDb.addExpense(description, date, amount, category);

        if (result != -1) {
            Toast.makeText(this, "Expense added successfully!", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(this, "Failed to add expense.", Toast.LENGTH_SHORT).show();
        }
    }
}