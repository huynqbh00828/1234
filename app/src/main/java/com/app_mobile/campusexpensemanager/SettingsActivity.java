package com.app_mobile.campusexpensemanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import androidx.appcompat.app.AppCompatActivity;
import androidx.annotation.NonNull;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import android.view.MenuItem;

public class SettingsActivity extends AppCompatActivity {

    private Button editProfileButton;
    private Button securityButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        editProfileButton = findViewById(R.id.editProfileButton);
        securityButton = findViewById(R.id.securityButton);



        editProfileButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Edit Profile screen
                Intent intent = new Intent(SettingsActivity.this, EditProfileActivity.class);
                startActivity(intent);
            }
        });

        securityButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Navigate to Security screen
                Intent intent = new Intent(SettingsActivity.this, SecurityActivity.class);
                startActivity(intent);
            }
        });
    }
}
