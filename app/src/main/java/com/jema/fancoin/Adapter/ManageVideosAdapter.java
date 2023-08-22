package com.jema.fancoin.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.jema.fancoin.R;

import java.util.ArrayList;

public class ManageVideosAdapter extends RecyclerView.Adapter<ManageVideosAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> pathsList;
    private AppCompatActivity activity;

    private boolean isFullScreen = false;
    public static boolean isFullLock = false;


    public ManageVideosAdapter(Context context, ArrayList<String> pathsList, AppCompatActivity activity) {
        this.context = context;
        this.pathsList = pathsList;
        this.activity = activity;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.manage_video_card, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("ClickableViewAccessibility")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
//        holder.manage_thumbnailImage.setImageBitmap(ThumbnailUtils.createVideoThumbnail(pathsList.get(position),MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));

        // get data
        Uri videoUri = Uri.parse(pathsList.get(position));

        /*String substring1 = "vod/";
        String substring2 = "/mp4";

        String videoId = StringUtils.substringBetween(videoUri.toString(), substring1, substring2);
        String Thumbnail = "https://vod.api.video/vod/" + videoId + "/thumbnail.jpg";


        Glide.with(context)
                .load(Thumbnail)
                .into(holder.manage_thumbnailImage);*/


        Glide.with(context)
                .load(videoUri)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(holder.manage_thumbnailImage);

        holder.manage_simpleExoPlayer = new ExoPlayer.Builder(context).build();

        holder.manage_playerView.setPlayer(holder.manage_simpleExoPlayer);

        MediaItem mediaItem = MediaItem.fromUri(videoUri);
        holder.manage_simpleExoPlayer.setMediaItem(mediaItem);

        holder.manage_bt_fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFullScreen) {
                    holder.manage_bt_fullscreen.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_fullscreen_exit)
                    );
//                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                } else {
                    holder.manage_bt_fullscreen.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_fullscreen));
//                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                isFullScreen = !isFullScreen;
            }
        });

        holder.manage_playPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.manage_playPauseBtn.setVisibility(View.GONE);
                holder.manage_bt_pause.setVisibility(View.VISIBLE);

                holder.manage_simpleExoPlayer.prepare();
                holder.manage_simpleExoPlayer.play();
                holder.manage_simpleExoPlayer.setPlayWhenReady(true);
                holder.manage_thumbnailImage.setVisibility(View.GONE);

//                holder.manage_simpleExoPlayer.play();
                holder.manage_simpleExoPlayer.addListener(new Player.Listener() {
                    @Override
                    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                        if (playbackState == Player.STATE_BUFFERING) {
                            holder.manage_progressBar.setVisibility(View.VISIBLE);
                        } else if (playbackState == Player.STATE_READY) {
                            holder.manage_progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        holder.manage_bt_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.manage_simpleExoPlayer.pause();
                holder.manage_bt_pause.setVisibility(View.GONE);
                holder.manage_bt_play.setVisibility(View.VISIBLE);
            }
        });

        holder.manage_bt_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.manage_simpleExoPlayer.play();
                holder.manage_bt_pause.setVisibility(View.VISIBLE);
                holder.manage_bt_play.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return pathsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        PlayerView manage_playerView; 
        ImageView manage_playPauseBtn;
        ImageView manage_bt_fullscreen, manage_bt_pause, manage_bt_play;
        ExoPlayer manage_simpleExoPlayer;
        ProgressBar manage_progressBar;
        ImageView manage_thumbnailImage;


        public ViewHolder(@NonNull View view) {
            super(view);
            manage_playerView = view.findViewById(R.id.statusSliderVideo);
            manage_thumbnailImage = view.findViewById(R.id.statusSliderThumbnailImage);
            manage_playPauseBtn = view.findViewById(R.id.playPauseBtn);
            manage_progressBar = view.findViewById(R.id.progress_bar);
            manage_bt_fullscreen = view.findViewById(R.id.bt_fullscreen);
            manage_bt_pause = view.findViewById(R.id.exo_pause);
            manage_bt_play = view.findViewById(R.id.exo_play); 
        }
    }
}