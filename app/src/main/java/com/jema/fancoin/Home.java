package com.jema.fancoin;

import static com.jema.fancoin.SettingsActivity.SettingsThemeActivity.THEME;
import static org.xmlpull.v1.XmlPullParser.TEXT;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.jema.fancoin.SettingsActivity.SettingsActivity;
import com.jema.fancoin.databinding.ActivityHomeBinding;
import com.squareup.picasso.Picasso;

public class Home extends AppCompatActivity {

    ActivityHomeBinding binding;
    DrawerLayout drawerLayout;
    String statusApplication = "none";
    androidx.appcompat.widget.Toolbar toolbar;
    NavigationView navigationView;
    MenuItem applyItem;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    public static final String SHARED_PREFS = "sharedPrefs";
    public static final String USERNAME = "username";
    public static final String USEREMAIL = "useremail";
    public static final String USERIMAGE = "userimage";
    public static final String USERPHONENUMBER = "userphonenumber";
    public static final String USERAPPLICATIONSTATUS = "userapplication";
    String applicationStat = "default";
    private FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences mySharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String myName = mySharedPreferences.getString(USERNAME, null);
        String myEmail = mySharedPreferences.getString(USEREMAIL, null);
        String myPP = mySharedPreferences.getString(USERIMAGE, null);
        String theme = mySharedPreferences.getString(THEME, null);


//        block used to check the default theme setting of user and start off the app with that
        if(theme != null) {
            if (theme.equalsIgnoreCase("1")) {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_NO);
            } else if (theme.equalsIgnoreCase("2")){
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_YES);
            } else {
                AppCompatDelegate.setDefaultNightMode(AppCompatDelegate.MODE_NIGHT_AUTO_BATTERY);
            }
        }


        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        firebaseAuth = FirebaseAuth.getInstance();

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                if (user != null) {
                    Intent intent = new Intent(Home.this, Login.class);
                    startActivity(intent);
                    finish();
                }
            }
        };

//        bottom navigation inflater
        binding = ActivityHomeBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replaceFragment(new HomeFragment());

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.homeMenu:
                    replaceFragment(new HomeFragment());
                    break;
                case R.id.inboxMenu:
                    replaceFragment(new InboxFragment());
                    break;
                case R.id.followingMenu:
                    replaceFragment(new FollowingFragment());
                    break;
                case R.id.profileMenu:
                    replaceFragment(new ProfileFragment());
                    break;
            }
            return true;
        });



        navigationView = findViewById(R.id.navigation_view);
        drawerLayout = findViewById(R.id.drawer_layout);
        toolbar = findViewById(R.id.toolbar);


        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawerLayout, toolbar, R.string.open, R.string.close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorBlack));

        navigationView.getMenu().getItem(0).setChecked(true);
        TextView draw_name =  (TextView) navigationView.getHeaderView(0).findViewById(R.id.drawer_name);
        TextView draw_email =  (TextView) navigationView.getHeaderView(0).findViewById(R.id.drawer_email);
        ImageView draw_pp =  (ImageView) navigationView.getHeaderView(0).findViewById(R.id.drawer_pp);
        applyItem = (MenuItem) navigationView.getMenu().findItem(R.id.applyPage);



//        setting elements in drawer
        draw_name.setText(myName);
        draw_email.setText(myEmail);

        Picasso.get().load(myPP).into(draw_pp);

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()){
                    case R.id.walletPage:
                        Intent i = new Intent(Home.this, WalletActivity.class);
                        startActivity(i);
                        break;
                    case R.id.applyPage:
                        if(statusApplication.equalsIgnoreCase("pending")){
                            Toast.makeText(Home.this, "Application Submitted", Toast.LENGTH_SHORT).show();
                            finish();
                            break;
                        }
                        Intent j = new Intent(Home.this, ApplicationActivity.class);
                        startActivity(j);
                        break;
                    case R.id.sign_out:
                        FirebaseAuth.getInstance().signOut();
                        Toast.makeText(Home.this, "Logged Out!", Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Home.this, Login.class);
                        startActivity(intent);
                        Home.this.finish();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);

                return true;       //you need to return true here, not false
            }
        });
//        download user data from firestore and save it locally
        saveData();

//        SharedPreferences mySharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
//        String string1 = mySharedPreferences.getString(USERNAME, null);
//        String string2 = mySharedPreferences.getString(USEREMAIL, null);
//
//        Log.d("JemaTag", string1);
//        Log.d("JemaTag", string2);
    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
    }
    public void saveData() {
        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();

//        we will get all the data about the current user and store it locally
        db.collection("Users").document(auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                        if(task.isSuccessful()){
                            String user = task.getResult().getString("name");
                            String image = task.getResult().getString("image");
                            String useremail = task.getResult().getString("email");
                            String phoneNumber = task.getResult().getString("phoneNumber");

                            editor.putString(USERNAME, user);
                            editor.putString(USERIMAGE, image);
                            editor.putString(USEREMAIL, useremail);
                            editor.putString(USERPHONENUMBER, phoneNumber);
                            editor.commit(); // persist the values
                        }
                    }
                });

    }

}