package com.app_mobile.campusexpensemanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.app_mobile.campusexpensemanager.Model.User;
import com.app_mobile.campusexpensemanager.SQLite.UserDb;

public class SignInActivity extends AppCompatActivity {

    private EditText edtUsername, edtPassword;
    private Button btnSignIn, btnSignUp;
    private UserDb userDb;
    private TextView tvError;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        edtUsername = findViewById(R.id.edtUsernameSignIn);
        edtPassword = findViewById(R.id.edtPassSignIn);
        btnSignIn = findViewById(R.id.btnSignIn);
        btnSignUp = findViewById(R.id.btnRegister);
        tvError = findViewById(R.id.tvError);
        userDb = new UserDb(SignInActivity.this);

        btnSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                login();
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SignInActivity.this, SignUpActivity.class);
                startActivity(intent);
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void login() {
        String username = edtUsername.getText().toString().trim();
        String password = edtPassword.getText().toString().trim();
        if (TextUtils.isEmpty(username)) {
            edtUsername.setError("Username cannot be empty");
            return;
        }
        if (TextUtils.isEmpty(password)) {
            edtPassword.setError("Password cannot be empty");
            return;
        }

        User infoUser = userDb.getSingleUser(username, password);
        if (infoUser.getEmail() != null && infoUser.getPhone() != null) {
            tvError.setText("");
            Intent intent = new Intent(SignInActivity.this, SettingsActivity.class);
            intent.putExtra("username", infoUser.getUsername());
            startActivity(intent);
            finish();
        } else {
            tvError.setText("Account Invalid");
        }
    }
}

