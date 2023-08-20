package com.jema.fancoin.UserProfile.SettingsActivity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.jema.fancoin.R;

import java.util.HashMap;

public class SettingsEmailActivity extends AppCompatActivity {

    private EditText emailInput;
    private TextInputLayout emailBox;
    private Button save;
    private ImageView back;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_email);


        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        emailInput = findViewById(R.id.settings_email_input);
        emailBox = findViewById(R.id.settings_email_input_box);
        save = findViewById(R.id.settings_email_save_btn);
        back = findViewById(R.id.settings_email_back_btn);

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
                emailBox.setError("");
                emailBox.setErrorEnabled(false);

                String emailr = emailInput.getText().toString();

                if (!emailr.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailr).matches()) {
//                        adding data into document of user
                    HashMap<String, Object> user = new HashMap<>();
                    user.put("email", emailr);

                    db.collection("Users").document(auth.getCurrentUser().getUid()).update(user).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(SettingsEmailActivity.this, "Email Updated", Toast.LENGTH_SHORT).show();
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(SettingsEmailActivity.this, "Unable to update", Toast.LENGTH_SHORT).show();
                        }
                    });

                } else {
                    emailBox.setError("Invalid Email");
                    emailBox.setErrorEnabled(true);
                    Toast.makeText(SettingsEmailActivity.this, "Invalid Email", Toast.LENGTH_SHORT).show();
                    return;
                }

            }
        });
    }


    private void EventChangeListener() {

        db.collection("Users").document(auth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("Firebase Error", error.getMessage());
                            return;
                        }

                        if (!value.getString("email").equalsIgnoreCase(emailInput.getText().toString())) {
                            emailInput.setText(value.getString("email"));
                        }
                    }
                });
    }

}