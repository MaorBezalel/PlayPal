package com.hit.playpal.profile.ui.fragments;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.hit.playpal.R;
import com.hit.playpal.entities.users.Relationship;
import com.hit.playpal.entities.users.enums.RelationshipStatus;
import com.hit.playpal.profile.domain.usecases.GetFriendListByDisplayNameUseCase;
import com.hit.playpal.profile.ui.activities.ProfileActivity;
import com.hit.playpal.profile.ui.adapters.FriendsAdapter;

import java.util.ArrayList;
import java.util.List;

public class FriendsFragment extends Fragment implements FriendsAdapter.OnFriendClickListener {
    private List<Relationship> friends;
    private String Uid;
    private RecyclerView recyclerView;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public FriendsFragment() {
        // Required empty public constructor
    }

    public static FriendsFragment newInstance(String param1, String param2) {
        FriendsFragment fragment = new FriendsFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    private DocumentSnapshot lastVisible = null;
    private int limit = 10; // Number of friends to display

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
            Uid = getArguments().getString("Uid");
            friends = new ArrayList<>();
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_friends, container, false);
        Button buttonReturn = view.findViewById(R.id.buttonFriendsReturn);
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        // Initialize the RecyclerView
        recyclerView = view.findViewById(R.id.recyclerViewFriends);

        // Set a layout manager for the RecyclerView
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        // Set an adapter for the RecyclerView
        FriendsAdapter adapter = new FriendsAdapter(friends, this);
        recyclerView.setAdapter(adapter);

        // Fetch the friends and update the RecyclerView
        GetFriendListByDisplayNameUseCase getFriendListByDisplayNameUseCase = new GetFriendListByDisplayNameUseCase();
        getFriendListByDisplayNameUseCase.execute(Uid, lastVisible, limit).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<Relationship> relationships = task.getResult();
                if (!relationships.isEmpty()) {
                    friends.addAll(relationships);
                    recyclerView.getAdapter().notifyDataSetChanged();
                }
            } else {
                Log.e("FriendsFragment", "Error getting friends: ", task.getException());
            }
        });

        return view;
    }

    @Override
    public void onFriendClick(Relationship relationship) {
        // Create an intent to start the ProfileActivity
        Intent intent = new Intent(getActivity(), ProfileActivity.class);
        // Pass the UID of the clicked user as an extra in the intent
        intent.putExtra("Uid", relationship.getOther_user().getUid());
        // Start the ProfileActivity
        startActivity(intent);
    }
}