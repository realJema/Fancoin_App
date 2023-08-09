package com.jema.fancoin.Auth;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jema.fancoin.Home;
import com.jema.fancoin.R;

public class Login extends AppCompatActivity {

    private Button signIn;
    private EditText email, password;
    private TextView forgoetPass;
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
        forgoetPass = findViewById(R.id.redirectForgotPass);

        forgoetPass.setOnClickListener(new View.OnClickListener() {
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
                    Toast.makeText(Login.this, "Empty Credentials!", Toast.LENGTH_LONG).show();
                } else if (txt_password.length() < 6) {
                    Toast.makeText(Login.this, "Password too short", Toast.LENGTH_SHORT).show();
                } else {
                    singInUser(txt_email, txt_password);
                }
            }
        });
    }

    private void singInUser(String email, String password) {
        signIn.setText("Signing In...");
        auth.signInWithEmailAndPassword(email, password).addOnSuccessListener(new OnSuccessListener<AuthResult>() {
            @Override
            public void onSuccess(AuthResult authResult) {

                Boolean isEmailVerified = authResult.getUser().isEmailVerified();

                if(isEmailVerified){
                    Toast.makeText(Login.this, "Sign In Successful!", Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(Login.this, Home.class);
                    startActivity(myIntent);
                    finish();

                } else {
                    Toast.makeText(Login.this, "Verify your email", Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(Login.this, Home.class);
//                    Intent myIntent = new Intent(Login.this, UnverifiedEmailActivity.class);
                    startActivity(myIntent);
                    finish();
                }
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(Login.this, "Sign In Failed!", Toast.LENGTH_SHORT).show();
                signIn.setText("Sign In");
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

//        if (user != null && user.isEmailVerified()){
            if (user != null){
            startActivity(new Intent(Login.this, Home.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }
    }
}