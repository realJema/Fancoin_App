package com.jema.fancoin;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.QuerySnapshot;
import com.jema.fancoin.Adapter.OrderAdapter;
import com.jema.fancoin.Model.OrderModel;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link InboxFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class InboxFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private RecyclerView orderFeed;

    ArrayList<OrderModel> orderModelArrayList;
    OrderAdapter orderAdapter;
    FirebaseFirestore db;
    private FirebaseAuth auth;

    public InboxFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment InboxFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static InboxFragment newInstance(String param1, String param2) {
        InboxFragment fragment = new InboxFragment();
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
        View rootView = inflater.inflate(R.layout.fragment_inbox, container, false);


        orderFeed = (RecyclerView)rootView.findViewById(R.id.orderFeed);
        orderFeed.setHasFixedSize(true);
        orderFeed.setLayoutManager(new LinearLayoutManager(getContext() , LinearLayoutManager.VERTICAL , false));


        auth = FirebaseAuth.getInstance();
        db = FirebaseFirestore.getInstance();

        orderModelArrayList = new ArrayList<OrderModel>();
        orderAdapter = new OrderAdapter(getContext(), orderModelArrayList);


        orderFeed.setAdapter(orderAdapter);
        OrderChangeListener();



        return rootView;


    }
    private void OrderChangeListener() {

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