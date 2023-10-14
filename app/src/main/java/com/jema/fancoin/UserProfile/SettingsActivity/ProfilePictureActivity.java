package com.jema.fancoin.UserProfile.SettingsActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jema.fancoin.Database.User;
import com.jema.fancoin.Database.UserViewModel;
import com.jema.fancoin.R;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Locale;

public class ProfilePictureActivity extends AppCompatActivity {
    private ImageView pp;
    ImageView back, editImage;

    ProgressDialog progressDialog;
    private Boolean imageSelected = false;
    Uri imageUri;
    StorageReference storageReference;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private UserViewModel viewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profile_picture);
        pp = findViewById(R.id.profile_image);
        back = findViewById(R.id.back_pp);
        editImage = findViewById(R.id.edit_pp);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        viewModel = new ViewModelProvider(ProfilePictureActivity.this).get(UserViewModel.class);
        viewModel.getUserInfo().observe(ProfilePictureActivity.this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user.username != null) {
                    Picasso.get().load(user.image).into(pp);
                }
            }
        });


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        editImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
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
            uploadImage();
        }
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
                                        Toast.makeText(ProfilePictureActivity.this,"Update Failed",Toast.LENGTH_SHORT).show();
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
                        Toast.makeText(ProfilePictureActivity.this,"Failed to Upload", Toast.LENGTH_SHORT).show();

                    }
                });

    }

}