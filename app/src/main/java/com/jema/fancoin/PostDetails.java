package com.jema.fancoin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class PostDetails extends AppCompatActivity {

    ImageView img, back;
    TextView proName, proPrice, proDesc, proCategory;

    String name, price, desc, cat;
    int image;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        Intent i = getIntent();

        name = i.getStringExtra("name");
        image = i.getIntExtra("image", R.drawable.cardbg);
        price = i.getStringExtra("price");
        desc = i.getStringExtra("bio");
        cat = i.getStringExtra("category");

        proName = findViewById(R.id.productName);
        proDesc = findViewById(R.id.prodBio);
        proPrice = findViewById(R.id.prodPrice);
        img = findViewById(R.id.big_image);
        back = findViewById(R.id.back2);
        proCategory = findViewById(R.id.prodCategory);

        proName.setText(name);
        proPrice.setText(price);
        proDesc.setText(desc);
        proCategory.setText(cat);


        img.setImageResource(image);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(PostDetails.this, Home.class);
                startActivity(i);
                finish();
            }
        });

    }
}
