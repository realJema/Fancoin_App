package com.jema.fancoin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

import androidx.appcompat.app.AppCompatActivity;

import com.jema.fancoin.Onboarding.Auth.Login;

public class UploadSuccessActivity extends AppCompatActivity {

    ImageView bactBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_success);

        bactBtn = findViewById(R.id.upload_success_back_btn);


        bactBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent intent = new Intent(UploadSuccessActivity.this, Home.class);
                startActivity(intent);
                finish();
            }
        });

    }
}