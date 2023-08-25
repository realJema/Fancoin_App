package com.jema.fancoin.UserProfile.SettingsActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.jema.fancoin.Utils.LanguageManager;
import com.jema.fancoin.Home;
import com.jema.fancoin.R;

public class SettingsLanguageActivity extends AppCompatActivity {

    ConstraintLayout frBtn, enBtn;
    ImageView frCheck, enCheck, backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_language);

        frBtn = findViewById(R.id.settings_lang_fr_btn);
        enBtn = findViewById(R.id.settings_lang_en_btn);
        enCheck = findViewById(R.id.settings_lang_en_check);
        frCheck = findViewById(R.id.settings_lang_fr_check);
        backBtn = findViewById(R.id.back2);

        LanguageManager lang = new LanguageManager(this);

        if(lang.getLang().equalsIgnoreCase("fr")){
            enCheck.setVisibility(View.GONE);
            frCheck.setVisibility(View.VISIBLE);
        } else {
            enCheck.setVisibility(View.VISIBLE);
            frCheck.setVisibility(View.GONE);
        }


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(SettingsLanguageActivity.this, SettingsActivity.class);
                startActivity(myIntent);

                finish();
            }
        });

        frBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lang.updateResource("fr");
                enCheck.setVisibility(View.GONE);
                frCheck.setVisibility(View.VISIBLE);
                recreate();
            }
        });

        enBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lang.updateResource("en");
                enCheck.setVisibility(View.VISIBLE);
                frCheck.setVisibility(View.GONE);
                recreate();
            }
        });


    }


    //    removing flickers
    @Override
    public void recreate() {
        finish();
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

        Intent intent = new Intent(SettingsLanguageActivity.this, Home.class);
        startActivity(intent);
        overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);

    }

}