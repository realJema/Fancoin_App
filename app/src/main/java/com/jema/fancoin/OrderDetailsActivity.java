package com.jema.fancoin;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.squareup.picasso.Picasso;

public class OrderDetailsActivity extends AppCompatActivity {
    private TextView id, date, descr, recipient, starName, description, pricing, bottomTitle, bottomSubtitle;
    private ImageView pp, backBtn;
    private Button bottomBtn;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    String iDate, documentId,iDescription, iRecipient, iId, star_image, star_pricing, star_name, personalized_video = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_details);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        Intent i = getIntent();

//        getting post data and passing from previous activity
        iDate = i.getStringExtra("date");
        iDescription = i.getStringExtra("description");
        iRecipient = i.getStringExtra("recipient");
        iId = i.getStringExtra("id");
        documentId = i.getStringExtra("DocumentId");
        star_image = i.getStringExtra("star_image");
        star_pricing = i.getStringExtra("star_pricing");
        star_name = i.getStringExtra("star_name");
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

        bottomTitle = findViewById(R.id.order_details_bottomCard_title);
        bottomSubtitle = findViewById(R.id.order_details_bottomCard_subtitle);
        bottomBtn = findViewById(R.id.order_details_bottom_btn);

        bottomTitle.setText(star_name.concat(" has not replied to you yet"));
        bottomSubtitle.setText("Waiting for ".concat(star_name).concat(" to reply!"));
        bottomBtn.setVisibility(View.GONE);

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

        bottomBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(personalized_video != null){
                    Intent myIntent = new Intent(OrderDetailsActivity.this, WatchVideoActivity.class);
                    myIntent.putExtra("video_url", personalized_video);
                    startActivity(myIntent);
                }

            }
        });

        orderListener(documentId);
    }

    public void orderListener(String documentID) {
        Log.d("JemaTag", documentID);
        db.collection("Orders").document(documentID).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firebase Error", error.getMessage());
                    return;
                }

                String videoUrl = value.getString("video_url");

                if(videoUrl != null && !videoUrl.equalsIgnoreCase("")){
                    personalized_video = videoUrl;
                    bottomTitle.setText(star_name.concat(" replied"));
                    bottomSubtitle.setText("Your personallized video from ".concat(star_name).concat(" is ready!"));
                    bottomBtn.setVisibility(View.VISIBLE);
                } else {
                    bottomTitle.setText(star_name.concat(" has not replied to you yet"));
                    bottomSubtitle.setText("Waiting for ".concat(star_name).concat(" to reply!"));
                    bottomBtn.setVisibility(View.GONE);
                }
            }
        });
    }
}