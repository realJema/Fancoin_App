package com.jema.fancoin.Adapter;

import android.content.Context;
import android.util.Log;
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

import org.ocpsoft.prettytime.PrettyTime;

import java.util.ArrayList;
import java.util.List;

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

        PrettyTime p = new PrettyTime();

        holder.commentName.setText(commentModelArray.get(position).getCommenter_username());
        holder.commentComment.setText(commentModelArray.get(position).getDescr());
        holder.commentDate.setText(p.format(commentModelArray.get(position).getDate()));
        Picasso.get().load(commentModelArray.get(position).getCommenter_photo()).into(holder.commentImage);

    }

    @Override
    public int getItemCount() {
        return commentModelArray.size();
    }

    public class CommentViewHolder extends RecyclerView.ViewHolder {

        TextView commentName, commentComment, commentDate;
        ImageView commentImage;

        public CommentViewHolder(@NonNull View itemView) {
            super(itemView);

            commentName = itemView.findViewById(R.id.commenter_name);
            commentComment = itemView.findViewById(R.id.commenter_comment);
            commentImage = itemView.findViewById(R.id.comment_image);
            commentDate = itemView.findViewById(R.id.comment_date);
        }
    }
}
