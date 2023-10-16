package com.jema.fancoin.UserProfile.SettingsActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
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
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jema.fancoin.Database.User;
import com.jema.fancoin.Database.UserViewModel;
import com.jema.fancoin.R;
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

    BottomSheetDialog dialog;
    TextView profieUsername, profileName, profileEmail;
    ImageView pp, back, editUsername, editName, editEmail, changeImageBtn;
    ShapeableImageView ppCircle;
    String ppPath = null;
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


        dialog = new BottomSheetDialog(
                this,
                R.style.ThemeOverlay_App_BottomSheetDialog
        );


        pp = findViewById(R.id.settings_profile_image);
        profileName = findViewById(R.id.name_text);
        profieUsername = findViewById(R.id.username_text);
        profileEmail = findViewById(R.id.settings_profile_email);
        editUsername = findViewById(R.id.username_icon_edit);
        editName = findViewById(R.id.name_icon_edit);
        editEmail = findViewById(R.id.edit_email);
        changeImageBtn = findViewById(R.id.settings_profile_change_image);
        back = findViewById(R.id.back2);
        ppCircle = findViewById(R.id.settings_profile_image);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        viewModel = new ViewModelProvider(SettingsProfileActivity.this).get(UserViewModel.class);
        viewModel.getUserInfo().observe(SettingsProfileActivity.this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user.username != null) {
                    profieUsername.setText(user.username);
                    profileName.setText(user.full_name);
                    profileEmail.setText(user.email);
                    Picasso.get().load(user.image).into(pp);
                    ppPath = user.image;
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

        editUsername.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("username");
            }
        });
        editName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("full_name");
            }
        });
        editEmail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog("email");
            }
        });
        changeImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });
        ppCircle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(SettingsProfileActivity.this, ProfilePictureActivity.class);
                i.putExtra("profile_image", ppPath);
                startActivity(i);
            }
        });
       /*
        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

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
                }
                else if(imageSelected == false)
                    Toast.makeText(SettingsProfileActivity.this,"No changes",Toast.LENGTH_SHORT).show();
            }
        });*/

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


    private String showDialog(String field) {

        View view = getLayoutInflater().inflate(R.layout.comment_bottomsheet, null);
        Button sendComment = view.findViewById(R.id.comment_send_btn);
        TextInputEditText descr = view.findViewById(R.id.comment_text);
        TextView title = view.findViewById(R.id.choosetxt);
        TextInputLayout input = view.findViewById(R.id.comment);

        sendComment.setText(R.string.save);
        title.setText("Change ".concat(field));
        input.setHint("New ".concat(field));


        view.setBackgroundColor(Color.TRANSPARENT);

        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (descr.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(SettingsProfileActivity.this, "Enter a ".concat(field), Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.dismiss();
//                        adding data into document of user
                HashMap<String , Object> user = new HashMap<>();
                user.put(field, descr.getText().toString());

                db.collection("Users").document(auth.getCurrentUser().getUid()).update(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        Toast.makeText(SettingsProfileActivity.this,field.concat(" Updated"),Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {

                        Toast.makeText(SettingsProfileActivity.this,"Update Failed",Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        dialog.setCancelable(true);
        dialog.setContentView(view);
        descr.requestFocus();
        dialog.show();
        return null;
    }
}