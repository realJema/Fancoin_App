package com.jema.fancoin.SettingsActivity;

import static android.content.ContentValues.TAG;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.jema.fancoin.Home;
import com.jema.fancoin.PostDetails;
import com.jema.fancoin.R;
import com.jema.fancoin.Register;
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
    EditText username;
    Button saveBtn;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_profile);

        pp = findViewById(R.id.settings_profile_image);
        changeImageBtn = findViewById(R.id.settings_profile_change_image_textview);
        username = findViewById(R.id.settings_profile_name_input);
        saveBtn = findViewById(R.id.settings_profile_save_btn);
        back = findViewById(R.id.back2);

        auth = FirebaseAuth.getInstance();


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent i = new Intent(SettingsProfileActivity.this, SettingsActivity.class);
                startActivity(i);
                finish();
            }
        });


        putPP();
        changeImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                selectImage();
            }
        });

        saveBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                uploadImage();
            }
        });

    }

    private void uploadImage() {

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Uploading File....");
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
                        FirebaseFirestore db = FirebaseFirestore.getInstance();
                        storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                String downloadUrl = uri.toString();



//                        adding data into document of user
                                HashMap<String , Object> user = new HashMap<>();
                                user.put("image" , downloadUrl);
                                user.put("username", username.getText().toString());

                                db.collection("Users").whereEqualTo("id", auth.getCurrentUser().getUid()).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                                    @Override
                                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                                        if(task.isSuccessful() && !task.getResult().isEmpty()){
                                            DocumentSnapshot documentSnapshot = task.getResult().getDocuments().get(0);
                                            String documentID = documentSnapshot.getId();
                                            db.collection("Users")
                                                    .document(documentID)
                                                    .update(user)
                                                    .addOnSuccessListener(new OnSuccessListener<Void>() {
                                                        @Override
                                                        public void onSuccess(Void unused) {
                                                            Toast.makeText(SettingsProfileActivity.this,"Successfully Updated",Toast.LENGTH_SHORT).show();
                                                        }
                                                    }).addOnFailureListener(new OnFailureListener() {
                                                        @Override
                                                        public void onFailure(@NonNull Exception e) {
                                                            Toast.makeText(SettingsProfileActivity.this,"Update Failed",Toast.LENGTH_SHORT).show();
                                                        }
                                                    });
                                        } else {
                                            Toast.makeText(SettingsProfileActivity.this,"Failed",Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }
                        });

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

    private void putPP() {
        FirebaseFirestore db = FirebaseFirestore.getInstance();
        String currentId = auth.getCurrentUser().getUid();
        db.collection("Users").whereEqualTo("id", currentId).get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {

                                String user = document.getString("username");
                                String image = document.getString("image");


                                Picasso.get().load(image).into(pp);
                                username.setText(user);
                            }
                        } else {
                            Log.d(TAG, "Error getting documents: ", task.getException());
                        }
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
        }
    }

    @Override
    protected void onStart() {
        super.onStart();
    }
}