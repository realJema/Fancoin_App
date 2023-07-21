package com.jema.fancoin.SettingsActivity;

import static com.jema.fancoin.Home.SHARED_PREFS;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.jema.fancoin.Home;
import com.jema.fancoin.Login;
import com.jema.fancoin.R;

public class SettingsThemeActivity extends AppCompatActivity {
    private ImageView lightCheck, darkCheck, systemCheck;
    ConstraintLayout lightBtn, darkBtn, systemBtn;
    public static final String THEME = "1";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_theme);

        lightCheck = findViewById(R.id.settings_theme_light_check);
        darkCheck = findViewById(R.id.settings_theme_dark_check);
        systemCheck = findViewById(R.id.settings_theme_system_check);
        lightBtn = findViewById(R.id.settings_theme_light_btn);
        darkBtn  = findViewById(R.id.settings_theme_dark_btn);
        systemBtn  = findViewById(R.id.settings_theme_system_btn);


        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();


        lightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
                lightCheck.setVisibility(View.VISIBLE);
                darkCheck.setVisibility(View.GONE);
                systemCheck.setVisibility(View.GONE);
                editor.putString(THEME, "1");
                editor.commit(); // persist the values
            }
        });

        darkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
                lightCheck.setVisibility(View.GONE);
                darkCheck.setVisibility(View.VISIBLE);
                systemCheck.setVisibility(View.GONE);
                editor.putString(THEME, "2");
                editor.commit(); // persist the values
            }
        });

        systemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
                lightCheck.setVisibility(View.GONE);
                darkCheck.setVisibility(View.GONE);
                systemCheck.setVisibility(View.VISIBLE);
                editor.putString(THEME, "3");
                editor.commit(); // persist the values
            }
        });

        int isNightModeOn = AppCompatDelegate.getDefaultNightMode();
        if(isNightModeOn == 1){
            lightCheck.setVisibility(View.VISIBLE);
            darkCheck.setVisibility(View.GONE);
            systemCheck.setVisibility(View.GONE);
        }else if(isNightModeOn == 2) {
            lightCheck.setVisibility(View.GONE);
            darkCheck.setVisibility(View.VISIBLE);
            systemCheck.setVisibility(View.GONE);
        } else {
            lightCheck.setVisibility(View.GONE);
            darkCheck.setVisibility(View.GONE);
            systemCheck.setVisibility(View.VISIBLE);
        }
    }

//    removing flickers
    @Override
    public void recreate() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);


        Intent intent = new Intent(SettingsThemeActivity.this, SettingsThemeActivity.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }
}