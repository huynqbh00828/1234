package com.app_mobile.campusexpensemanager.SQLite;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class ExpenseDb extends SQLiteOpenHelper {

    private static final String DB_NAME = "expense_db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "expenses";

    // Define columns
    private static final String ID_COL = "id";
    private static final String DESCRIPTION_COL = "description";
    private static final String DATE_COL = "date";
    private static final String AMOUNT_COL = "amount";
    private static final String CATEGORY_COL = "category";

    public ExpenseDb(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the expenses table
        String query = "CREATE TABLE " + TABLE_NAME + " ("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + DESCRIPTION_COL + " TEXT, "
                + DATE_COL + " TEXT, "
                + AMOUNT_COL + " REAL, "
                + CATEGORY_COL + " TEXT)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Method to add a new expense
    public long addExpense(String description, String date, double amount, String category) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(DESCRIPTION_COL, description);
        values.put(DATE_COL, date);
        values.put(AMOUNT_COL, amount);
        values.put(CATEGORY_COL, category);
        long result = db.insert(TABLE_NAME, null, values);
        db.close();
        return result;
    }

    // Method to get all expenses
    public Cursor getAllExpenses() {
        SQLiteDatabase db = this.getReadableDatabase();
        String query = "SELECT * FROM " + TABLE_NAME;
        return db.rawQuery(query, null);
    }

    // Method to get a single expense by ID
    public Cursor getExpenseById(long id) {
        SQLiteDatabase db = this.getReadableDatabase();
        return db.query(
                "expenses",
                null, // Select all columns
                "id = ?",
                new String[]{String.valueOf(id)},
                null,
                null,
                null
        );
    }

    public int updateExpense(long id, ContentValues values) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.update(
                "expenses",
                values,
                "id = ?",
                new String[]{String.valueOf(id)}
        );
    }

    public int deleteExpense(long id) {
        SQLiteDatabase db = this.getWritableDatabase();
        return db.delete(
                "expenses",
                "id = ?",
                new String[]{String.valueOf(id)}
        );
    }
}
