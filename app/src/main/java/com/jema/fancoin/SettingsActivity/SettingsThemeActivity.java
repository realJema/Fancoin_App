package com.jema.fancoin.SettingsActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.jema.fancoin.R;
import com.jema.fancoin.ThemeManager;

public class SettingsThemeActivity extends AppCompatActivity {
    private ImageView lightCheck, darkCheck, systemCheck, backBtn;
    ConstraintLayout lightBtn, darkBtn, systemBtn;

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
        backBtn = findViewById(R.id.back2);

        ThemeManager theme = new ThemeManager(this);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        lightBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theme.updateTheme("1");
                lightCheck.setVisibility(View.VISIBLE);
                darkCheck.setVisibility(View.GONE);
                systemCheck.setVisibility(View.GONE);
                recreate();
            }
        });

        darkBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theme.updateTheme("2");
                lightCheck.setVisibility(View.GONE);
                darkCheck.setVisibility(View.VISIBLE);
                systemCheck.setVisibility(View.GONE);
                recreate();
            }
        });

        systemBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                theme.updateTheme("3");
                lightCheck.setVisibility(View.GONE);
                darkCheck.setVisibility(View.GONE);
                systemCheck.setVisibility(View.VISIBLE);
                recreate();
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

        startActivity(getIntent());
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }
}