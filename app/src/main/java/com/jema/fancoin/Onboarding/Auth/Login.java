package com.jema.fancoin.Onboarding.Auth;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.jema.fancoin.Home;
import com.jema.fancoin.R;
import com.jema.fancoin.UnverifiedEmailActivity;

public class Login extends AppCompatActivity {

    private Button signIn;
    private EditText email, password;
    private TextView forgotPass;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        auth = FirebaseAuth.getInstance();

        final TextView redirectSignUp = (TextView) findViewById(R.id.redirectSignUp);

        redirectSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Login.this, Register.class);
                startActivity(myIntent);
            }
        });


        email = findViewById(R.id.mailSignIn);
        password = findViewById(R.id.passwordSignIn);
        signIn = findViewById(R.id.signInBtn);
        forgotPass = findViewById(R.id.redirectForgotPass);

        forgotPass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(Login.this, ForgotPassActivity.class);
                startActivity(i);
            }
        });

        signIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();

                if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(Login.this, R.string.empty_credentials, Toast.LENGTH_LONG).show();
                } else if (txt_password.length() < 6) {
                    Toast.makeText(Login.this, R.string.password_too_short, Toast.LENGTH_SHORT).show();
                } else {
                    singInUser(txt_email, txt_password);
                }
            }
        });
    }

    private void singInUser(String email, String password) {
        signIn.setText(R.string.signing_in_text_loading);
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                Boolean isEmailVerified = authResult.getUser().isEmailVerified();

                if(isEmailVerified){
                    Toast.makeText(Login.this, R.string.sign_in_successful_message, Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(Login.this, Home.class);
                    startActivity(myIntent);
                    finish();

                } else {
                    Toast.makeText(Login.this, R.string.verify_your_email_text, Toast.LENGTH_SHORT).show();
//                    Intent myIntent = new Intent(Login.this, Home.class);
                    Intent myIntent = new Intent(Login.this, UnverifiedEmailActivity.class);
                    startActivity(myIntent);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this, R.string.verify_credentials_signin, Toast.LENGTH_SHORT).show();

                Log.d("JemaTag", e.getMessage());
                signIn.setText(R.string.sign_in);
            }
        });
    }
}