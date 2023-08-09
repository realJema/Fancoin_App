package com.jema.fancoin.SettingsActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import com.jema.fancoin.LanguageManager;
import com.jema.fancoin.R;

public class SettingsLanguageActivity extends AppCompatActivity {

    ConstraintLayout frBtn, enBtn;
    ImageView frCheck, enCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_language);

        frBtn = findViewById(R.id.settings_lang_fr_btn);
        enBtn = findViewById(R.id.settings_lang_en_btn);
        enCheck = findViewById(R.id.settings_lang_en_check);
        frCheck = findViewById(R.id.settings_lang_fr_check);

        LanguageManager lang = new LanguageManager(this);

        if(lang.getLang().equalsIgnoreCase("fr")){
            enCheck.setVisibility(View.GONE);
            frCheck.setVisibility(View.VISIBLE);
        } else {
            enCheck.setVisibility(View.VISIBLE);
            frCheck.setVisibility(View.GONE);
        }

        frBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("JemaTag", "changed to french");
                lang.updateResource("fr");
                enCheck.setVisibility(View.GONE);
                frCheck.setVisibility(View.VISIBLE);
                recreate();
            }
        });

        enBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("JemaTag", "changed to english");
                lang.updateResource("en");
                enCheck.setVisibility(View.VISIBLE);
                frCheck.setVisibility(View.GONE);
                recreate();
            }
        });


    }

}