package com.jema.fancoin;

import static android.content.ContentValues.TAG;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.jema.fancoin.SettingsActivity.SettingsActivity;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

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

    private Button editProfileBtn, adminBtn;
    private FirebaseAuth auth;
    private ImageView pp;
    private TextView username, email, phone, followers, following, requests, requestsBtnText, myOrdersBtnText;
    private ConstraintLayout requestPage, ordersPage;
    private FirebaseFirestore db;
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
        editProfileBtn = (Button)rootView.findViewById(R.id.editProfileBtn);
        adminBtn = (Button)rootView.findViewById(R.id.adminBtn);
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

        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        pp = (ImageView)rootView.findViewById(R.id.profile_imageview);
        username = (TextView)rootView.findViewById(R.id.profile_username);
        phone = (TextView)rootView.findViewById(R.id.profile_phone);
        email = (TextView)rootView.findViewById(R.id.profile_email);
        followers = (TextView)rootView.findViewById(R.id.profile_followers_count);
        following = (TextView)rootView.findViewById(R.id.profile_following_count);
        requests = (TextView)rootView.findViewById(R.id.profile_requests_count);
        requestsBtnText = (TextView)rootView.findViewById(R.id.profile_requests_page_text);
        myOrdersBtnText = (TextView)rootView.findViewById(R.id.profile_orders_page_text);
        requestPage = (ConstraintLayout)rootView.findViewById(R.id.profile_requests_page);
        ordersPage = (ConstraintLayout)rootView.findViewById(R.id.profile_orders_page);

        requestPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), RequestsActivity.class);
                startActivity(intent);
            }
        });
        ordersPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), MyOrdersActivity.class);
                startActivity(intent);
            }
        });


        EventChangeListener(); // listening for changes to update image, name and profile picture
        OrderChangeListener(); // listening for changes to orders collection to update requests counts and orders
        MyOrderChangeListener();

        return rootView;

    }


    private void OrderChangeListener() {

        db.collection("Orders").whereEqualTo("star_uid", auth.getCurrentUser().getUid())
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

                            String numberOrders = String.valueOf(value.getDocuments().size());
                            requests.setText(numberOrders);
                            requestsBtnText.setText("Requests (".concat(numberOrders).concat(")"));
                        }
                    }
                });
    }


    private void MyOrderChangeListener() {

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

                            String numberOrders = String.valueOf(value.getDocuments().size());
                            myOrdersBtnText.setText("My Orders (".concat(numberOrders).concat(")"));
                        }
                    }
                });
    }

    private void EventChangeListener() {

        db.collection("Users").document(auth.getCurrentUser().getUid())
                .addSnapshotListener(new EventListener<DocumentSnapshot>() {
                    @Override
                    public void onEvent(@Nullable DocumentSnapshot value, @Nullable FirebaseFirestoreException error) {
                        if(error != null) {
                            Log.e("Firebase Error", error.getMessage());
                            return;
                        }


                        String user = value.getString("name");
                        String image = value.getString("image");
                        String useremail = value.getString("email");
                        String phoneNumber = value.getString("phoneNumber");
                        List<String> myFollowers = (List<String>) value.get("followers");
                        List<String> myFollowing = (List<String>) value.get("following");

                        if(!user.equalsIgnoreCase(username.getText().toString())){
                            username.setText(user);
                        }
                        if(!useremail.equalsIgnoreCase(email.getText().toString())){
                            email.setText(useremail);
                        }
                        if(!phoneNumber.equalsIgnoreCase(phone.getText().toString())){
                            phone.setText(phoneNumber);
                        }
                        if(!image.equalsIgnoreCase(currentPP)){
                            Picasso.get().load(image).into(pp);
                            currentPP = value.getString("image");
                        }
                        if(myFollowers != null){
                            followers.setText(String.valueOf(myFollowers.size()));
                        }
                        if(myFollowing != null){
                            following.setText(String.valueOf(myFollowing.size()));
                        }
                    }

                });
    }
}