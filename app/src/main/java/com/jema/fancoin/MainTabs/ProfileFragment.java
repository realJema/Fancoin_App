package com.jema.fancoin.MainTabs;

import static android.content.Context.MODE_PRIVATE;
import static com.jema.fancoin.Home.SHARED_PREFS;
import static com.jema.fancoin.Home.UBIO;
import static com.jema.fancoin.Home.UFOLLOWERS;
import static com.jema.fancoin.Home.UFOLLOWING;
import static com.jema.fancoin.Home.UIMAGE;
import static com.jema.fancoin.Home.UNAME;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.jema.fancoin.Adapter.ProfileVideosAdapter;
import com.jema.fancoin.AdminActivity;
import com.jema.fancoin.R;
import com.jema.fancoin.RequestsActivity;
import com.jema.fancoin.SettingsActivity.SettingsActivity;
import com.jema.fancoin.database.User;
import com.jema.fancoin.database.UserViewModel;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ProfileFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ProfileFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2, currentPP;
    private RecyclerView videosFeed;
    private Button editProfileBtn, adminBtn, requestsPage;
    private ArrayList<String> videoPaths;
    private FirebaseAuth auth;
    private ProfileVideosAdapter videosAdapter;
    private ImageView pp;
    private TextView username, usernametop, noVideos, bio, followers, following, requests, requestsBtnText, myOrdersBtnText;
    private FirebaseFirestore db;

    private UserViewModel viewModel;

    public ProfileFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ProfileFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ProfileFragment newInstance(String param1, String param2) {
        ProfileFragment fragment = new ProfileFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

//        routing to different pages
        editProfileBtn = (Button) rootView.findViewById(R.id.editProfileBtn);
        adminBtn = (Button) rootView.findViewById(R.id.adminBtn);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        pp = (ImageView) rootView.findViewById(R.id.profile_imageview);
        username = (TextView) rootView.findViewById(R.id.profile_username);
        usernametop = (TextView) rootView.findViewById(R.id.profile_username1);
        bio = (TextView) rootView.findViewById(R.id.profile_bio);
        noVideos = (TextView) rootView.findViewById(R.id.profile_no_videos);
        followers = (TextView) rootView.findViewById(R.id.profile_followers_count);
        following = (TextView) rootView.findViewById(R.id.profile_following_count);
        requests = (TextView) rootView.findViewById(R.id.profile_requests_count);
//        requestsBtnText = (TextView)rootView.findViewById(R.id.profile_requests_page_text);
        requestsPage = (Button) rootView.findViewById(R.id.profile_requests);
        videosFeed = (RecyclerView) rootView.findViewById(R.id.profile_videos_feed);


        SharedPreferences mySharedPreferences = getActivity().getSharedPreferences(SHARED_PREFS, MODE_PRIVATE);
        String myusername = mySharedPreferences.getString(UNAME, "username");
        String mybio = mySharedPreferences.getString(UBIO, null);
        String myimage = mySharedPreferences.getString(UIMAGE, null);
        String myfollowersnum = mySharedPreferences.getString(UFOLLOWERS, "0");
        String myfollowingnum = mySharedPreferences.getString(UFOLLOWING, "0");

        viewModel = new ViewModelProvider(this).get(UserViewModel.class);
        viewModel.getUserInfo().observe(getViewLifecycleOwner(), new Observer<User>() {
            @Override
            public void onChanged(User user) {
                if (user.username != null) {
                    username.setText("@".concat(user.username));
                    usernametop.setText("@".concat(user.username));
                } else {
                    username.setText("@".concat("empty"));
                    usernametop.setText("@".concat("empty"));

                }
            }
        });

        if(mybio.equalsIgnoreCase("")){
            bio.setText("(no bio, update in settings)");
        } else {
            bio.setText(mybio);
        }
        Picasso.get().load(myimage).into(pp);
        followers.setText(myfollowersnum);
        following.setText(myfollowingnum);


//        videosFeed.setHasFixedSize(true);
        RecyclerView.LayoutManager mLayoutManager = new GridLayoutManager(getActivity(), 3);
        videosFeed.setLayoutManager(mLayoutManager);
        videoPaths = new ArrayList<>();
        videosAdapter = new ProfileVideosAdapter(getActivity().getApplicationContext(), videoPaths, getActivity());
        videosFeed.setAdapter(videosAdapter);

//        button triggers
        editProfileBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), SettingsActivity.class);
                startActivity(intent);
            }
        });
        adminBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), AdminActivity.class);
                startActivity(intent);
            }
        });
        requestsPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RequestsActivity.class);
                startActivity(intent);
            }
        });

        EventChangeListener(); // listening for changes to update image, name and profile picture
        OrderChangeListener(); // listening for changes to orders collection to update requests counts and orders
//        MyOrderChangeListener();

        return rootView;

    }


    /**
     * Listening on the data base of orders to count all the commands passed for this user
     */
    private void OrderChangeListener() {

        db.collection("Orders").whereEqualTo("star_uid", auth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException error) {

                        if (error != null) {
//                            if(progressDialog.isShowing())
//                                progressDialog.dismiss();
                            Log.i("JemaTag", "error gettting data");
                            return;
                        }

                        for (DocumentChange dc : value.getDocumentChanges()) {

                            String numberOrders = String.valueOf(value.getDocuments().size());
                            requests.setText(numberOrders);
                            requestsPage.setText("Requests (".concat(numberOrders).concat(")"));
                        }
                    }
                });
    }


    /**
     * Listen to changes in showcase videos on firebase for the current user
     *
     */
    private void EventChangeListener() {
        db.collection("Users").document(auth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if (error != null) {
                            Log.e("Firebase Error", error.getMessage());
                            return;
                        }
                        List<String> group = (List<String>) value.get("showcase");

//                        adding videos to profile
                        if (group != null && group.size() != 0) {
                            noVideos.setVisibility(View.GONE);
                            for (int i = 0; i < group.size(); i++) {
                                if(!videoPaths.contains(group.get(i))) {
                                    videoPaths.add(group.get(i));
                                }
                            }
                            videosAdapter.notifyDataSetChanged();
                        }
                    }
                });
    }

}