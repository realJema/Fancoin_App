package com.jema.fancoin;

import static com.jema.fancoin.SettingsActivity.SettingsThemeActivity.THEME;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

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

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.jema.fancoin.databinding.ActivityHomeBinding;
import com.squareup.picasso.Picasso;

import java.util.List;

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
    public static final String UNAME = "username";
    public static final String UFULLNAME = "name";
    public static final String UAPPLICATION_STATUS = "application_status";
    public static final String UBIO = "bio";
    public static final String UCATEGORY = "category";
    public static final String UEMAIL = "email";
    public static final String UFOLLOWERS = "followers";
    public static final String UFOLLOWING = "following";
    public static final String UID = "id";
    public static final String UIMAGE = "image";
    public static final String UPHONE = "phone";
    public static final String UPRICING = "pricing";
    String applicationStat = "default";
    private FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        SharedPreferences mySharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        SharedPreferences.Editor editor = mySharedPreferences.edit();
        String myName = mySharedPreferences.getString(UNAME, null);
        String myEmail = mySharedPreferences.getString(UEMAIL, null);
        String myPP = mySharedPreferences.getString(UIMAGE, null);
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

        toggle.getDrawerArrowDrawable().setColor(getResources().getColor(R.color.colorAccent));

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

//                        clear saved mySharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear().commit();

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
        UserDataListener();

    }

    private void replaceFragment(Fragment fragment){
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frameLayout,fragment);
        fragmentTransaction.commit();
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