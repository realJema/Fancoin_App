package com.jema.fancoin;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.jema.fancoin.SettingsActivity.SettingsAccountInformationActivity;
import com.squareup.picasso.Picasso;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class PostDetails extends AppCompatActivity {

    ImageView img, back, pp;
    TextView proName, proPrice, proDesc, proCategory, follow;

    String name, price, desc, cat, image, id;
    Button orderVideo;
    private FirebaseAuth auth;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

        Intent i = getIntent();
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

//        getting post data and passing from previous activity

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
        follow = findViewById(R.id.details_follow_btn);
        orderVideo = findViewById(R.id.details_get_video_btn);

        proName.setText(name);
//        proPrice.setText(price);
        proDesc.setText(desc);
        proCategory.setText(cat);


        Picasso.get().load(image).into(img);
        Picasso.get().load(image).into(pp);

        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                db.collection("Users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        DocumentSnapshot document = task.getResult();
                        List<String> group = (List<String>) document.get("followers");
                        String currendId = auth.getCurrentUser().getUid();

                        if(group != null){
                            if(!group.contains(id)){
                                group.add(id); // adding current user id
                            } else {
                                while (group.contains(id)) {
                                    group.remove(id);
                                }
                            }
                        } else {
                            group = new ArrayList<String>() {{
                                add(id);
                            }};

                        }
                        db.collection("Users").document(currendId).update("followers", group).addOnSuccessListener(new OnSuccessListener<Void>() {
                            @Override
                            public void onSuccess(Void unused) {
                                Toast.makeText(PostDetails.this, "Followed", Toast.LENGTH_SHORT).show();
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(PostDetails.this, "Unable to update", Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                });
            }
        });


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


        EventChangeListener();
    }

    private void EventChangeListener() {

        db.collection("Users").document(auth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null) {
                            Log.e("Firebase Error", error.getMessage());
                            return;
                        }
                        List<String> group = (List<String>) value.get("followers");


                        if(group != null){
                            if(group.contains(id)){
                                follow.setText("Unfollow");
                            } else {
                                follow.setText("Follow");
                            }

                        }

                    }

                });
    }
}