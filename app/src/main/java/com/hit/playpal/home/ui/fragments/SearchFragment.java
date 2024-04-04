package com.hit.playpal.home.ui.fragments;

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

import com.google.firebase.firestore.FirebaseFirestore;
import com.hit.playpal.R;
import com.hit.playpal.entities.users.User;
import com.hit.playpal.home.adapters.classes.AllUsersAdapter;
import com.hit.playpal.home.adapters.interfaces.IBindableUser;
import com.hit.playpal.home.adapters.interfaces.IUserAdapter;
import com.hit.playpal.home.adapters.classes.UserAdapter;
import com.hit.playpal.profile.ui.activities.ProfileActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link SearchFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class SearchFragment extends Fragment {

    private RecyclerView mUsersRecyclerView;
    private SearchView mUserSearchView;
    private UserAdapter<User> mUserAdapter;
    private Button mUserSearchBtn;
    private Button mUserClearBtn;
    private String mCurrentSearchQuery;
    private ProgressBar mProgressBar;
    private TextView mNoUsersFound;
    private TextView mErrorText;
    public SearchFragment() {

    }

    public static SearchFragment newInstance(String param1, String param2) {
        SearchFragment fragment = new SearchFragment();

        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search, container, false);
        initializeUI(view);
        return view;
    }

    private void initializeUI(View view) {
        mProgressBar = view.findViewById(R.id.fragment_search_users_progressBar);
        mNoUsersFound = view.findViewById(R.id.fragment_search_users_not_found);
        mErrorText = view.findViewById(R.id.fragment_search_users_db_error);

        mUsersRecyclerView = view.findViewById(R.id.fragment_search_users_list);
        mUserSearchView = view.findViewById(R.id.fragment_search_bar_users);
        mUsersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mUserAdapter = new AllUsersAdapter(new IUserAdapter() {
            @Override
            public void onUserClick(String iUserId) {
                Intent intent = new Intent(getActivity(), ProfileActivity.class);
                intent.putExtra("userId", iUserId);
                startActivity(intent);
            }
        }, this);
        mUsersRecyclerView.setAdapter(mUserAdapter);

        mUserSearchBtn = view.findViewById(R.id.fragment_search_searchBtn);
        mUserClearBtn = view.findViewById(R.id.fragment_search_clearBtn);

        mCurrentSearchQuery = "";
        mUserSearchBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String newQuery = mUserSearchView.getQuery().toString();
                if(!mCurrentSearchQuery.equals(newQuery)) {
                    mCurrentSearchQuery = newQuery;
                    mUserAdapter.applyFilters(mCurrentSearchQuery);
                    mUserSearchView.clearFocus();
                }
            }
        });

        mUserClearBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mUserSearchView.setQuery("", false);
                if(!mCurrentSearchQuery.trim().isEmpty())
                {
                    mCurrentSearchQuery = "";
                    mUserAdapter.applyFilters(mCurrentSearchQuery);
                    mUserSearchView.clearFocus();
                }
            }
        });


        mUserAdapter.addLoadStateListener(loadStates -> {
            if(loadStates.getRefresh() instanceof LoadState.NotLoading)
            {
                if(mUserAdapter.getItemCount() == 0)
                {
                    mUsersRecyclerView.setVisibility(View.GONE);
                    mNoUsersFound.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                    mErrorText.setVisibility(View.GONE);
                }
                else
                {
                    mUsersRecyclerView.setVisibility(View.VISIBLE);
                    mNoUsersFound.setVisibility(View.GONE);
                    mProgressBar.setVisibility(View.GONE);
                    mErrorText.setVisibility(View.GONE);
                }
            }
            else if (loadStates.getRefresh() instanceof LoadState.Loading)
            {
                mUsersRecyclerView.setVisibility(View.GONE);
                mNoUsersFound.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.VISIBLE);
                mErrorText.setVisibility(View.GONE);
            }
            else if (loadStates.getRefresh() instanceof LoadState.Error)
            {
                mUsersRecyclerView.setVisibility(View.GONE);
                mNoUsersFound.setVisibility(View.GONE);
                mProgressBar.setVisibility(View.GONE);
                mErrorText.setVisibility(View.VISIBLE);
            }
            return null;
        });
    }





}