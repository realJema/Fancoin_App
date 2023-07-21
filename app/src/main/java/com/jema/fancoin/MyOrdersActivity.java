package com.jema.fancoin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.jema.fancoin.Adapter.OrderAdapter;
import com.jema.fancoin.Model.OrderModel;

import java.util.ArrayList;

public class MyOrdersActivity extends AppCompatActivity {
    private RecyclerView myOrdersFeed;

    ArrayList<OrderModel> orderModelArrayList;
    ImageView back;
    OrderAdapter orderAdapter;
    FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_orders);

        back = findViewById(R.id.my_orders_back_btn);
        myOrdersFeed = findViewById(R.id.myOrdersFeed);
        myOrdersFeed.setHasFixedSize(true);
        myOrdersFeed.setLayoutManager(new LinearLayoutManager( MyOrdersActivity.this , LinearLayoutManager.VERTICAL , false));


        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        orderModelArrayList = new ArrayList<OrderModel>();
        orderAdapter = new OrderAdapter(MyOrdersActivity.this, orderModelArrayList);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        myOrdersFeed.setAdapter(orderAdapter);
        MyOrdersChangeListener();
    }
    private void MyOrdersChangeListener() {

        db.collection("Orders").whereEqualTo("id", auth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error != null) {
//                            if(progressDialog.isShowing())
//                                progressDialog.dismiss();
                            Log.i("JemaTag", "error gettting data");
                            return;
                        }

                        for(DocumentChange dc : value.getDocumentChanges()){

                            String id = dc.getDocument().getId();
                            int oldIndex = orderModelArrayList.indexOf(id);

                            switch (dc.getType()){
                                case ADDED:
                                    orderModelArrayList.add(dc.getDocument().toObject(OrderModel.class));
                                    break;
                                case MODIFIED:

                                    // modifying

                                    String docID = dc.getDocument().getId();
                                    OrderModel changedModel = dc.getDocument().toObject(OrderModel.class);
                                    if (dc.getOldIndex() == dc.getNewIndex()) {
                                        // Item changed but remained in same position
                                        orderModelArrayList.set(dc.getOldIndex(),changedModel);
                                    }else {
                                        // Item changed and changed position
                                        orderModelArrayList.remove(dc.getOldIndex());
                                        orderModelArrayList.add(dc.getNewIndex(),changedModel);
                                        orderAdapter.notifyItemMoved(dc.getOldIndex(),dc.getNewIndex());
                                    }
                                    break;
                                case REMOVED:
                                    orderModelArrayList.remove(oldIndex);
                            }
                            orderAdapter.notifyDataSetChanged();
//                            if(progressDialog.isShowing())
//                                progressDialog.dismiss();
                        }
                    }
                });
    }
}