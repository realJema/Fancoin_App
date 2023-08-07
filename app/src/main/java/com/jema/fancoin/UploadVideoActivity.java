package com.jema.fancoin;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
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
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.io.File;
import java.text.SimpleDateFormat;
import java.util.Date;

import video.api.client.ApiVideoClient;
import video.api.client.api.ApiException;
import video.api.client.api.clients.VideosApi;
import video.api.client.api.models.Video;


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
    ProgressDialog progressBar;
    private int progressBarStatus = 0;
    private Handler progressBarHandler = new Handler();
    private long fileSize = 0;
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
                    uploadVideo(vidUri);
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

//            uploadVideo(vidUri);

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

    private void uploadVideo(Uri videoUri) {
        StorageReference storageRef = FirebaseStorage.getInstance().getReference();

//        the path of the video file is going to contain, the order's uid, date and time to make it unique
        Date now = new Date();
        SimpleDateFormat formatter = new SimpleDateFormat("dd_MM_yyyy_HH-m-s"); // we change the format to give a valid name string
        String strDate= formatter.format(now);
        String filename = DocumentId.concat("_" + strDate + ".mp4"); // the name containing the document id, date formatted and the extension

        videoRef = storageRef.child("/orders/" + filename); // the directory on firebase


//        String str = DateUtils.getRelativeDateTimeString(UploadVideoActivity.this, now, DateUtils.MINUTE_IN_MILLIS, DateUtils.WEEK_IN_MILLIS, 0);

        if (videoUri != null) {
            // creating progress bar dialog
            progressBar = new ProgressDialog(UploadVideoActivity.this);
            progressBar.setCancelable(true);
            progressBar.setMessage("File uploading ...");
            progressBar.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
            progressBar.setProgress(0);
            progressBar.setMax(100);
            progressBar.show();
            //reset progress bar and filesize status
            progressBarStatus = 0;
            fileSize = 0;

            ApiVideoClient client = new ApiVideoClient("9WkC5F6s1UN4lOJywaK0YqaO56oXqYjTqouaYX6OKs9");
            // if you rather like to use the sandbox environment:
            // ApiVideoClient client = new ApiVideoClient("YOUR_SANDBOX_API_KEY", ApiVideoClient.Environment.SANDBOX);

            VideosApi apiInstance = client.videos();

            String videoId = "vEUuaTdRAEjQ4Jfrgz"; // Enter the videoId you want to use to upload your video.
            File file = new File(videoUri.getPath()); // The path to the video you would like to upload. The path must be local. If you want to use a video from an online source, you must use the "/videos" endpoint and add the "source" parameter when you create a new video.

            try {
                Video result = apiInstance.upload(videoId, file);
                Log.d("JemaTag", String.valueOf(result));
            } catch (ApiException e) {
                Log.d("JemaTag", "Exception when calling VideosApi#upload");
                Log.d("JemaTag", "Status code: " + e.getCode());
                Log.d("JemaTag", "Reason: " + e.getMessage());
                Log.d("JemaTag", "Response headers: " + e.getResponseHeaders());
                e.printStackTrace();
                Toast.makeText(UploadVideoActivity.this, "upload success!", Toast.LENGTH_SHORT).show();
                progressBar.dismiss();
            }


        } else {
            progressBar.dismiss();
            Toast.makeText(UploadVideoActivity.this, "upload failed!", Toast.LENGTH_SHORT).show();
        }

    }

// StorageReference videoRef;
}