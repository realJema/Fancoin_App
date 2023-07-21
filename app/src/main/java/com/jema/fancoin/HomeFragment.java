package com.jema.fancoin;

import static android.content.ContentValues.TAG;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;
import android.widget.Toolbar;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.navigation.NavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jema.fancoin.Adapter.PostAdapter;
import com.jema.fancoin.Model.PostCard;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link HomeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class HomeFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;
    private RecyclerView tiktokFeed, entertainmentFeed, musicFeed;

    ArrayList<PostCard> postCardArrayList, tikCardArrayList, entCardArrayList, musCardArrayList;
    PostAdapter postAdapter, tikAdapter, entAdapter, musAdapter;
    FirebaseFirestore db;
    ProgressDialog progressDialog;
    private FirebaseAuth auth;
    public HomeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment HomeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static HomeFragment newInstance(String param1, String param2) {
        HomeFragment fragment = new HomeFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_home, container, false);
//
//        progressDialog = new ProgressDialog(getContext());
//        progressDialog.setCancelable(false);
//        progressDialog.setMessage("Fetching data...");
//        progressDialog.show();


        tiktokFeed = (RecyclerView)rootView.findViewById(R.id.tiktokFeed);
        tiktokFeed.setHasFixedSize(true);
        tiktokFeed.setLayoutManager(new LinearLayoutManager(getContext() , LinearLayoutManager.HORIZONTAL , false));

        entertainmentFeed = (RecyclerView)rootView.findViewById(R.id.entertainmentFeed);
        entertainmentFeed.setHasFixedSize(true);
        entertainmentFeed.setLayoutManager(new LinearLayoutManager(getContext()  , LinearLayoutManager.HORIZONTAL , false));

        musicFeed = (RecyclerView)rootView.findViewById(R.id.musicFeed);
        musicFeed.setHasFixedSize(true);
        musicFeed.setLayoutManager(new LinearLayoutManager(getContext()  , LinearLayoutManager.HORIZONTAL , false));


        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        postCardArrayList = new ArrayList<PostCard>();
        tikCardArrayList = new ArrayList<PostCard>();
        entCardArrayList = new ArrayList<PostCard>();
        musCardArrayList = new ArrayList<PostCard>();
        postAdapter = new PostAdapter(getContext(),postCardArrayList);
        tikAdapter = new PostAdapter(getContext(), tikCardArrayList);
        entAdapter = new PostAdapter(getContext(), entCardArrayList);
        musAdapter = new PostAdapter(getContext(), musCardArrayList);

        tiktokFeed.setAdapter(tikAdapter);


        entertainmentFeed.setAdapter(entAdapter);
        musicFeed.setAdapter(musAdapter);
        EventChangeListener();
        EventChangeListenerEnt();
        EventChangeListenerMus();

        return rootView;
    }


    private void EventChangeListener() {

        db.collection("Users").whereEqualTo("category", "Tiktok").whereEqualTo("application_status", "confirmed")
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
                            int oldIndex = tikCardArrayList.indexOf(id);

                            switch (dc.getType()){
                                case ADDED:
                                    tikCardArrayList.add(dc.getDocument().toObject(PostCard.class));
                                    break;
                                case MODIFIED:

                                    // modifying

                                    String docID = dc.getDocument().getId();
                                    PostCard changedModel = dc.getDocument().toObject(PostCard.class);
                                    if (dc.getOldIndex() == dc.getNewIndex()) {
                                        // Item changed but remained in same position
                                        tikCardArrayList.set(dc.getOldIndex(),changedModel);
                                    }else {
                                        // Item changed and changed position
                                        tikCardArrayList.remove(dc.getOldIndex());
                                        tikCardArrayList.add(dc.getNewIndex(),changedModel);
                                        tikAdapter.notifyItemMoved(dc.getOldIndex(),dc.getNewIndex());
                                    }
                                    break;
//                                case REMOVED:
//                                    tikCardArrayList.remove(oldIndex);
//                                    break;
                            }
                            tikAdapter.notifyDataSetChanged();
//                            if(progressDialog.isShowing())
//                                progressDialog.dismiss();
                        }
                    }
                });
    }
    private void EventChangeListenerEnt() {

        db.collection("Users").whereEqualTo("category", "Entertainment").whereEqualTo("application_status", "confirmed")
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if(error != null) {
//                            if(progressDialog.isShowing())
//                                progressDialog.dismiss();
                            Log.i("JemaTag", "error gettting data");
                            return;
                        }
//                        Log.i("JemaTag", "success gettting data");

                        for(DocumentChange dc : value.getDocumentChanges()){

                            String id = dc.getDocument().getId();
                            int oldIndex = entCardArrayList.indexOf(id);

                            switch (dc.getType()){
                                case ADDED:
                                    entCardArrayList.add(dc.getDocument().toObject(PostCard.class));
                                    break;
                                case MODIFIED:

                                    // modifying

                                    String docID = dc.getDocument().getId();
                                    PostCard changedModel = dc.getDocument().toObject(PostCard.class);
                                    if (dc.getOldIndex() == dc.getNewIndex()) {
                                        // Item changed but remained in same position
                                        entCardArrayList.set(dc.getOldIndex(),changedModel);
                                    }else {
                                        // Item changed and changed position
                                        entCardArrayList.remove(dc.getOldIndex());
                                        entCardArrayList.add(dc.getNewIndex(),changedModel);
                                        entAdapter.notifyItemMoved(dc.getOldIndex(),dc.getNewIndex());
                                    }
                                    break;
//                                case REMOVED:
//
//                                        entCardArrayList.remove(oldIndex);
//                                    break;

                            }
                            entAdapter.notifyDataSetChanged();
//                            if(progressDialog.isShowing())
//                                progressDialog.dismiss();
                        }
                    }
                });
    }

    private void EventChangeListenerMus() {

        db.collection("Users").whereEqualTo("category", "Music").whereEqualTo("application_status", "confirmed")
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
                            int oldIndex = musCardArrayList.indexOf(id);

                            switch (dc.getType()){
                                case ADDED:
                                    musCardArrayList.add(dc.getDocument().toObject(PostCard.class));
                                    break;
                                case MODIFIED:

                                    // modifying

                                    String docID = dc.getDocument().getId();
                                    PostCard changedModel = dc.getDocument().toObject(PostCard.class);
                                    if (dc.getOldIndex() == dc.getNewIndex()) {
                                        // Item changed but remained in same position
                                        musCardArrayList.set(dc.getOldIndex(),changedModel);
                                    }else {
                                        // Item changed and changed position
                                        musCardArrayList.remove(dc.getOldIndex());
                                        musCardArrayList.add(dc.getNewIndex(),changedModel);
                                        musAdapter.notifyItemMoved(dc.getOldIndex(),dc.getNewIndex());
                                    }
                                    break;
//                                case REMOVED:
//                                    musCardArrayList.remove(oldIndex);
//                                    break;
                            }
                            musAdapter.notifyDataSetChanged();
//                            if(progressDialog.isShowing())
//                                progressDialog.dismiss();
                        }
                    }
                });
    }

}
