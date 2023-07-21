package com.jema.fancoin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class ApplicationSuccessActivity extends AppCompatActivity {

    private ImageView pp, backBtn;
    private String name, image;
    private TextView title;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application_success);

        pp = findViewById(R.id.application_success_profile_image);
        title = findViewById(R.id.application_success_order_title);
        backBtn = findViewById(R.id.application_success_back_btn);


        Intent i = getIntent();
        name = i.getStringExtra("name");
        image = i.getStringExtra("image");


        Picasso.get().load(image).into(pp);
        title.setText("Application Submitted for ".concat(name));


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}