package com.jema.fancoin.UserProfile.SettingsActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.jema.fancoin.R;

public class SettingsActivity extends AppCompatActivity {

    private ConstraintLayout profileSetting, themeSetting, languageSetting, phoneSetting, emailSetting, manageVideosSetting, accountSetting;

    private ImageView back;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();

        profileSetting = findViewById(R.id.profileSetting);
        accountSetting = findViewById(R.id.accountSetting);
        manageVideosSetting = findViewById(R.id.manageVideosSetting);
        emailSetting = findViewById(R.id.emailSetting);
        phoneSetting = findViewById(R.id.phoneSetting);
        languageSetting = findViewById(R.id.languageSetting);
        themeSetting = findViewById(R.id.themeSetting);
        back = findViewById(R.id.settings_back_btn);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        profileSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, SettingsProfileActivity.class);
                startActivity(intent);
            }
        });
        accountSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, SettingsAccountInformationActivity.class);
                startActivity(intent);
            }
        });
        manageVideosSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, SettingsManageVideosActivity.class);
                startActivity(intent);
            }
        });
        emailSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, SettingsEmailActivity.class);
                startActivity(intent);
            }
        });
        phoneSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, SettingsPhoneActivity.class);
                startActivity(intent);
            }
        });
        languageSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, SettingsLanguageActivity.class);
                startActivity(intent);
            }
        });
        themeSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, SettingsThemeActivity.class);
                startActivity(intent);
            }
        });
    }

}