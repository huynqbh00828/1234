package com.app_mobile.campusexpensemanager.SQLite;

import android.annotation.SuppressLint;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class BalanceDb extends SQLiteOpenHelper {

    private static final String DB_NAME = "balance_db";
    private static final int DB_VERSION = 1;
    private static final String TABLE_NAME = "balance";

    // Define columns
    private static final String ID_COL = "id";
    private static final String BALANCE_COL = "balance";

    public BalanceDb(Context context) {
        super(context, DB_NAME, null, DB_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        // Create the balance table
        String query = "CREATE TABLE " + TABLE_NAME + "("
                + ID_COL + " INTEGER PRIMARY KEY AUTOINCREMENT, "
                + BALANCE_COL + " REAL)";
        db.execSQL(query);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_NAME);
        onCreate(db);
    }

    // Method to add or update balance
    public long setBalance(double amount) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(BALANCE_COL, amount);

        // Check if a row already exists
        Cursor cursor = db.query(TABLE_NAME, new String[]{ID_COL},
                null, null, null, null, null);
        long result;
        if (cursor.getCount() > 0) {
            // Row exists, update it
            cursor.moveToFirst();
            int id = cursor.getInt(cursor.getColumnIndex(ID_COL));
            result = db.update(TABLE_NAME, values, ID_COL + " = ?", new String[]{String.valueOf(id)});
        } else {
            // No rows exist, insert a new one
            result = db.insert(TABLE_NAME, null, values);
        }
        cursor.close();
        db.close();
        return result;
    }

    // Method to get current balance
    @SuppressLint("Range")
    public double getBalance() {
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.query(TABLE_NAME, new String[]{BALANCE_COL},
                null, null, null, null, null);

        double balance = 0.0;
        if (cursor.moveToFirst()) {
            balance = cursor.getDouble(cursor.getColumnIndex(BALANCE_COL));
        }
        cursor.close();
        return balance;
    }
}
