package com.jema.fancoin.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jema.fancoin.R;

public class ForgotPassActivity extends AppCompatActivity {

    private TextInputEditText email;
    private TextView subtitle;
    private TextInputLayout emailBox;
    private ImageView topImage, checkMark, backBtn;
    private Button resetBtn;
    private FirebaseAuth auth;
    private FirebaseFirestore db;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_pass);

        email = findViewById(R.id.forgot_pass_mailinput_box);
        emailBox = findViewById(R.id.forgot_pass_mailinput);
        resetBtn = findViewById(R.id.forgot_pass_reset_btn);
        topImage = findViewById(R.id.topImage);
        subtitle = findViewById(R.id.forgot_pass_subtitle);
        checkMark = findViewById(R.id.checkMark);
        backBtn = findViewById(R.id.forgot_pass_back_btn);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        resetBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                resetBtn.setText(R.string.sending_email_text);
                emailBox.setError("");
                emailBox.setErrorEnabled(false);

                String emailr = email.getText().toString();

                if (!emailr.isEmpty() && Patterns.EMAIL_ADDRESS.matcher(emailr).matches()) {

                    auth.sendPasswordResetEmail(emailr).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            emailBox.setVisibility(View.GONE);
                            resetBtn.setVisibility(View.GONE);
                            checkMark.setVisibility(View.VISIBLE);
                            subtitle.setText(R.string.reset_link_message);
                            Toast.makeText(ForgotPassActivity.this, R.string.reset_link_sent, Toast.LENGTH_SHORT).show();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(ForgotPassActivity.this, R.string.user_doesnt_exist_forgotpass, Toast.LENGTH_SHORT).show();
                        }
                    });
                } else {
                    emailBox.setError("Invalid Email");
                    emailBox.setErrorEnabled(true);
                    Toast.makeText(ForgotPassActivity.this, R.string.invalid_email_text, Toast.LENGTH_SHORT).show();
                    return;
                }
            }
        });
    }
}