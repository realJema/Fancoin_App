package com.jema.fancoin;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

public class OrderReplyActivity extends AppCompatActivity {
    private TextView id, date, descr, recipient, starName, description, pricing;
    private ImageView pp, backBtn;
    private Button replyBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_reply);

        Intent i = getIntent();

//        getting post data and passing from previous activity
        String iDate = i.getStringExtra("date");
        String iDescription = i.getStringExtra("description");
        String iRecipient = i.getStringExtra("recipient");
        String iId = i.getStringExtra("id");
        String DocumentId = i.getStringExtra("DocumentId");
        Log.d("JemaTag", DocumentId);
        String star_image = i.getStringExtra("star_image");
        String star_pricing = i.getStringExtra("star_pricing");
        String star_name = i.getStringExtra("star_name");

//        getting textviews to put data
        id = findViewById(R.id.order_details_id_text);
        date = findViewById(R.id.order_details_date_text);
        descr = findViewById(R.id.order_details_descr_text);
        recipient = findViewById(R.id.order_details_recipient_text);
        pp = findViewById(R.id.order_details_profile_image);
        starName = findViewById(R.id.order_details_star_name);
        pricing = findViewById(R.id.order_details_pricing);
        backBtn = findViewById(R.id.order_details_back_btn);
        replyBtn = findViewById(R.id.reply_upload_btn);

//        setting values in textviews

        id.setText(iId);
        date.setText(iDate);
        descr.setText(iDescription);
        recipient.setText(iRecipient);
        starName.setText(star_name);
        pricing.setText("Pricing : ".concat(star_pricing));

        Picasso.get().load(star_image).into(pp);


        replyBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(OrderReplyActivity.this, UploadVideoActivity.class);
                i.putExtra("DocumentId", DocumentId);
                startActivity(i);
                finish();

            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

    }

}