package com.jema.fancoin;

import android.Manifest;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
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
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.jema.fancoin.Database.AppDatabase;
import com.jema.fancoin.Database.Post;
import com.jema.fancoin.Database.PostViewModel;
import com.jema.fancoin.Database.User;
import com.jema.fancoin.Database.UserViewModel;
import com.jema.fancoin.HomeTabs.FollowingFragment;
import com.jema.fancoin.HomeTabs.HomeFragment;
import com.jema.fancoin.HomeTabs.InboxFragment;
import com.jema.fancoin.HomeTabs.ProfileFragment;
import com.jema.fancoin.Model.PostCard;
import com.jema.fancoin.Onboarding.Auth.Login;
import com.jema.fancoin.UserProfile.UserApplicationActivity;
import com.jema.fancoin.Utils.LanguageManager;
import com.jema.fancoin.Utils.ThemeManager;
import com.jema.fancoin.databinding.ActivityHomeBinding;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
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
    public static final String LANG = "en";
    public static final String PLAYEDONBOARDING = "false";
    public static final String THEME = "1"; // 1 is light mode, 2 is dark mode, 3 is system
    String applicationStat = "default";
    private FirebaseAuth firebaseAuth;
    FirebaseAuth.AuthStateListener mAuthListener;
    AppDatabase localDb;

    public ArrayList<String> videoPaths;
    private LiveData<ArrayList> data = new MutableLiveData<>();
    private UserViewModel viewModel;
    private PostViewModel postModel;
    private Boolean applied = false;
    private int STORAGE_PERMISSION_CODE = 1;
    public User mydata;
    public String myUsername = "username";
    public String numberOfRequests;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        checkPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE, STORAGE_PERMISSION_CODE);

        localDb = Room.databaseBuilder(getApplicationContext(),
                AppDatabase.class, "Fancoin_DB").build();

//        changing the theme and language to match the user's configs
        LanguageManager lang = new LanguageManager(this);
        ThemeManager theme = new ThemeManager(this);

        lang.updateResource(lang.getLang());
        theme.updateTheme(theme.getTheme());

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        postModel = new ViewModelProvider(this).get(PostViewModel.class);

        videoPaths = new ArrayList<String>();
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
        Bundle arguments5 = new Bundle();
        replaceFragment(new HomeFragment(), arguments5);

        binding.bottomNavigationView.setOnItemSelectedListener(item -> {

            switch (item.getItemId()) {
                case R.id.homeMenu:
                    Bundle arguments = new Bundle();
//                    arguments.putString( string_key , desired_string);
                    replaceFragment(new HomeFragment(), arguments);
                    break;
                case R.id.inboxMenu:
                    Bundle arguments2 = new Bundle();
//                    arguments2.putString( string_key , desired_string);
                    replaceFragment(new InboxFragment(), arguments2);
                    break;
                case R.id.followingMenu:
                    Bundle arguments3 = new Bundle();
//                    arguments3.putString( string_key , desired_string);
                    replaceFragment(new FollowingFragment(), arguments3);
                    break;
                case R.id.profileMenu:
                    Bundle arguments4 = new Bundle();
                    arguments4.putString("username", myUsername);
                    replaceFragment(new ProfileFragment(), arguments4);
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
        TextView draw_name = (TextView) navigationView.getHeaderView(0).findViewById(R.id.drawer_name);
        TextView draw_email = (TextView) navigationView.getHeaderView(0).findViewById(R.id.drawer_email);
        ImageView draw_pp = (ImageView) navigationView.getHeaderView(0).findViewById(R.id.drawer_pp);
        applyItem = (MenuItem) navigationView.getMenu().findItem(R.id.applyPage);

        viewModel.getUserInfo().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {

                if (user != null) {
//        setting elements in drawer
                    draw_name.setText(user.full_name);
                    draw_email.setText(user.email);
                    Picasso.get().load(user.image).into(draw_pp);
                    myUsername = user.username;

                    if (user.application_status.equalsIgnoreCase("confirmed")) {
                        applyItem.setTitle("Application Confirmed");
                        applied = true;
                    } else if (user.application_status.equalsIgnoreCase("pending")) {
                        applyItem.setTitle("Application Pending");
                        applied = true;
                    } else if (user.application_status.equalsIgnoreCase("admin")) {
                        applyItem.setTitle("You're an admin");
                        applied = true;
                    }else {
                        applied = false;
                    }
                    mydata = user;
                } else {
                    draw_name.setText("empty");
                    draw_email.setText("empty");
//                    Picasso.get().load(user.image).into(draw_pp); // put local image
                }

            }
        });


        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                switch (item.getItemId()) {
                    /*case R.id.walletPage:
                        Intent i = new Intent(Home.this, WalletActivity.class);
                        startActivity(i);
                        break;*/
                    case R.id.applyPage:
                        if(applied){
                            Toast.makeText(Home.this, R.string.application_submitted, Toast.LENGTH_SHORT).show();
                            break;
                        }
                        Intent j = new Intent(Home.this, UserApplicationActivity.class);
                        startActivity(j);
                        break;
                    case R.id.sign_out:
                        FirebaseAuth.getInstance().signOut();

//                        clear saved mySharedPreferences
                        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();
                        editor.clear().commit();

                        Toast.makeText(Home.this, R.string.logged_out_message, Toast.LENGTH_SHORT).show();

                        Intent intent = new Intent(Home.this, Login.class);
                        startActivity(intent);
                        Home.this.finish();
                        break;
                }
                drawerLayout.closeDrawer(GravityCompat.START);

                return true;   //you need to return true here, not false
            }
        });
//        download user data from firestore and save it locally
        UserDataListener();
        EventChangeListener();
        RequestsListener();
    }

    private void replaceFragment(Fragment fragment, Bundle bundle) {

            fragment.setArguments(bundle);
            FragmentManager fragmentManager = getSupportFragmentManager();
            FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
            fragmentTransaction.replace(R.id.frameLayout, fragment);
            fragmentTransaction.commit();
    }

    public void UserDataListener() {
//        we will get all the data about the current user and store it locally
        db.collection("Users").document(auth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                if (error != null) {
                    Log.e("Firebase Error", error.getMessage());
                    return;
                }
                Log.d("firebase value check", String.valueOf(value));

                String temp_username = value.getString("username");
                String temp_full_name = value.getString("name");
                String temp_application_status = value.getString("application_status");
                String temp_bio = value.getString("bio");
                String temp_category = value.getString("category");
                String temp_email = value.getString("email");
                String temp_phone = value.getString("phoneNumber");
                String temp_pricing = value.getString("pricing");
                String temp_uid = value.getString("id");
                String temp_image = value.getString("image");
                List<String> myFollowers =  (List<String>) value.get("followers");
                List<String> myFollowing = (List<String>) value.get("following");

                String myfollo = "0";
                String myfolli = "0";
                if(myFollowers != null){
                    myfollo = String.valueOf(myFollowers.size());
                }
                if(myFollowing != null) {
                    myfolli = String.valueOf(myFollowing.size());
                }

//                AsyncTask.execute(() -> Log.d("JemaTag", String.valueOf(viewModel.check4User(temp_uid))));

                Boolean result = viewModel.check4User(); // check if user already exists in db
                Log.d("check user tag", String.valueOf(result));
                if (result) {
                    viewModel.updateUser(temp_username, temp_full_name, temp_application_status, temp_category, temp_email, temp_bio, temp_image, temp_phone, temp_pricing, myfollo, myfolli);
                } else {
                    User theUser = new User();
                    theUser.username = temp_username;
                    theUser.full_name = temp_full_name;
                    theUser.application_status = temp_application_status;
                    theUser.category = temp_category;
                    theUser.email = temp_email;
                    theUser.bio = temp_bio;
                    theUser.image = temp_image;
                    theUser.phone = temp_phone;
                    theUser.pricing = temp_pricing;
                    theUser.uid = temp_uid;
                    if(myFollowers != null) {
                        theUser.followers = String.valueOf(myFollowers.size());
                    }
                    if(myFollowing != null) {
                        theUser.following = String.valueOf(myFollowing.size());
                    }
                    AsyncTask.execute(() -> localDb.allDao().insertUser(theUser));
                }
            }
        });
    }

    private void EventChangeListener() {

        db.collection("Users").whereEqualTo("application_status", "confirmed")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
//                            if(progressDialog.isShowing())
//                                progressDialog.dismiss();
                            Log.i("JemaTag", "error gettting data");
                            return;
                        }
                        ArrayList<Post> allPosts = new ArrayList<Post>();
                        PostCard data = new PostCard();
                        AsyncTask.execute(() -> localDb.allDao().deletePost());

                        for (DocumentChange dc : value.getDocumentChanges()) {

                            String id = dc.getDocument().getId();
                            data = dc.getDocument().toObject(PostCard.class);

                            Post post = new Post();
                            post.username = data.getUsername();
                            post.full_name = data.getName();
                            post.application_status = data.getApplication_status();
                            post.category = data.getCategory();
                            post.email = data.getEmail();
                            post.bio = data.getBio();
                            post.image = data.getImage();
                            post.phone = data.getPhone();
                            post.pricing = data.getPricing();
                            post.uid = data.getId();
                            Boolean postExist = postModel.check4Post(data.getId()); // check if user already exists in db

                            Integer position = 0;
                            PostCard finalData = data;

                            allPosts.add(post);

//                            switch (dc.getType()) {
//                                case ADDED:
//                                    if(!postExist){
//                                    }
//                                    break;
//                                case MODIFIED:
////                                    allPosts.set(position, post);
////                                    Log.d("JemaTag", position.toString());
////                                    Log.d("JemaTag", allPosts.get(position).username);
//                                    break;
//                                case REMOVED:
//                                    allPosts.remove(Integer.parseInt(id));
//                                    break;
//                            }
                        }
                        postModel.insertPosts(allPosts);
                    }
                });
    }

    // Function to check and request permission.
    public void checkPermission(String permission, int requestCode)
    {
        if (ContextCompat.checkSelfPermission(Home.this, permission) == PackageManager.PERMISSION_DENIED) {

            Log.d("JemaTag", "Permission unavailable");

            // Requesting the permission
            requestStoragePermission();
        }
        else {
            Log.d("JemaTag", "Permission already granted");
        }
    }


    private void requestStoragePermission() {
        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Home.this,
                                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }

        if (ActivityCompat.shouldShowRequestPermissionRationale(this,
                Manifest.permission.WRITE_EXTERNAL_STORAGE)) {

            new AlertDialog.Builder(this)
                    .setTitle("Permission needed")
                    .setMessage("This permission is needed because of this and that")
                    .setPositiveButton("ok", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            ActivityCompat.requestPermissions(Home.this,
                                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
                        }
                    })
                    .setNegativeButton("cancel", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                        }
                    })
                    .create().show();

        } else {
            ActivityCompat.requestPermissions(this,
                    new String[] {Manifest.permission.WRITE_EXTERNAL_STORAGE}, STORAGE_PERMISSION_CODE);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == STORAGE_PERMISSION_CODE) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Toast.makeText(this, "Permission GRANTED", Toast.LENGTH_SHORT).show();
            } else {
                Toast.makeText(this, "Permission DENIED", Toast.LENGTH_SHORT).show();
            }
        }
    }


    /**
     * Listening on the data base of orders to count all the commands passed for this user
     */
    private void RequestsListener() {

        db.collection("Orders").whereEqualTo("star_uid", auth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
//                            if(progressDialog.isShowing())
//                                progressDialog.dismiss();
                            Log.i("JemaTag", "error gettting data");
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {

                            String numberOrders = String.valueOf(value.getDocuments().size());
                            numberOfRequests = numberOrders;
                         }
                    }
                });
    }
}