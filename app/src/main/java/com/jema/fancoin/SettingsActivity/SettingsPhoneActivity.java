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
import com.jema.fancoin.R;

import java.util.HashMap;

public class SettingsPhoneActivity extends AppCompatActivity {

    private EditText phoneInput;
    private Button save;
    private ImageView back;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_phone);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        phoneInput = findViewById(R.id.settings_phone_input);
        save = findViewById(R.id.settings_phone_save_btn);
        back = findViewById(R.id.settings_phone_back_btn);

        EventChangeListener();

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String phoner = phoneInput.getText().toString();

//                        adding data into document of user
                HashMap<String , Object> user = new HashMap<>();
                user.put("phoneNumber" , phoner);

                db.collection("Users").document(auth.getCurrentUser().getUid()).update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(SettingsPhoneActivity.this, "Phone Number Updated", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(SettingsPhoneActivity.this, "Unable to update", Toast.LENGTH_SHORT).show();
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

                        if(!value.getString("phoneNumber").equalsIgnoreCase(phoneInput.getText().toString())){
                            phoneInput.setText(value.getString("phoneNumber"));
                        }
                    }
                });
    }
}