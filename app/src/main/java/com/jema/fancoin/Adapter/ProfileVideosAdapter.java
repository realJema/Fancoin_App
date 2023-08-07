package com.jema.fancoin.Adapter;

import android.annotation.SuppressLint;
import android.content.Context;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.FragmentActivity;
import androidx.media3.common.MediaItem;
import androidx.media3.common.Player;
import androidx.media3.exoplayer.ExoPlayer;
import androidx.media3.ui.PlayerView;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.jema.fancoin.R;

import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;

public class ProfileVideosAdapter extends RecyclerView.Adapter<ProfileVideosAdapter.ViewHolder> {
    private Context context;
    private ArrayList<String> pathsList;
    private FragmentActivity activity;

    private boolean isFullScreen = false;
    public static boolean isFullLock = false;


    public ProfileVideosAdapter(Context context, ArrayList<String> pathsList, FragmentActivity activity) {
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
//        holder.profile_thumbnailImage.setImageBitmap(ThumbnailUtils.createVideoThumbnail(pathsList.get(position),MediaStore.Video.Thumbnails.FULL_SCREEN_KIND));

        // get data
        Uri videoUri = Uri.parse(pathsList.get(position));

        String substring1 = "vod/";
        String substring2 = "/mp4";

        String videoId = StringUtils.substringBetween(videoUri.toString(), substring1, substring2);
        String Thumbnail = "https://vod.api.video/vod/" + videoId + "/thumbnail.jpg";


        Glide.with(context)
                .load(Thumbnail)
                .into(holder.profile_thumbnailImage);

        holder.profile_simpleExoPlayer = new ExoPlayer.Builder(context).build();

        holder.profile_playerView.setPlayer(holder.profile_simpleExoPlayer);

        MediaItem mediaItem = MediaItem.fromUri(videoUri);
        holder.profile_simpleExoPlayer.setMediaItem(mediaItem);

        holder.profile_bt_fullscreen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!isFullScreen) {
                    holder.profile_bt_fullscreen.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_fullscreen_exit)
                    );
//                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_SENSOR_LANDSCAPE);
                } else {
                    holder.profile_bt_fullscreen.setImageDrawable(ContextCompat.getDrawable(context, R.drawable.ic_fullscreen));
//                    activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
                }
                isFullScreen = !isFullScreen;
            }
        });

        holder.profile_playPauseBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.profile_playPauseBtn.setVisibility(View.GONE);
                holder.profile_bt_pause.setVisibility(View.VISIBLE);


                holder.profile_simpleExoPlayer.prepare();
                holder.profile_simpleExoPlayer.play();
                holder.profile_simpleExoPlayer.setPlayWhenReady(true);
                holder.profile_thumbnailImage.setVisibility(View.GONE);

//                holder.profile_simpleExoPlayer.play();
                holder.profile_simpleExoPlayer.addListener(new Player.Listener() {
                    @Override
                    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {
                        if (playbackState == Player.STATE_BUFFERING) {
                            holder.profile_progressBar.setVisibility(View.VISIBLE);
                        } else if (playbackState == Player.STATE_READY) {
                            holder.profile_progressBar.setVisibility(View.GONE);
                        }
                    }
                });
            }
        });

        holder.profile_bt_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.profile_simpleExoPlayer.pause();
                holder.profile_bt_pause.setVisibility(View.GONE);
                holder.profile_bt_play.setVisibility(View.VISIBLE);
            }
        });

        holder.profile_bt_play.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.profile_simpleExoPlayer.play();
                holder.profile_bt_pause.setVisibility(View.VISIBLE);
                holder.profile_bt_play.setVisibility(View.GONE);
            }
        });

    }

    @Override
    public int getItemCount() {
        return pathsList.size();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        PlayerView profile_playerView; 
        ImageView profile_playPauseBtn;
        ImageView profile_bt_fullscreen, profile_bt_pause, profile_bt_play;
        ExoPlayer profile_simpleExoPlayer;
        ProgressBar profile_progressBar;
        ImageView profile_thumbnailImage;

        public ViewHolder(@NonNull View view) {
            super(view);
           profile_playerView = view.findViewById(R.id.statusSliderVideo);
            profile_thumbnailImage = view.findViewById(R.id.statusSliderThumbnailImage);
           profile_playPauseBtn = view.findViewById(R.id.playPauseBtn);
           profile_progressBar = view.findViewById(R.id.progress_bar);
           profile_bt_fullscreen = view.findViewById(R.id.bt_fullscreen);
           profile_bt_pause = view.findViewById(R.id.exo_pause);
           profile_bt_play = view.findViewById(R.id.exo_play);
        }
    }
}