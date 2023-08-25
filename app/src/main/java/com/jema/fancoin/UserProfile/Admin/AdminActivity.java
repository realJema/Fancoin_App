package com.jema.fancoin.UserProfile.Admin;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.jema.fancoin.Adapter.AdminAdapter;
import com.jema.fancoin.Model.PostCard;
import com.jema.fancoin.R;

import java.util.ArrayList;

public class AdminActivity extends AppCompatActivity {

    private RecyclerView usersFeed;

    ArrayList<PostCard> postCardArrayListt;
    ImageView back;
    AdminAdapter postAdapter;
    FirebaseFirestore db;

    private GridLayoutManager layoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);

        db = FirebaseFirestore.getInstance();

        back = findViewById(R.id.admin_back_btn);

        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(AdminActivity.this, 2);

        usersFeed = findViewById(R.id.usersFeed);
        usersFeed.setLayoutManager(mLayoutManager);
        usersFeed.setHasFixedSize(true);

        postCardArrayListt = new ArrayList<PostCard>();
        postAdapter = new AdminAdapter(AdminActivity.this, postCardArrayListt);

        usersFeed.setAdapter(postAdapter);


        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        EventChangeListener();

    }

    private void EventChangeListener() {

        db.collection("Users").orderBy("category")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error != null) {
//                            if(progressDialog.isShowing())
//                                progressDialog.dismiss();
                            Log.i("JemaTag", getString(R.string.error_getting_data));
                            return;
                        }

                        for(DocumentChange dc : value.getDocumentChanges()){

                            String id = dc.getDocument().getId();
                            int oldIndex = postCardArrayListt.indexOf(id);

                            switch (dc.getType()){
                                case ADDED:
                                    postCardArrayListt.add(dc.getDocument().toObject(PostCard.class));
                                    break;
                                case MODIFIED:

                                    // modifying

                                    String docID = dc.getDocument().getId();
                                    PostCard changedModel = dc.getDocument().toObject(PostCard.class);
                                    if (dc.getOldIndex() == dc.getNewIndex()) {
                                        // Item changed but remained in same position
                                        postCardArrayListt.set(dc.getOldIndex(),changedModel);
                                    }else {
                                        // Item changed and changed position
                                        postCardArrayListt.remove(dc.getOldIndex());
                                        postCardArrayListt.add(dc.getNewIndex(),changedModel);
                                        postAdapter.notifyItemMoved(dc.getOldIndex(),dc.getNewIndex());
                                    }
                                    break;
                                case REMOVED:
                                    postCardArrayListt.remove(oldIndex);
                            }
                            postAdapter.notifyDataSetChanged();
//                            if(progressDialog.isShowing())
//                                progressDialog.dismiss();
                        }
                    }
                });
    }
}