package com.jema.fancoin.HomeTabs;

import android.content.Intent;
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
import com.jema.fancoin.Database.User;
import com.jema.fancoin.Database.UserViewModel;
import com.jema.fancoin.Home;
import com.jema.fancoin.R;
import com.jema.fancoin.UserProfile.Admin.AdminActivity;
import com.jema.fancoin.UserProfile.RequestsActivity;
import com.jema.fancoin.UserProfile.SettingsActivity.SettingsActivity;
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
    private FirebaseAuth auth;
    private ProfileVideosAdapter videosAdapter;
    private ImageView pp;

    private ArrayList<String> videoPaths;
    private UserViewModel viewModel;
    private TextView username, usernametop, noVideos, bio, followers, following, requests, requestsBtnText, myOrdersBtnText;
    private FirebaseFirestore db;

    private ArrayList<String> videoPathslocal;
    public String numberOfRequests = "0";

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
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Home homeActivity = (Home) getActivity();
        User iData = homeActivity.mydata;

        Log.d("JemaTag", iData.username);

        // Inflate the layout for this fragment
        View rootView = inflater.inflate(R.layout.fragment_profile, container, false);

//        routing to different pages
        editProfileBtn = (Button) rootView.findViewById(R.id.editProfileBtn);
        adminBtn = (Button) rootView.findViewById(R.id.adminBtn);

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();
        viewModel = new ViewModelProvider(this).get(UserViewModel.class);

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

        username.setText("@".concat(iData.username));
        usernametop.setText(iData.full_name);
        bio.setText(iData.bio);
        Picasso.get().load(iData.image).into(pp);
        followers.setText(iData.followers);
        following.setText(iData.following);
        requests.setText(numberOfRequests);
        String requestStr = getString(R.string.requests);
        requestsPage.setText(requestStr.concat(" (").concat(numberOfRequests).concat(")"));

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

        ProfileVideosListener();
        RequestsListener();

        return rootView;

    }

    /**
     * Listen to changes in showcase videos on firebase for the current user
     */
    private void ProfileVideosListener() {
        db.collection("Users").document(auth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<DocumentSnapshot>() {
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
                        if (!videoPaths.contains(group.get(i))) {
                            videoPaths.add(group.get(i));
                        }
                    }
                    videosAdapter.notifyDataSetChanged();
                }
            }
        });
    }

    /**
     * Listening on the data base of orders to count all the commands passed for this user
     */
    private void RequestsListener() {

        db.collection("Orders").whereEqualTo("star_uid", auth.getCurrentUser().getUid()).addSnapshotListener(new EventListener<QuerySnapshot>() {
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
                    numberOfRequests = numberOrders;
                    requests.setText(numberOfRequests);
                    String requestStr = getString(R.string.requests);
                    requestsPage.setText(requestStr.concat(" (").concat(numberOfRequests).concat(")"));
                }
            }
        });
    }

}