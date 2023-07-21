package com.jema.fancoin;

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

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.auth.User;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.List;

public class UserAdministrationActivity extends AppCompatActivity {


    TextView name, bio, price, category, followers, social, username, id;

    String iId;
    Button confirm, reject, pending;
    ImageView image, back;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_administration);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        id = findViewById(R.id.admin_user_id);
        name = findViewById(R.id.admin_user_name);
        category = findViewById(R.id.admin_user_category);
        image = findViewById(R.id.admin_user_pp);
        followers = findViewById(R.id.admin_user_followers);
        social = findViewById(R.id.admin_user_social);
        username = findViewById(R.id.admin_user_name_2);
        back = findViewById(R.id.admin_user_back);
        confirm = findViewById(R.id.admin_user_confirm_btn);
        reject = findViewById(R.id.admin_user_reject_btn);
        pending = findViewById(R.id.admin_user_pending_btn);


        Intent i = getIntent();

//        getting post data and passing from previous activity

        iId = i.getStringExtra("id");
        String iName = i.getStringExtra("name");
        String iPrice = i.getStringExtra("price");
        String iBio = i.getStringExtra("bio");
        String iCat = i.getStringExtra("category");
        String iImage = i.getStringExtra("image");
        String iFollowers = i.getStringExtra("followers");
        String iSocial = i.getStringExtra("social");

        id.setText(iId);
        name.setText(iName);
        category.setText(iCat);
        followers.setText(iFollowers);
        social.setText(iSocial);
        username.setText(iName);
        Picasso.get().load(iImage).into(image);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                        adding data into document of user
                HashMap<String , Object> user = new HashMap<>();
                user.put("application_status" , "confirmed");

                db.collection("Users").document(iId).update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UserAdministrationActivity.this, "Status Updated", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                        adding data into document of user
                HashMap<String , Object> user = new HashMap<>();
                user.put("application_status" , "rejected");

                db.collection("Users").document(iId).update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UserAdministrationActivity.this, "Status Updated", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
        pending.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                        adding data into document of user
                HashMap<String , Object> user = new HashMap<>();
                user.put("application_status" , "pending");

                db.collection("Users").document(iId).update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(UserAdministrationActivity.this, "Status Updated", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        EventChangeListener();

    }


    private void EventChangeListener() {

        db.collection("Users").document(iId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null) {
                            Log.e("Firebase Error", error.getMessage());
                            return;
                        }
                        String stat = (String) value.get("application_status");
                        if(stat != null){
                            followers.setText(stat);

                        }

                    }

                });
    }
}