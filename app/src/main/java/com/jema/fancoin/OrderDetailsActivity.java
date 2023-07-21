package com.jema.fancoin;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import org.ocpsoft.prettytime.PrettyTime;

public class OrderDetailsActivity extends AppCompatActivity {
    private TextView id, date, descr, recipient, starName, description, pricing;
    private ImageView pp, backBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);


        Intent i = getIntent();

//        getting post data and passing from previous activity
        String iDate = i.getStringExtra("date");
        String iDescription = i.getStringExtra("description");
        String iRecipient = i.getStringExtra("recipient");
        String iId = i.getStringExtra("id");
//        String star_uid = i.getStringExtra("star_uid");
        String star_image = i.getStringExtra("star_image");
        String star_pricing = i.getStringExtra("star_pricing");
        String star_name = i.getStringExtra("star_name");
//        String client_uid = i.getStringExtra("client_uid");
//        String client_image = i.getStringExtra("client_image");
//        String client_name = i.getStringExtra("client_name");
//        String client_phonenumber = i.getStringExtra("client_phonenumber");
//        String client_email = i.getStringExtra("client_email");

//        getting textviews to put data
        id = findViewById(R.id.order_details_id_text);
        date = findViewById(R.id.order_details_date_text);
        descr = findViewById(R.id.order_details_descr_text);
        recipient = findViewById(R.id.order_details_recipient_text);
        pp = findViewById(R.id.order_details_profile_image);
        starName = findViewById(R.id.order_details_star_name);
        pricing = findViewById(R.id.order_details_pricing);
        backBtn = findViewById(R.id.order_details_back_btn);

//        setting values in textviews

        id.setText(iId);
        date.setText(iDate);
        descr.setText(iDescription);
        recipient.setText(iRecipient);
        starName.setText(star_name);
        pricing.setText("Pricing : ".concat(star_pricing));

        Picasso.get().load(star_image).into(pp);


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}