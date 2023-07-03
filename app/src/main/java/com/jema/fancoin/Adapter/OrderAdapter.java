package com.jema.fancoin.Adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jema.fancoin.Model.OrderModel;
import com.jema.fancoin.R;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.OrderViewHolder> {

    Context context;
    ArrayList<OrderModel> orderModelArrayList;


    public OrderAdapter(Context context, ArrayList<OrderModel> orderModelArrayList) {
        this.context = context;
        this.orderModelArrayList = orderModelArrayList;
    }

    @NonNull
    @Override
    public OrderAdapter.OrderViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(context).inflate(R.layout.order_layout, parent, false);

        return new OrderViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull OrderAdapter.OrderViewHolder holder, int position) {
        holder.recipient.setText(orderModelArrayList.get(position).getRecipient());
        holder.star_name.setText(orderModelArrayList.get(position).getStar_name());
        holder.date.setText(orderModelArrayList.get(position).getDate());
        holder.descr.setText(orderModelArrayList.get(position).getDescription());
        Picasso.get().load(orderModelArrayList.get(position).getStar_image()).into(holder.image);

    }

    @Override
    public int getItemCount() {
        return orderModelArrayList.size();
    }

    public class OrderViewHolder extends RecyclerView.ViewHolder {

        TextView recipient, star_name, descr, date;
        ImageView image;
        public OrderViewHolder(@NonNull View itemView) {
            super(itemView);

            star_name = itemView.findViewById(R.id.order_star_name);
            descr = itemView.findViewById(R.id.order_description);
            recipient = itemView.findViewById(R.id.order_recipient_name);
            date = itemView.findViewById(R.id.order_date);
            image = itemView.findViewById(R.id.order_profile_image);

        }
    }
}
