package com.app_mobile.campusexpensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app_mobile.campusexpensemanager.SQLite.UserDb;

public class SignUpActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword, edtEmail, edtPhone;
    private Button btnSubmit;
    private UserDb userDb;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        edtUsername = findViewById(R.id.edtUser);
        edtPassword = findViewById(R.id.edtPass);
        edtEmail = findViewById(R.id.edtEmail);
        edtPhone = findViewById(R.id.edtPhoneNumber);
        btnSubmit = findViewById(R.id.btnSignUp);

        userDb = new UserDb(SignUpActivity.this);

        btnSubmit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                insertUser();
            }
        });
    }

    private void insertUser() {
        String user = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        String email = edtEmail.getText().toString().trim();
        String phone = edtPhone.getText().toString().trim();

        if (TextUtils.isEmpty(user)) {
            edtUsername.setError("Username cannot be empty");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            edtPassword.setError("Password cannot be empty");
            return;
        }
        if (TextUtils.isEmpty(email)) {
            edtEmail.setError("Email cannot be empty");
            return;
        }
        if (TextUtils.isEmpty(phone)) {
            edtPhone.setError("Phone cannot be empty");
            return;
        }

        long result = userDb.addNewUser(user, password, email, phone);
        if (result == -1) {
            Toast.makeText(SignUpActivity.this, "Sign up failed", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(SignUpActivity.this, "Sign up successful", Toast.LENGTH_SHORT).show();
            Intent intent = new Intent(SignUpActivity.this, SignInActivity.class);
            startActivity(intent);
            finish(); // Finish the current activity
        }
    }
}