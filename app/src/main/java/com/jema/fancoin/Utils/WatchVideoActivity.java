package com.jema.fancoin.Utils;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.storage.StorageReference;
import com.jema.fancoin.R;
import com.squareup.picasso.Picasso;

import org.apache.commons.lang3.StringUtils;

public class WatchVideoActivity extends AppCompatActivity {

    private Button uploadVideo;
    private ImageView thumbnailImage, playPauseBtn, iconUpload, playBtn, pauseBtn, backBtn;
    String videoUrl;
    TextView selector;
    CardView bottomCard;
    StorageReference videoRef;
    PlayerView playerView;
    ExoPlayer simpleExoPlayer;
    ProgressBar progressBar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_watch_video);


        Intent i = getIntent();

//        getting post data and passing from previous activity
        videoUrl = i.getStringExtra("video_url");


        thumbnailImage = findViewById(R.id.statusSliderThumbnailImage);
        progressBar = findViewById(R.id.progress_bar);
        playPauseBtn = findViewById(R.id.playPauseBtn);
        playerView = findViewById(R.id.download_video_selected);
        iconUpload = findViewById(R.id.download_video_icon);
        playBtn = findViewById(R.id.upload_exo_play);
        pauseBtn = findViewById(R.id.upload_exo_pause);
        backBtn = findViewById(R.id.upload_video_back_btn);
        selector = findViewById(R.id.download_video_selector);
        bottomCard = findViewById(R.id.upload_video_bottom_card);

        simpleExoPlayer = new ExoPlayer.Builder(WatchVideoActivity.this).build();
        playerView.setClipToOutline(true);

        playerView.setPlayer(simpleExoPlayer);
        playerView.setVisibility(View.VISIBLE);
        pauseBtn.setVisibility(View.VISIBLE);
        playBtn.setVisibility(View.GONE);

        MediaItem mediaItem = MediaItem.fromUri(videoUrl);
        simpleExoPlayer.setMediaItem(mediaItem);


        if(!videoUrl.isEmpty()){
            iconUpload.setVisibility(View.GONE);
            selector.setVisibility(View.GONE);
            thumbnailImage.setVisibility(View.VISIBLE);
        }

        Glide.with(WatchVideoActivity.this)
                .load(videoUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(thumbnailImage);

//        settings the thumbnailImage
        String substring1 = "vod/";
        String substring2 = "/mp4";

        String videoId = StringUtils.substringBetween(videoUrl, substring1, substring2);
        String Thumbnail = "https://vod.api.video/vod/" + videoId + "/thumbnail.jpg";


        backBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        playPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                playPauseBtn.setVisibility(View.GONE);
                pauseBtn.setVisibility(View.VISIBLE);

                simpleExoPlayer.prepare();
                simpleExoPlayer.play();
                simpleExoPlayer.setPlayWhenReady(true);
                thumbnailImage.setVisibility(View.GONE);

//                simpleExoPlayer.play();
                simpleExoPlayer.addListener(new Player.Listener() {
                    @Override
                    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                        if (playbackState == Player.STATE_BUFFERING) {
                            progressBar.setVisibility(View.VISIBLE);
                        } else if (playbackState == Player.STATE_READY) {
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                });
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
}