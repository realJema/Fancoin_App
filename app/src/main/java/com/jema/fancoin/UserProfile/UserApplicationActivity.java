package com.jema.fancoin.UserProfile;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jema.fancoin.R;
import com.jema.fancoin.Database.User;
import com.jema.fancoin.Database.UserViewModel;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

public class UserApplicationActivity extends AppCompatActivity {
    private ImageView back, pp;
    private TextView email, name, username, followers;
    private Spinner category, social;
    private Button submit;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    private UserViewModel viewModel;
    String myName, myPP;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_application);


        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        back = findViewById(R.id.apply_back_btn);
        email = findViewById(R.id.apply_email_input);
        name = findViewById(R.id.apply_full_name_input);
        username = findViewById(R.id.apply_username_input);
        followers = findViewById(R.id.apply_followers_number_input);
        category = findViewById(R.id.apply_category);
        social = findViewById(R.id.apply_social);
        submit = findViewById(R.id.apply_submit_btn);
        pp = findViewById(R.id.application_profile_image);

        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        viewModel.getUserInfo().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user.username != null) {
//        setting elements in drawer
                    name.setText(user.full_name);
                    email.setText(user.email);
                    Picasso.get().load(user.image).into(pp);

                    myName = user.full_name;
                    myPP = user.image;
                } else {
                    name.setText("empty");
                    email.setText("empty");
//                    Picasso.get().load(user.image).into(draw_pp); // put local image
                }
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String uUseranme = username.getText().toString();
                String ufollowers = followers.getText().toString();
                String uCategory = category.getSelectedItem().toString();
                String uSocial = social.getSelectedItem().toString();

                if (uUseranme.equalsIgnoreCase("") || ufollowers.equalsIgnoreCase("") || uCategory.equalsIgnoreCase("Select a Category") || uSocial.equalsIgnoreCase("Select a Social Media")){
                    Toast.makeText(UserApplicationActivity.this, R.string.please_fill_all_fields_form, Toast.LENGTH_SHORT).show();
                } else {
                    HashMap<String , Object> user = new HashMap<>();
                    user.put("application_username" , uUseranme);
                    user.put("application_followers", ufollowers);
                    user.put("category", uCategory);
                    user.put("application_social", uSocial);
                    user.put("application_status", "pending");

                    db.collection("Users").document(auth.getCurrentUser().getUid()).update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(UserApplicationActivity.this, R.string.application_submitted, Toast.LENGTH_SHORT).show();
                            Intent myIntent = new Intent(UserApplicationActivity.this, UserApplicationSuccessActivity.class);
                            myIntent.putExtra("name", myName);
                            myIntent.putExtra("image", myPP);
                            startActivity(myIntent);
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(UserApplicationActivity.this, R.string.unable_to_submit_form, Toast.LENGTH_SHORT).show();
                        }
                    });
                }
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}