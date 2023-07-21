package com.jema.fancoin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jema.fancoin.SettingsActivity.SettingsActivity;
import com.squareup.picasso.Picasso;

public class SuccessOrderActivity extends AppCompatActivity {

    private ImageView backBtn, pp;
    private TextView title;
    String name, image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_order);

        pp = findViewById(R.id.success_order_profile_image);
        backBtn = findViewById(R.id.success_order_back_btn);
        title = findViewById(R.id.success_order_title);


        Intent i = getIntent();

        name = i.getStringExtra("name");
        image = i.getStringExtra("image");

        title.setText("Order Successful to ".concat(name));
        Picasso.get().load(image).into(pp);

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}