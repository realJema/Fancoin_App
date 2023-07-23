package com.jema.fancoin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jema.fancoin.Model.PostCard;
import com.jema.fancoin.PostDetails;
import com.jema.fancoin.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class FollowingAdapter extends RecyclerView.Adapter<FollowingAdapter.PostViewHolder> {

    Context context;
    ArrayList<PostCard> postCardArrayList;

    public FollowingAdapter(Context context, ArrayList<PostCard> postCardArrayList) {
        this.context = context;
        this.postCardArrayList = postCardArrayList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.follower_layout, parent, false);

        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, final int position) {

        holder.name.setText(postCardArrayList.get(position).getName());

        String ubio = postCardArrayList.get(position).getBio();
        if(ubio.equalsIgnoreCase("")){
            ubio = "(no description)";
        }
        holder.descr.setText(ubio);

        Integer followersCount = postCardArrayList.get(position).getFollowers().size();
//        Integer followingCount = postCardArrayList.get(position).getFollowing().size();

//        Log.d("JemaTag", followingCount.toString());

        if(followersCount != null) {
            holder.followersCount.setText("Followers : ".concat(followersCount.toString()));
        }
//        if(followingCount != null){
//            holder.followingCount.setText("Following : ".concat(followingCount.toString()));
//        }

        Picasso.get().load(postCardArrayList.get(position).getImage()).into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, PostDetails.class);
                i.putExtra("id", postCardArrayList.get(position).getId());
                i.putExtra("name", postCardArrayList.get(position).getName());
//                i.putExtra("image", postCardArrayList.get(position).getBigimageurl());
                i.putExtra("price",postCardArrayList.get(position).getPricing());
                i.putExtra("bio",postCardArrayList.get(position).getBio());
                i.putExtra("category",postCardArrayList.get(position).getCategory());
                i.putExtra("image", postCardArrayList.get(position).getImage());

                context.startActivity(i);

            }
        });

    }

    @Override
    public int getItemCount() {
        return postCardArrayList.size();
    }

    public  static class PostViewHolder extends RecyclerView.ViewHolder{

        TextView name, bio, descr, followingCount, followersCount;
        ImageView image;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.following_star_name);
            descr = itemView.findViewById(R.id.following_description);
//            followingCount = itemView.findViewById(R.id.following_count);
            followersCount = itemView.findViewById(R.id.followers_count);
            image = itemView.findViewById(R.id.followeing_profile_image);

        }
    }

}
