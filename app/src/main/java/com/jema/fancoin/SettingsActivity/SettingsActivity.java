package com.jema.fancoin.SettingsActivity;

import static com.jema.fancoin.Home.SHARED_PREFS;
import static com.jema.fancoin.Home.UAPPLICATION_STATUS;
import static com.jema.fancoin.Home.UBIO;
import static com.jema.fancoin.Home.UCATEGORY;
import static com.jema.fancoin.Home.UEMAIL;
import static com.jema.fancoin.Home.UFOLLOWERS;
import static com.jema.fancoin.Home.UFOLLOWING;
import static com.jema.fancoin.Home.UFULLNAME;
import static com.jema.fancoin.Home.UID;
import static com.jema.fancoin.Home.UIMAGE;
import static com.jema.fancoin.Home.UNAME;
import static com.jema.fancoin.Home.UPHONE;
import static com.jema.fancoin.Home.UPRICING;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.jema.fancoin.Home;
import com.jema.fancoin.R;

import java.util.List;

public class SettingsActivity extends AppCompatActivity {

    private ConstraintLayout profileSetting, themeSetting, languageSetting, phoneSetting, emailSetting, manageVideosSetting, accountSetting;

    private ImageView back;
    private FirebaseAuth firebaseAuth;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);


        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();

        profileSetting = findViewById(R.id.profileSetting);
        accountSetting = findViewById(R.id.accountSetting);
        manageVideosSetting = findViewById(R.id.manageVideosSetting);
        emailSetting = findViewById(R.id.emailSetting);
        phoneSetting = findViewById(R.id.phoneSetting);
        languageSetting = findViewById(R.id.languageSetting);
        themeSetting = findViewById(R.id.themeSetting);
        back = findViewById(R.id.settings_back_btn);

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        profileSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, SettingsProfileActivity.class);
                startActivity(intent);
            }
        });
        accountSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, SettingsAccountInformationActivity.class);
                startActivity(intent);
            }
        });
        manageVideosSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, SettingsManageVideosActivity.class);
                startActivity(intent);
            }
        });
        emailSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, SettingsEmailActivity.class);
                startActivity(intent);
            }
        });
        phoneSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, SettingsPhoneActivity.class);
                startActivity(intent);
            }
        });
        languageSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, SettingsLanguageActivity.class);
                startActivity(intent);
            }
        });
        themeSetting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, SettingsThemeActivity.class);
                startActivity(intent);
            }
        });

//        listen to data changes
        UserDataListener();
    }

    public void UserDataListener() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

//        we will get all the data about the current user and store it locally
        db.collection("Users").document(auth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firebase Error", error.getMessage());
                    return;
                }


                String temp_username = value.getString("username");
                String temp_fullname = value.getString("name");
                String temp_status = value.getString("application_status");
                String temp_bio = value.getString("bio");
                String temp_category = value.getString("category");
                String temp_email = value.getString("email");
                String temp_phone = value.getString("phone");
                String temp_pricing = value.getString("pricing");
                String temp_id = value.getString("id");
                String temp_image = value.getString("image");
                List<String> myFollowers = (List<String>) value.get("followers");
                List<String> myFollowing = (List<String>) value.get("following");

                if (temp_id != null) {
                    editor.putString(UID, temp_id);
                }
                if (temp_username != null) {
                    editor.putString(UNAME, temp_username);
                }
                if (temp_fullname != null) {
                    editor.putString(UFULLNAME, temp_fullname);
                }
                if (temp_status != null) {
                    editor.putString(UAPPLICATION_STATUS, temp_status);
                }
                if (temp_bio != null) {
                    editor.putString(UBIO, temp_bio);
                }
                if (temp_category != null) {
                    editor.putString(UCATEGORY, temp_category);
                }
                if (temp_email != null) {
                    editor.putString(UEMAIL, temp_email);
                }
                if (temp_bio != null) {
                    editor.putString(UID, temp_bio);
                }
                if (temp_image != null) {
                    editor.putString(UIMAGE, temp_image);
                }
                if (temp_phone != null) {
                    editor.putString(UPHONE, temp_phone);
                }
                if (temp_pricing != null) {
                    editor.putString(UPRICING, temp_pricing);
                }


                if(myFollowers != null) {
                    editor.putString(UFOLLOWERS, String.valueOf(myFollowers.size()));
                }
                if(myFollowers != null) {
                    editor.putString(UFOLLOWING, String.valueOf(myFollowing.size()));
                }
                editor.commit(); // persist the values

                Log.d("JemaTag", "User Data Retrieved");
            }
        });
    }
}