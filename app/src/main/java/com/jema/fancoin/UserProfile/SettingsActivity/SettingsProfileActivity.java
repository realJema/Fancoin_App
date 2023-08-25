package com.jema.fancoin.UserProfile.SettingsActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jema.fancoin.R;
import com.jema.fancoin.Database.User;
import com.jema.fancoin.Database.UserViewModel;
import com.jema.fancoin.databinding.ActivityMainBinding;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class SettingsProfileActivity extends AppCompatActivity {

    ActivityMainBinding binding;
    Uri imageUri;
    StorageReference storageReference;
    ProgressDialog progressDialog;

    TextView changeImageBtn;
    ImageView pp, back;
    TextInputEditText username;
    TextInputLayout usernameContainer;
    Button saveBtn;
    private String currentName, currentPP;
    private Boolean imageSelected = false;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private UserViewModel viewModel;
    String myUsername;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_profile);

        pp = findViewById(R.id.settings_profile_image);
        changeImageBtn = findViewById(R.id.settings_profile_change_image_textview);
        username = findViewById(R.id.settings_profile_name_input);
        usernameContainer = findViewById(R.id.settings_profile_username_container);
        saveBtn = findViewById(R.id.settings_profile_save_btn);
        back = findViewById(R.id.back2);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        viewModel = new ViewModelProvider(SettingsProfileActivity.this).get(UserViewModel.class);
        viewModel.getUserInfo().observe(SettingsProfileActivity.this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user.username != null) {
                    username.setText(user.username);
                    Picasso.get().load(user.image).into(pp);
                    myUsername = user.username;
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        changeImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Log.d("JemaTag", imageSelected.toString());

                usernameContainer.setError("");
                usernameContainer.setErrorEnabled(false);

                if(username.getText().toString().contains(" ")){
                    usernameContainer.setError("Can't have spaces");
                    usernameContainer.setErrorEnabled(true);
                    return;
                }

                if(imageSelected == true){
                    Log.d("JemaTag", "uploading image");
                    uploadImage();
                }
                if(!myUsername.equalsIgnoreCase(username.getText().toString())){
                    Log.d("JemaTag", "Saving name");
                    saveName();
                }
                else if(imageSelected == false)
                    Toast.makeText(SettingsProfileActivity.this,"No changes",Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void uploadImage() {
        progressDialog = new ProgressDialog(this);
        progressDialog.setCancelable(false);
        progressDialog.setMessage("Image uploading ...");
        progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        progressDialog.show();

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy_MM_dd_HH_mm_ss", Locale.CANADA);
        Date now = new Date();
        String fileName = formatter.format(now);
        storageReference = FirebaseStorage.getInstance().getReference("pp/"+fileName);


        storageReference.putFile(imageUri)
                .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                    @Override
                    public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                        pp.setImageURI(null);
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String downloadUrl = uri.toString();

//                        adding data into document of user
                                HashMap<String , Object> user = new HashMap<>();
                                user.put("image" , downloadUrl);

                                db.collection("Users").document(auth.getCurrentUser().getUid()).update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
//                                        add image url to room db
                                        viewModel.updateImage(downloadUrl);
                                    }
                                }).addOnFailureListener(new OnFailureListener() {
                                    @Override
                                    public void onFailure(@NonNull Exception e) {
                                        Toast.makeText(SettingsProfileActivity.this,"Update Failed",Toast.LENGTH_SHORT).show();
                                    }
                                });
                            }
                        });

                        imageSelected = false;
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();

                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        if (progressDialog.isShowing())
                            progressDialog.dismiss();
                        Toast.makeText(SettingsProfileActivity.this,"Failed to Upload", Toast.LENGTH_SHORT).show();

                    }
                });

    }


    private void saveName() {

//                        adding data into document of user
        HashMap<String , Object> user = new HashMap<>();
        user.put("username", username.getText().toString());

        db.collection("Users").document(auth.getCurrentUser().getUid()).update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        finish(); // goes to previous activity
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(SettingsProfileActivity.this,"Update Failed",Toast.LENGTH_SHORT).show();
                    }
                });

    }


    private void selectImage() {

        pp.setImageURI(null);
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,100);

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && data != null && data.getData() != null){
            imageUri = data.getData();
            pp.setImageURI(imageUri);
            imageSelected = true;
        }
    }
}