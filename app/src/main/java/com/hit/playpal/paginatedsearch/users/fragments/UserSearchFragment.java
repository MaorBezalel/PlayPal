package com.hit.playpal.paginatedsearch.users.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.paging.LoadState;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hit.playpal.R;
import com.hit.playpal.home.adapters.users.AllUsersAdapter;
import com.hit.playpal.profile.adapters.FriendsOfUserAdapter;
import com.hit.playpal.profile.ui.activities.ProfileActivity;
import com.hit.playpal.paginatedsearch.users.enums.UserSearchType;
import com.hit.playpal.paginatedsearch.users.adapters.IUserAdapter;
import com.hit.playpal.paginatedsearch.users.adapters.UserAdapter;

public class UserSearchFragment extends Fragment {

    private static final String TAG = "UserSearchFragment";

    private static final String ARG_SEARCH_TYPE = "searchType";
    private static final String ARG_USER_ID = "userId";

    private static final String USER_SEARCH_ALL_TITLE = "User Browser";
    private static final String USER_FRIENDS_SEARCH_TITLE = "Friends Browser";

    private RecyclerView mUsersRecyclerView;
    private SearchView mUserSearchView;

    // In generic code, the question mark (?), called the 'wildcard', represents an unknown type.
    private UserAdapter<?> mUserAdapter;
    private Button mUserSearchBtn;
    private Button mUserClearBtn;
    private String mCurrentSearchQuery;
    private ProgressBar mProgressBar;
    private TextView mNoUsersFound;
    private TextView mErrorText;

    private TextView mUserSearchTitle;


    private UserSearchType mCurrentUserSearchType;
    private String mCurrentUserId;

    private boolean isFragmentFirstCreation = true;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_users, container, false);

        if(getArguments() != null)
        {
            mCurrentUserSearchType = UserSearchType.valueOf(getArguments().getString(ARG_SEARCH_TYPE));

            if(mCurrentUserSearchType == UserSearchType.FRIENDS)
            {
                mCurrentUserId = getArguments().getString(ARG_USER_ID);
            }
        }
        else mCurrentUserSearchType = UserSearchType.ALL;

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

        mUserSearchTitle = view.findViewById(R.id.fragment_search_title);

        switch(mCurrentUserSearchType)
        {
            case ALL:
                mUserAdapter = new AllUsersAdapter(new IUserAdapter() {
                    @Override
                    public void onUserClick(String iUserId) {
                        Intent intent = new Intent(getActivity(), ProfileActivity.class);
                        intent.putExtra(ARG_USER_ID, iUserId);
                        startActivity(intent);
                    }
                }, this);

                mUserSearchTitle.setText(USER_SEARCH_ALL_TITLE);
                break;

            case FRIENDS:
                mUserAdapter = new FriendsOfUserAdapter(new IUserAdapter() {
                    @Override
                    public void onUserClick(String iUserId) {
                        Intent intent = new Intent(getActivity(), ProfileActivity.class);
                        intent.putExtra(ARG_USER_ID, iUserId);
                        startActivity(intent);
                    }
                }, this, mCurrentUserId);

                mUserSearchTitle.setText(USER_FRIENDS_SEARCH_TITLE);
                break;
        }

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

    @Override
    public void onResume() {
        super.onResume();
        if(mUserAdapter != null && !isFragmentFirstCreation && mCurrentUserSearchType == UserSearchType.FRIENDS)
        {
            mUserAdapter.refresh();
        }
        isFragmentFirstCreation = false;
    }
}
