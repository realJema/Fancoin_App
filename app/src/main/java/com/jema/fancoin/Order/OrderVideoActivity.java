package com.jema.fancoin.Order;

import static com.jema.fancoin.Home.SHARED_PREFS;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.jema.fancoin.R;

import com.jema.fancoin.Database.User;
import com.jema.fancoin.Database.UserViewModel;
import com.squareup.picasso.Picasso;

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
    BottomSheetDialog dialog;
    private FirebaseAuth auth;
    private String username, useremail, userphone, userimage;
    private UserViewModel viewModel;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_order_video);
        dialog = new BottomSheetDialog(
                this,
                R.style.ThemeOverlay_App_BottomSheetDialog
        );

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

        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        viewModel.getUserInfo().observe(this, new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user.username != null) {
//        setting elements in drawer
                    username = user.username;
                    useremail = user.email;
                    userimage = user.image;
                    userphone = user.phone; // this might be null
                }
            }
        });

        orderVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if(recipient.getText().toString().matches("") ){
                    Toast.makeText(OrderVideoActivity.this, R.string.order_recipient_and_description,Toast.LENGTH_LONG).show();
                    return;
                }
                if( description.getText().toString().matches("")){
                    Toast.makeText(OrderVideoActivity.this,R.string.order_recipient_and_description,Toast.LENGTH_LONG).show();
                    return;
                }
                showDialog();

            }
        });


        SharedPreferences sharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
//        Log.d("JemaTag", sharedPreferences.getString(USERNAME, ""));

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private String showDialog() {
        View view = getLayoutInflater().inflate(R.layout.order_bottomsheet, null);
        Button sendOrder = view.findViewById(R.id.order_send_order_btn);
        TextView descr = view.findViewById(R.id.order_bottomsheet_descr);
        TextView pricing = view.findViewById(R.id.order_bottomsheet_pricing);

        descr.setText("Your are about to order from ".concat(name));
        pricing.setText("Pricing : ".concat(price).concat(" XAF"));

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
                order.put("date", new Date());
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
                        Toast.makeText(OrderVideoActivity.this, R.string.unable_to_send_order, Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });
        dialog.setCancelable(true);
        dialog.setContentView(view);
        dialog.show();
        return null;
    }
}