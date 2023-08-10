package com.jema.fancoin;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.media3.common.MediaItem;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.StorageReference;

import java.io.IOException;
import java.net.URISyntaxException;


public class UploadVideoActivity extends AppCompatActivity {

    private static final int PICK_FROM_FILE = 1;
    private Button uploadVideo;
    private ImageView iconUpload, playBtn, pauseBtn, backBtn;
    String DocumentId;
    TextView selector;
    CardView bottomCard;
    StorageReference videoRef;
    Uri vidUri = null;
    PlayerView playerView;
    ExoPlayer simpleExoPlayer;
    private FirebaseAuth auth;
    private FirebaseFirestore db;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_video);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();


        Intent i = getIntent();
        DocumentId = i.getStringExtra("DocumentId");

        uploadVideo = findViewById(R.id.reply_upload_btn);
        playerView = findViewById(R.id.upload_video_selected);
        iconUpload = findViewById(R.id.upload_video_icon);
        playBtn = findViewById(R.id.upload_exo_play);
        pauseBtn = findViewById(R.id.upload_exo_pause);
        backBtn = findViewById(R.id.upload_video_back_btn);
        selector = findViewById(R.id.upload_video_selector);
        bottomCard = findViewById(R.id.upload_video_bottom_card);

        UploadManager uploader = new UploadManager(this);

        simpleExoPlayer = new ExoPlayer.Builder(UploadVideoActivity.this).build();

        playerView.setClipToOutline(true);

        selector.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iconUpload.setVisibility(View.GONE);
                Intent i = new Intent(Intent.ACTION_PICK, android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
                i.setType("video/*");
                startActivityForResult(i, PICK_FROM_FILE);
            }
        });

        uploadVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!vidUri.toString().equalsIgnoreCase("")){
                    try {
                        uploader.uploadVideo(vidUri, "order", DocumentId);
                    } catch (URISyntaxException e) {
                        throw new RuntimeException(e);
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
//                    finish();
                }
                else {
                    Toast.makeText(UploadVideoActivity.this, "Choose Valid Video", Toast.LENGTH_SHORT).show();

                }
            }
        });


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        pauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simpleExoPlayer.pause();
                pauseBtn.setVisibility(View.GONE);
                playBtn.setVisibility(View.VISIBLE);
            }
        });

        playBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                simpleExoPlayer.play();
                pauseBtn.setVisibility(View.VISIBLE);
                playBtn.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode != RESULT_OK)
            return;

        if (requestCode == PICK_FROM_FILE) {
            vidUri = data.getData();

            bottomCard.setVisibility(View.VISIBLE);
            selector.setVisibility(View.GONE);
        }


        playerView.setPlayer(simpleExoPlayer);
        playerView.setVisibility(View.VISIBLE);
        pauseBtn.setVisibility(View.VISIBLE);
        playBtn.setVisibility(View.GONE);

        MediaItem mediaItem = MediaItem.fromUri(vidUri);
        simpleExoPlayer.setMediaItem(mediaItem);
        simpleExoPlayer.prepare();
        simpleExoPlayer.seekTo(1);
        simpleExoPlayer.play();

    }

}