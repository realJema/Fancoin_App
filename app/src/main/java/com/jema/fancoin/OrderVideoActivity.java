package com.jema.fancoin;

import static com.jema.fancoin.Home.SHARED_PREFS;
import static com.jema.fancoin.Home.USEREMAIL;
import static com.jema.fancoin.Home.USERIMAGE;
import static com.jema.fancoin.Home.USERNAME;
import static com.jema.fancoin.Home.USERPHONENUMBER;
import static org.xmlpull.v1.XmlPullParser.TEXT;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;

public class OrderVideoActivity extends AppCompatActivity {


    ImageView img, back, pp;
    TextView proName, proPrice, proDesc, proCategory;
    EditText recipient, description;

    String name, price, desc, cat, image, id;
    Button orderVideo;

    Button bottomsheet;

    FirebaseFirestore db;
    private FirebaseAuth auth;
    private String username, useremail, userphone, userimage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_video);

        Intent i = getIntent();

        name = i.getStringExtra("name");
        price = i.getStringExtra("price");
        desc = i.getStringExtra("bio");
        cat = i.getStringExtra("category");
        image = i.getStringExtra("image");
        id = i.getStringExtra("id");

        proName = findViewById(R.id.order_star_name);
        proDesc = findViewById(R.id.order_star_bio);
        back = findViewById(R.id.order_back_btn);
        pp = findViewById(R.id.order_profile_image);
        orderVideo = findViewById(R.id.order_get_video_btns);

        recipient = findViewById(R.id.order_recipient_input);
        description = findViewById(R.id.order_description_input);

        db = FirebaseFirestore.getInstance();
        auth = FirebaseAuth.getInstance();


        proName.setText(name);
        proDesc.setText(desc);
        Picasso.get().load(image).into(pp);


//                get client info from local preferences
        SharedPreferences mySharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);

        username = mySharedPreferences.getString(USERNAME, null);
        useremail = mySharedPreferences.getString(USEREMAIL, null);
        userimage = mySharedPreferences.getString(USERIMAGE, null);
        userphone = mySharedPreferences.getString(USERPHONENUMBER, null);

        orderVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(recipient.getText().toString().matches("") ){
                    Toast.makeText(OrderVideoActivity.this,"Please Input Recipient and Description",Toast.LENGTH_LONG).show();
                    return;
                }
                if( description.getText().toString().matches("")){
                    Toast.makeText(OrderVideoActivity.this,"Please Input Recipient and Description",Toast.LENGTH_LONG).show();
                    return;
                }
                showDialog();

            }
        });


        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        Log.d("JemaTag", sharedPreferences.getString(USERNAME, ""));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
    private void showDialog() {

        final Dialog dialog = new Dialog(this);
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        dialog.setContentView(R.layout.bottom_sheet_layout);

        Button sendOrder = dialog.findViewById(R.id.order_send_order_btn);
        TextView descr = dialog.findViewById(R.id.order_bottomsheet_descr);

        descr.setText("Your are about to order from ".concat(name));

        sendOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.dismiss();

                HashMap<String , Object> order = new HashMap<>();

                order.put("star_uid", id);
                order.put("star_image", image);
                order.put("star_pricing", price);
                order.put("star_name", name);

                order.put("client_uid", auth.getCurrentUser().getUid());
                order.put("client_image", userimage);
                order.put("client_name", username);
                order.put("client_phoneNumber", userphone);
                order.put("client_email", useremail);


                order.put("recipient" , recipient.getText().toString());
                order.put("description", description.getText().toString());
                order.put("date", getDateTime());
                order.put("id", auth.getCurrentUser().getUid());

                db.collection("Orders").document().set(order).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(OrderVideoActivity.this, "Order Sent", Toast.LENGTH_SHORT).show();
                        Intent myIntent = new Intent(OrderVideoActivity.this, SuccessOrderActivity.class);
                        myIntent.putExtra("name", name);
                        myIntent.putExtra("image", image);
                        startActivity(myIntent);
                        finish();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(OrderVideoActivity.this, "Unable to send, Try again", Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });

        dialog.show();
        dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT,ViewGroup.LayoutParams.WRAP_CONTENT);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        dialog.getWindow().getAttributes().windowAnimations = R.style.DialogAnimation;
        dialog.getWindow().setGravity(Gravity.BOTTOM);

    }

    private String getDateTime() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}