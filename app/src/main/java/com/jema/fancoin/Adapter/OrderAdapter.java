package com.jema.fancoin.Adapter;

import android.content.Context;
import android.content.Intent;
import android.icu.text.RelativeDateTimeFormatter;
import android.text.format.DateUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.jema.fancoin.Model.OrderModel;
import com.jema.fancoin.OrderDetailsActivity;
import com.jema.fancoin.PostDetails;
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
        holder.recipient.setText("Recipient : ".concat(orderModelArrayList.get(position).getRecipient()));
        holder.star_name.setText(orderModelArrayList.get(position).getStar_name());
        holder.date.setText(orderModelArrayList.get(position).getDate());
        holder.descr.setText(orderModelArrayList.get(position).getDescription());
        Picasso.get().load(orderModelArrayList.get(position).getStar_image()).into(holder.image);


//
//        long now = System.currentTimeMillis();
//        long due = now + 864000;
//
//        String theTime = (String) DateUtils.getRelativeTimeSpanString(now, 0L, DateUtils.FORMAT_ABBREV_ALL);
//        Log.d("jematag", theTime);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent i = new Intent(context, OrderDetailsActivity.class);
                i.putExtra("date", orderModelArrayList.get(position).getDate());
                i.putExtra("description", orderModelArrayList.get(position).getDescription());
                i.putExtra("recipient", orderModelArrayList.get(position).getRecipient());
                i.putExtra("id", orderModelArrayList.get(position).getId());
                i.putExtra("star_uid", orderModelArrayList.get(position).getStar_uid());
                i.putExtra("star_image", orderModelArrayList.get(position).getStar_image());
                i.putExtra("star_pricing", orderModelArrayList.get(position).getStar_pricing());
                i.putExtra("star_name", orderModelArrayList.get(position).getStar_name());
                i.putExtra("client_uid", orderModelArrayList.get(position).getClient_uid());
                i.putExtra("client_image", orderModelArrayList.get(position).getClient_image());
                i.putExtra("client_name", orderModelArrayList.get(position).getClient_name());
                i.putExtra("client_phonenumber", orderModelArrayList.get(position).getClient_phoneNumber());
                i.putExtra("client_email", orderModelArrayList.get(position).getClient_email());

                context.startActivity(i);

            }
        });

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
