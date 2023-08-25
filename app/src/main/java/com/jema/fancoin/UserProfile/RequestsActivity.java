package com.jema.fancoin.UserProfile;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import com.jema.fancoin.Adapter.ReplyAdapter;
import com.jema.fancoin.Model.OrderModel;
import com.jema.fancoin.R;

import java.util.ArrayList;

public class RequestsActivity extends AppCompatActivity {
    private String mParam1;
    private String mParam2;

    private RecyclerView requestsFeed;

    ArrayList<OrderModel> orderModelArrayList;
    ImageView back;
    ReplyAdapter replyAdapter;
    FirebaseFirestore db;
    private FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_requests);


        back = findViewById(R.id.requests_back_btn);
        requestsFeed = findViewById(R.id.requestsFeed);
        requestsFeed.setHasFixedSize(true);
        requestsFeed.setLayoutManager(new LinearLayoutManager( RequestsActivity.this , LinearLayoutManager.VERTICAL , false));


        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        orderModelArrayList = new ArrayList<OrderModel>();
        replyAdapter = new ReplyAdapter(RequestsActivity.this, orderModelArrayList);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        requestsFeed.setAdapter(replyAdapter);
        RequestsChangeListener();

    }
    private void RequestsChangeListener() {

        db.collection("Orders").whereEqualTo("star_uid", auth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error != null) {
//                            if(progressDialog.isShowing())
//                                progressDialog.dismiss();
                            Log.i("JemaTag", "error gettting data at Requests Activity");
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
                                    OrderModel changedModel = dc.getDocument().toObject(OrderModel.class);
                                    if (dc.getOldIndex() == dc.getNewIndex()) {
                                        // Item changed but remained in same position
                                        orderModelArrayList.set(dc.getOldIndex(),changedModel);
                                    }else {
                                        // Item changed and changed position
                                        orderModelArrayList.remove(dc.getOldIndex());
                                        orderModelArrayList.add(dc.getNewIndex(),changedModel);
                                        replyAdapter.notifyItemMoved(dc.getOldIndex(),dc.getNewIndex());
                                    }
                                    break;
                                case REMOVED:
                                    orderModelArrayList.remove(oldIndex);
                            }
                            replyAdapter.notifyDataSetChanged();
//                            if(progressDialog.isShowing())
//                                progressDialog.dismiss();
                        }
                    }
                });
    }
}