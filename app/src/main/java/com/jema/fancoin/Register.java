package com.jema.fancoin;

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

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class Register extends AppCompatActivity {


    private Button singupBtn;
    private EditText email, password, name, phoneNumber;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        final TextView redirectSignIn = (TextView) findViewById(R.id.redirectSignIn);

        redirectSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(Register.this, Login.class);
                startActivity(myIntent);
            }
        });


        auth = FirebaseAuth.getInstance();

        email = findViewById(R.id.mailSignUp);
        password = findViewById(R.id.passwordSignUp);
        name = findViewById(R.id.nameSignUp);
        phoneNumber = findViewById(R.id.numberSignUp);
        singupBtn = findViewById(R.id.signUpBtn);

        singupBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String txt_email = email.getText().toString();
                String txt_password = password.getText().toString();
                String txt_name = name.getText().toString();
                String txt_phoneNumber = phoneNumber.getText().toString();


                if(TextUtils.isEmpty(txt_email) || TextUtils.isEmpty(txt_password)){
                    Toast.makeText(Register.this, "Empty Credentials!", Toast.LENGTH_SHORT).show();
                } else if (txt_password.length() < 6) {
                    Toast.makeText(Register.this, "Password too short", Toast.LENGTH_SHORT).show();
                } else {
                    registerUser(txt_email, txt_password, txt_name, txt_phoneNumber); 
                }
            }
        });
    }

    private void registerUser(String email, String password, String name, String phoneNumber) {
        auth.createUserWithEmailAndPassword(email, password).addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(Register.this, "Registering user successful", Toast.LENGTH_SHORT).show();
                    Intent myIntent = new Intent(Register.this, Home.class);
                    startActivity(myIntent);
                    finish();
                } else {
                    Toast.makeText(Register.this, "Registering user failed", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }
}