package com.jema.fancoin.Order;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.appcompat.app.AppCompatActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;

import com.jema.fancoin.R;

public class FullscreenViewAcivity extends AppCompatActivity {
    private PlayerView playerView;
    private ExoPlayer simpleExoPlayer;
    private ImageView bt_fullscreen, playBtn, pauseBtn, playPauseBtn;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        // remove title
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);
        setContentView(R.layout.activity_fullscreen_view_acivity);

        Intent i = getIntent();
        String videoLink = i.getStringExtra("videoPath");
        playerView = findViewById(R.id.fullscreen_player);
        playBtn = findViewById(R.id.upload_exo_play);
        pauseBtn = findViewById(R.id.upload_exo_pause);
        bt_fullscreen = findViewById(R.id.bt_fullscreen);
        playPauseBtn = findViewById(R.id.playPauseBtn);

        bt_fullscreen.setImageResource(R.drawable.ic_fullscreen_exit);

        simpleExoPlayer = new ExoPlayer.Builder(FullscreenViewAcivity.this).build();
        playerView.setClipToOutline(true);

        playerView.setPlayer(simpleExoPlayer);
        playerView.setVisibility(View.VISIBLE);


        if(!videoLink.equalsIgnoreCase("")) {
            MediaItem mediaItem = MediaItem.fromUri(videoLink);
            simpleExoPlayer.setMediaItem(mediaItem);
            simpleExoPlayer.prepare();
            simpleExoPlayer.setPlayWhenReady(true);
            simpleExoPlayer.play();
            playBtn.setVisibility(View.GONE);
            pauseBtn.setVisibility(View.VISIBLE);
            playPauseBtn.setVisibility(View.GONE);
        } else {
            playerView.setVisibility(View.GONE);
        }

        bt_fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
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