package com.jema.fancoin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.jema.fancoin.SettingsActivity.SettingsActivity;

public class SuccessOrderActivity extends AppCompatActivity {

    private ImageView backBtn, pp;
    private TextView title;
    String name;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_success_order);

        pp = findViewById(R.id.success_order_profile_image);
        backBtn = findViewById(R.id.success_order_back_btn);
        title = findViewById(R.id.success_order_title);


        Intent i = getIntent();

        name = i.getStringExtra("name");

        title.setText("Order Successful to ".concat(name));



        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SuccessOrderActivity.this, Home.class);
                startActivity(i);
                finish();
            }
        });
    }
}