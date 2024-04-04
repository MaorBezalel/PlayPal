package com.hit.playpal.profile.ui.fragments;


import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.paging.LoadState;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.firestore.FirebaseFirestore;
import com.hit.playpal.R;
import com.hit.playpal.entities.users.Relationship;

import com.hit.playpal.entities.users.enums.RelationshipStatus;
import com.hit.playpal.home.adapters.classes.FriendsOfUserAdapter;
import com.hit.playpal.home.adapters.interfaces.IBindableUser;
import com.hit.playpal.home.adapters.interfaces.IUserAdapter;
import com.hit.playpal.home.adapters.classes.UserAdapter;

import com.hit.playpal.profile.ui.activities.ProfileActivity;

public class FriendsFragment extends Fragment {
    private String Uid;
    private RecyclerView mRecyclerView;
    private UserAdapter<Relationship> mUserAdapter;

    private Button mUserSearchBtn;
    private Button mUserClearBtn;

    private ProgressBar mProgressBar;
    private TextView mNoUsersFound;
    private TextView mErrorText;
    private SearchView mUserSearchView;
    private String mCurrentSearchQuery;
    private boolean isFragmentFirstCreation = true;

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

        mProgressBar = view.findViewById(R.id.fragment_friends_progressBar);
        mNoUsersFound = view.findViewById(R.id.fragment_friends_no_friends_found_error);
        mErrorText = view.findViewById(R.id.fragment_friends_db_error);

        mRecyclerView = view.findViewById(R.id.recyclerViewFriends);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(getActivity()));

        mUserSearchBtn = view.findViewById(R.id.friendsSearchButton);
        mUserClearBtn = view.findViewById(R.id.friendsClearButton);

        mCurrentSearchQuery = "";
        mUserSearchView = view.findViewById(R.id.searchViewFriends);

        mUserAdapter = new FriendsOfUserAdapter(new IUserAdapter() {
            @Override
            public void onUserClick(String iUserId) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra("userId", iUserId);
                startActivity(intent);
            }
        }, this, Uid);

        mUserAdapter.addLoadStateListener(loadStates -> {
            if (loadStates.getRefresh() instanceof LoadState.Error) {
                mProgressBar.setVisibility(View.GONE);
                mErrorText.setVisibility(View.VISIBLE);
                mNoUsersFound.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);
            } else if (loadStates.getRefresh() instanceof LoadState.Loading) {
                mProgressBar.setVisibility(View.VISIBLE);
                mErrorText.setVisibility(View.GONE);
                mNoUsersFound.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);
            } else if (loadStates.getRefresh() instanceof LoadState.NotLoading) {
                if (mUserAdapter.getItemCount() == 0) {
                    mProgressBar.setVisibility(View.GONE);
                    mErrorText.setVisibility(View.GONE);
                    mNoUsersFound.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.GONE);
                } else {
                    mProgressBar.setVisibility(View.GONE);
                    mErrorText.setVisibility(View.GONE);
                    mNoUsersFound.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                }
            }
            return null;
        });

        mRecyclerView.setAdapter(mUserAdapter);

        mUserSearchBtn.setOnClickListener(v -> {
            String newQuery = mUserSearchView.getQuery().toString();
            if(!mCurrentSearchQuery.equals(newQuery)) {
                mCurrentSearchQuery = newQuery;
                mUserAdapter.applyFilters(mCurrentSearchQuery);
                mUserSearchView.clearFocus();
            }

        });

        mUserClearBtn.setOnClickListener(v -> {
            mUserSearchView.setQuery("", false);
            if(!mCurrentSearchQuery.trim().isEmpty())
            {
                mCurrentSearchQuery = "";
                mUserAdapter.applyFilters(mCurrentSearchQuery);
                mUserSearchView.clearFocus();
            }
        });


        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mUserAdapter != null && !isFragmentFirstCreation)
        {
            mUserAdapter.refresh();
        }
        isFragmentFirstCreation = false;
    }
}