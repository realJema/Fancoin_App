package com.jema.fancoin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import com.jema.fancoin.SettingsActivity.SettingsActivity;

public class ApplicationActivity extends AppCompatActivity {
    private ImageView back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);


        back = findViewById(R.id.apply_back_btn);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(ApplicationActivity.this, Home.class);
                startActivity(i);
                finish();
            }
        });
    }
}