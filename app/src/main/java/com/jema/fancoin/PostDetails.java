package com.jema.fancoin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.common.MimeTypes;
import androidx.media3.datasource.DefaultDataSource;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.exoplayer.source.ProgressiveMediaSource;
import androidx.media3.ui.PlayerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.MediaController;
import android.widget.TextView;
import android.widget.VideoView;

import com.squareup.picasso.Picasso;


public class PostDetails extends AppCompatActivity {

    ImageView img, back, pp;
    TextView proName, proPrice, proDesc, proCategory;

    String name, price, desc, cat, image, id;
    Button orderVideo;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        Intent i = getIntent();

        name = i.getStringExtra("name");
        price = i.getStringExtra("price");
        desc = i.getStringExtra("bio");
        cat = i.getStringExtra("category");
        image = i.getStringExtra("image");
        id = i.getStringExtra("id");

        proName = findViewById(R.id.productName);
        proDesc = findViewById(R.id.prodBio);
//        proPrice = findViewById(R.id.prodPrice);
        img = findViewById(R.id.big_image);
        back = findViewById(R.id.back2);
        proCategory = findViewById(R.id.prodCategory);
        pp = findViewById(R.id.details_profile_image);
        orderVideo = findViewById(R.id.details_get_video_btn);

        proName.setText(name);
//        proPrice.setText(price);
        proDesc.setText(desc);
        proCategory.setText(cat);


        Picasso.get().load(image).into(img);
        Picasso.get().load(image).into(pp);


        orderVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(PostDetails.this, OrderVideoActivity.class);
                i.putExtra("name", proName.getText());
//                i.putExtra("image", postCardArrayList.get(position).getBigimageurl());
                i.putExtra("price", price);
                i.putExtra("bio", proDesc.getText());
                i.putExtra("category", proCategory.getText());
                i.putExtra("image", image);
                i.putExtra("id", id);

                PostDetails.this.startActivity(i);
            }
        });
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