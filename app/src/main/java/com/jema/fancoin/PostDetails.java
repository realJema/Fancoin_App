package com.jema.fancoin;

import static com.jema.fancoin.Home.SHARED_PREFS;
import static com.jema.fancoin.Home.UIMAGE;
import static com.jema.fancoin.Home.UNAME;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

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
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.jema.fancoin.Adapter.CommentAdapter;
import com.jema.fancoin.Adapter.VideoSliderAdapter;
import com.jema.fancoin.Model.CommentModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;


public class PostDetails extends AppCompatActivity {

    ImageView img, back, pp;
    TextView proName, bottomTitle, bottomSubtitle, proDesc, proCategory, follow, noComment, noShowcase, addReview;

    String name, price, desc, cat, image, artistId, currentUserId;

    public List<CommentModel> commentsList;
    Button orderVideo;
    private FirebaseAuth auth;
    private FirebaseFirestore db;
    private RecyclerView commentsFeed;

    ArrayList<CommentModel> commentsArrayList;
    CommentAdapter commentAdapter;
    private RecyclerView showcaseFeed;
    private VideoSliderAdapter myAdapter;
    BottomSheetDialog dialog;
    ArrayList<String> videoPaths;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_details);

//        viewPager = findViewById(R.id.videoViewPager);
        showcaseFeed = findViewById(R.id.showcaseFeed);
        dialog = new BottomSheetDialog(this);

        videoPaths = new ArrayList<>();

//        We only show showcase videos if there are links in videoPaths

        showcaseFeed.setHasFixedSize(true);
        showcaseFeed.setLayoutManager(new LinearLayoutManager(PostDetails.this, LinearLayoutManager.HORIZONTAL, false));

        myAdapter = new VideoSliderAdapter(getApplicationContext(), videoPaths,
                PostDetails.this);
        showcaseFeed.setAdapter(myAdapter);
        showcaseFeed.setPadding(10, 0, 10, 0);


        Intent i = getIntent();
        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        currentUserId = auth.getCurrentUser().getUid();

//        getting post data and passing from previous activity

        name = i.getStringExtra("name");
        price = i.getStringExtra("price");
        desc = i.getStringExtra("bio");
        cat = i.getStringExtra("category");
        image = i.getStringExtra("image");
        artistId = i.getStringExtra("id");

        proName = findViewById(R.id.productName);
        proDesc = findViewById(R.id.prodBio);
//        proPrice = findViewById(R.id.prodPrice);
//        img = findViewById(R.id.big_image);
        back = findViewById(R.id.back2);
        proCategory = findViewById(R.id.prodCategory);
        pp = findViewById(R.id.details_profile_image);
        follow = findViewById(R.id.details_follow_btn);
        orderVideo = findViewById(R.id.details_get_video_btn);
        bottomTitle = findViewById(R.id.post_details_username);
        bottomSubtitle = findViewById(R.id.post_details_username_subtitle);
        noShowcase = findViewById(R.id.post_details_no_videos);
        noComment = findViewById(R.id.post_details_no_comments);
        addReview = findViewById(R.id.post_details_add_review);

        proName.setText(name);
//        proPrice.setText(price);
        proDesc.setText(desc);
        proCategory.setText(cat);
        bottomTitle.setText(name);
        bottomSubtitle.setText("Follow ".concat(name).concat(" to receive updates when they post"));


        showcaseFeed.setVisibility(View.GONE);
        noComment.setVisibility(View.VISIBLE);

//        Picasso.get().load(image).into(img);
        Picasso.get().load(image).into(pp);

//        loading comments into recycler
        commentsFeed = findViewById(R.id.commentsFeed);
        commentsFeed.setHasFixedSize(true);
        commentsFeed.setLayoutManager(new LinearLayoutManager(PostDetails.this, LinearLayoutManager.VERTICAL, false));
        commentsArrayList = new ArrayList<CommentModel>();
        commentAdapter = new CommentAdapter(PostDetails.this, commentsArrayList);
        commentsFeed.setAdapter(commentAdapter);


        addReview.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showDialog();
            }
        });
        follow.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Log.d("JemaTag", "Follow button clicked");
                String choice = follow.getText().toString();
                if (choice.equalsIgnoreCase("follow")) {
                    AddFollowing();
                } else {
                    RemoveFollowing();
                }
            }
        });
        orderVideo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Intent i = new Intent(PostDetails.this, OrderVideoActivity.class);
                i.putExtra("name", name);
//                i.putExtra("image", postCardArrayList.get(position).getBigimageurl());
                i.putExtra("price", price);
                i.putExtra("bio", proDesc.getText());
                i.putExtra("category", proCategory.getText());
                i.putExtra("image", image);
                i.putExtra("id", artistId);

                PostDetails.this.startActivity(i);
            }
        });
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });


        EventChangeListener();
        CommentsChangeListener();
    }

    private void CommentsChangeListener() {
        db.collection("Comments")
                .whereEqualTo("star_uid", artistId)
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (value.isEmpty()) {
                            noComment.setVisibility(View.VISIBLE);
                            commentsFeed.setVisibility(View.GONE);
                            return;
                        }


//                        Log.d("JemaTag", String.valueOf(value.getDocuments().size()));
                        noComment.setVisibility(View.GONE);
                        commentsFeed.setVisibility(View.VISIBLE);

                        for (DocumentChange dc : value.getDocumentChanges()) {

                            String id = dc.getDocument().getId();
                            int oldIndex = commentsArrayList.indexOf(id);

                            switch (dc.getType()) {
                                case ADDED:
                                    commentsArrayList.add(dc.getDocument().toObject(CommentModel.class));
                                    break;
                                case MODIFIED:
                                    // modifying

                                    String docID = dc.getDocument().getId();
                                    CommentModel changedModel = dc.getDocument().toObject(CommentModel.class);
                                    if (dc.getOldIndex() == dc.getNewIndex()) {
                                        // Item changed but remained in same position
                                        commentsArrayList.set(dc.getOldIndex(), changedModel);
                                    } else {
                                        // Item changed and changed position
                                        commentsArrayList.remove(dc.getOldIndex());
                                        commentsArrayList.add(dc.getNewIndex(), changedModel);
                                        commentAdapter.notifyItemMoved(dc.getOldIndex(), dc.getNewIndex());
                                    }
                                    break;
//                                case REMOVED:
//                                    tikCardArrayList.remove(oldIndex);
//                                    break;
                            }
                            commentAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

    private void EventChangeListener() {

        db.collection("Users").document(artistId)
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("Firebase Error", error.getMessage());
                            return;
                        }
                        List<String> group = (List<String>) value.get("followers");
                        List<String> shocaseVideos = (List<String>) value.get("showcase");


//                        displaying showcase videos
                        if (shocaseVideos != null) {
//                            Log.d("JemaTag", "Contains Showcase");
                            for (int j = 0; j < shocaseVideos.size(); j++) {
                                videoPaths.add(shocaseVideos.get(j));
//                                Log.d("JemaTag", shocaseVideos.get(j));
                            }

                            showcaseFeed.setVisibility(View.VISIBLE);
                            noShowcase.setVisibility(View.GONE);
                        } else {
                            showcaseFeed.setVisibility(View.GONE);
                            noShowcase.setVisibility(View.VISIBLE);
                        }


//                        settings following status
                        if (group != null) {
                            if (group.contains(auth.getCurrentUser().getUid())) {
                                follow.setText("Unfollow");
                            } else {
                                follow.setText("Follow");
                            }

                        }

//                        displaying comments


                    }

                });
    }

    private void AddFollowing() {
        db.collection("Users").document(currentUserId).update("following", FieldValue.arrayUnion(artistId)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {

                db.collection("Users").document(artistId).update("followers", FieldValue.arrayUnion(currentUserId)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(PostDetails.this, "Followed", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PostDetails.this, "Unable to update", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PostDetails.this, "Unable to update", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void RemoveFollowing() {
        db.collection("Users").document(artistId).update("followers", FieldValue.arrayRemove(currentUserId)).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                db.collection("Users").document(currentUserId).update("following", FieldValue.arrayRemove(artistId)).addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void unused) {
                        Toast.makeText(PostDetails.this, "Unfollowed", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PostDetails.this, "Unable to update", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(PostDetails.this, "Unable to update", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private String showDialog() {
        View view = getLayoutInflater().inflate(R.layout.comment_bottomsheet, null);
        Button sendComment = view.findViewById(R.id.comment_send_btn);
        TextView descr = view.findViewById(R.id.comment_text);

        sendComment.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (descr.getText().toString().equalsIgnoreCase("")) {
                    Toast.makeText(PostDetails.this, "Please enter a comment", Toast.LENGTH_SHORT).show();
                    return;
                }
                dialog.dismiss();


                SharedPreferences mySharedPreferences = getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
                String uname = mySharedPreferences.getString(UNAME, null);
                String uimage = mySharedPreferences.getString(UIMAGE, null);


                HashMap<String, Object> order = new HashMap<>();

                order.put("commenter_uid", currentUserId);
                order.put("commenter_username", uname);
                order.put("commenter_photo", uimage);
                order.put("date", new Date());
                order.put("descr", descr.getText().toString());
                order.put("star_uid", artistId);

                db.collection("Comments").add(order).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Toast.makeText(PostDetails.this, "Comment Sent", Toast.LENGTH_SHORT).show();
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(PostDetails.this, "Unable to send, Try again", Toast.LENGTH_SHORT).show();
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