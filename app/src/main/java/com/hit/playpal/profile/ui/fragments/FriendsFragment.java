package com.hit.playpal.profile.ui.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.hit.playpal.R;
import com.hit.playpal.entities.users.Relationship;

import com.hit.playpal.entities.users.enums.RelationshipStatus;
import com.hit.playpal.home.adapters.IBindableUser;
import com.hit.playpal.home.adapters.IUserAdapter;
import com.hit.playpal.home.adapters.UserAdapter;

import com.hit.playpal.profile.ui.activities.ProfileActivity;

import java.util.ArrayList;

public class FriendsFragment extends Fragment {
    private String Uid;
    private RecyclerView mRecyclerView;
    private UserAdapter<Relationship> mUserAdapter;

    public FriendsFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            Uid = getArguments().getString("userId");
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

        mRecyclerView = view.findViewById(R.id.recyclerViewFriends);
        mUserAdapter = new UserAdapter<Relationship>(new IUserAdapter() {
            @Override
            public void onUserClick(String iUserId) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                intent.putExtra("userId", iUserId);
                startActivity(intent);
            }
        }, new IBindableUser<Relationship>() {
            @Override
            public String getUserId(Relationship iItem) {
                return iItem.getOther_user().getUid();
            }

            @Override
            public String getUserImage(Relationship iItem) {
                return iItem.getOther_user().getProfilePicture();
            }

            @Override
            public String getDisplayName(Relationship iItem) {
                return iItem.getOther_user().getDisplayName();
            }
        }, this, FirebaseFirestore.getInstance().collection("users").document(Uid).collection("relationships").whereEqualTo("status",RelationshipStatus.friends), Relationship.class);
        mRecyclerView.setAdapter(mUserAdapter);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        Button friendsSearchButton = view.findViewById(R.id.friendsSearchButton);
        SearchView friendsSearchView = view.findViewById(R.id.searchViewFriends);

        friendsSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String query = friendsSearchView.getQuery().toString();
                if (query.trim().isEmpty()) {
                    Toast.makeText(getContext(), "Please enter a search query", Toast.LENGTH_SHORT).show();
                    return;
                }
                mUserAdapter.applyFilters(query);
            }
        });

        Button friendsClearButton = view.findViewById(R.id.friendsClearButton);
        friendsClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserAdapter.applyFilters("");
            }
        });

        return view;
    }
}