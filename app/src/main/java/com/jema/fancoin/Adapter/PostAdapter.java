package com.jema.fancoin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.recyclerview.widget.RecyclerView;

import com.jema.fancoin.Model.PostCard;
import com.jema.fancoin.PostDetails;
import com.jema.fancoin.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class PostAdapter extends RecyclerView.Adapter<PostAdapter.PostViewHolder> {

    Context context;
    ArrayList<PostCard> postCardArrayList;

    public PostAdapter(Context context, ArrayList<PostCard> postCardArrayList) {
        this.context = context;
        this.postCardArrayList = postCardArrayList;
    }

    @NonNull
    @Override
    public PostViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.post_layout, parent, false);

        return new PostViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull PostViewHolder holder, final int position) {

        holder.name.setText(postCardArrayList.get(position).getName());
        holder.price.setText(postCardArrayList.get(position).getPricing());
//        holder.bio.setText(postCardArrayList.get(position).getBio());
        holder.category.setText(postCardArrayList.get(position).getCategory());
//        holder.bg.setBackgroundResource(R.drawable.cardbg);
//        holder.bg.setBackgroundResource(postCardArrayList.get(position).getImage());
        Picasso.get().load(postCardArrayList.get(position).getImage()).into(holder.image);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, PostDetails.class);
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

        TextView name, bio, price, category;
        ImageView image;

        public PostViewHolder(@NonNull View itemView) {
            super(itemView);

            name = itemView.findViewById(R.id.product_name);
//            bio = itemView.findViewById(R.id.product_description);
            price = itemView.findViewById(R.id.product_price);
            category = itemView.findViewById(R.id.product_category);
            image = itemView.findViewById(R.id.postImage);

        }
    }

}
