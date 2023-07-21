package com.jema.fancoin.SettingsActivity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jema.fancoin.PostDetails;
import com.jema.fancoin.R;
import com.squareup.picasso.Picasso;

import java.util.HashMap;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class SettingsAccountInformationActivity extends AppCompatActivity {
    private Spinner category;
    private EditText pricing, bio;
    private ImageView back;
    private Button save;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_account_information);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        category = findViewById(R.id.settings_acount_category);
        pricing = findViewById(R.id.settings_account_pricing_input);
        bio = findViewById(R.id.settings_account_bio_input);
        save = findViewById(R.id.settings_account_save_btn);
        back = findViewById(R.id.settings_account_back);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        EventChangeListener();

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String price = pricing.getText().toString();
                String biography = bio.getText().toString();
                String cat = category.getSelectedItem().toString();

//                        adding data into document of user
                HashMap<String , Object> user = new HashMap<>();
                user.put("pricing" , price);
                user.put("bio", biography);
                user.put("category", cat);

                db.collection("Users").document(auth.getCurrentUser().getUid()).update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {

                        new SweetAlertDialog(SettingsAccountInformationActivity.this, SweetAlertDialog.SUCCESS_TYPE)
                                .setTitleText("Information Updated")
                                .setContentText("Your information was updated successfully")
                                .setConfirmText("Ok")
                                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                    @Override
                                    public void onClick(SweetAlertDialog sDialog) {
                                        finish(); // goes to previous activity
                                    }
                                })
                                .show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(SettingsAccountInformationActivity.this, "Unable to update", Toast.LENGTH_SHORT).show();
                    }
                });

             }
        });
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


                        String price = value.getString("pricing");
                        String biog = value.getString("bio");
                        String cate = value.getString("category");
                        int cateVal = 0;
                        if(cate.equalsIgnoreCase("tiktok")){
                            cateVal = 1;
                        } else if (cate.equalsIgnoreCase("Entertainment")){
                            cateVal = 2;
                        } else {
                            cateVal = 3;
                        }

                        if(!price.equalsIgnoreCase(pricing.getText().toString())){
                            pricing.setText(price);
                        }
                        if(!biog.equalsIgnoreCase(bio.getText().toString())){
                            bio.setText(biog);
                        }
                        if(!cate.equalsIgnoreCase(category.getSelectedItem().toString())){
                            category.setSelection(cateVal);
                        }
                    }

                });
    }
}