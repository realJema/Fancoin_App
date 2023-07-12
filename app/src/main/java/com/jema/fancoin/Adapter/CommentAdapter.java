package com.jema.fancoin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jema.fancoin.Model.CommentModel;
import com.jema.fancoin.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class CommentAdapter extends RecyclerView.Adapter<CommentAdapter.CommentViewHolder> {

    Context context;
    ArrayList<CommentModel> commentModelArray;

    public CommentAdapter(Context context, ArrayList<CommentModel> commentModelArray) {
        this.context = context;
        this.commentModelArray = commentModelArray;
    }

    @NonNull
    @Override
    public CommentAdapter.CommentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.comment_layout, parent, false);

        return new CommentViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CommentAdapter.CommentViewHolder holder, int position) {

        holder.name.setText(commentModelArray.get(position).getDescr());
        holder.price.setText(commentModelArray.get(position).getUsername());
        Picasso.get().load(commentModelArray.get(position).getPhoto()).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return commentModelArray.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        TextView name, bio, price, category;
        ImageView image;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);


            name = itemView.findViewById(R.id.product_name);
            price = itemView.findViewById(R.id.product_price);
            image = itemView.findViewById(R.id.postImage);
        }
    }
}
