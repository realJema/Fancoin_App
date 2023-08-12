package com.jema.fancoin;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.squareup.picasso.Picasso;

public class ActionSuccessActivity extends AppCompatActivity {

    private TextView title, subtitle;
    private ImageView backBtn, image, icon;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_action_success);

        backBtn = findViewById(R.id.action_success_back_btn);
        title = findViewById(R.id.action_success_title);
        subtitle = findViewById(R.id.action_success_subtitle);
        image = findViewById(R.id.action_success_image);
        icon = findViewById(R.id.action_success_icon);

        Intent i = getIntent();
//        getting post data and passing from previous activity

        String myTitle = i.getStringExtra("title");
        String mySubtitle = i.getStringExtra("subtitle");
        String myImage = i.getStringExtra("image");

        title.setText(myTitle);
        subtitle.setText(mySubtitle);

        Picasso.get().load(myImage).into(image);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent myIntent = new Intent(ActionSuccessActivity.this, Home.class);
                startActivity(myIntent);
                finish();
            }
        });

    }
}