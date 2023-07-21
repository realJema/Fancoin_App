package com.jema.fancoin.SettingsActivity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.jema.fancoin.Adapter.CommentAdapter;
import com.jema.fancoin.Adapter.VideoSliderAdapter;
import com.jema.fancoin.AddShowcaseActivity;
import com.jema.fancoin.AdminActivity;
import com.jema.fancoin.Home;
import com.jema.fancoin.Login;
import com.jema.fancoin.Model.CommentModel;
import com.jema.fancoin.PostDetails;
import com.jema.fancoin.R;
import com.jema.fancoin.Register;
import com.jema.fancoin.SuccessOrderActivity;

import java.util.ArrayList;
import java.util.List;

public class SettingsManageVideosActivity extends AppCompatActivity {

    public List<CommentModel> commentsList;
    Button orderVideo;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    ImageView backBtn;
    Button uploadPage;
    private RecyclerView showcaseFeed;
    private VideoSliderAdapter myAdapter;
    ArrayList<String> videoPaths;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings_manage_videos);


        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        showcaseFeed = findViewById(R.id.showcaseFeed);
        uploadPage = findViewById(R.id.settings_manage_videos_add);
        backBtn = findViewById(R.id.settings_manage_videos_back_btn);

        showcaseFeed.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(SettingsManageVideosActivity.this, 2);
        showcaseFeed.setLayoutManager(mLayoutManager);
        showcaseFeed.setPadding(10, 10, 10, 10);


        videoPaths = new ArrayList<>();

        // add paths for video simllarly

        myAdapter = new VideoSliderAdapter(getApplicationContext(), videoPaths,
                SettingsManageVideosActivity.this);
        showcaseFeed.setAdapter(myAdapter);

        uploadPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent myIntent = new Intent(SettingsManageVideosActivity.this, AddShowcaseActivity.class);
                startActivity(myIntent);
            }
        });

        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        getShowcases();
    }

    public void getShowcases() {
        db.collection("Users").document(auth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
            @Override
            public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                List<String> group = (List<String>) value.get("showcase");


                if(group != null) {
                    for(int i = 0; i < group.size(); i++){
                        videoPaths.add(group.get(i));
                    }
                }
            }
        });
    }

}