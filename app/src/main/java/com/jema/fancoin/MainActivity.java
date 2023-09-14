package com.jema.fancoin;

import static com.jema.fancoin.Home.PLAYEDONBOARDING;
import static com.jema.fancoin.Home.SHARED_PREFS;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.jema.fancoin.Database.AppDatabase;
import com.jema.fancoin.Onboarding.Auth.Login;
import com.jema.fancoin.Onboarding.OnboardingActivity;

public class MainActivity extends AppCompatActivity {

    private FirebaseAuth auth;
    FirebaseAuth.AuthStateListener mAuthListener;
    private ImageView iconImage;
    private LinearLayout linearLayout;
    AppDatabase db;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        iconImage = findViewById(R.id.icon_image);

        TranslateAnimation animation = new TranslateAnimation(0 , 0 , 0 , -700);
        animation.setDuration(3000);
        animation.setFillAfter(false);
        animation.setAnimationListener(new MyAnimationListener());

        iconImage.setAnimation(animation);

    }

    private class MyAnimationListener implements Animation.AnimationListener {

        @Override
        public void onAnimationStart(Animation animation) {

        }

        @Override
        public void onAnimationEnd(Animation animation) {
            iconImage.clearAnimation();
            iconImage.setVisibility(View.INVISIBLE);

            SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
            String onboard = sharedPreferences.getString(PLAYEDONBOARDING, "false");
//        play onboarding
            if (onboard == "false"){
                startActivity(new Intent(MainActivity.this, OnboardingActivity.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
            } else {
                startActivity(new Intent(MainActivity.this, Login.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
                finish();
            }
        }

        @Override
        public void onAnimationRepeat(Animation animation) {

        }
    }

    @Override
    protected void onStart() {
        super.onStart();

        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();

        if (user != null && user.isEmailVerified()){
//            Log.d("JemaTag", "user is logged in ");
            startActivity(new Intent(MainActivity.this, Home.class).addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK));
        }

    }
}