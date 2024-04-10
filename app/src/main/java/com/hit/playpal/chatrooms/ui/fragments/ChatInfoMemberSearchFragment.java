package com.hit.playpal.chatrooms.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.hit.playpal.R;
import com.hit.playpal.chatrooms.ui.adapters.UsersInGroupChatAdapter;
import com.hit.playpal.chatrooms.ui.viewmodels.ChatInfoViewModel;
import com.hit.playpal.entities.chats.GroupProfile;
import com.hit.playpal.paginatedsearch.users.adapters.IUserAdapter;
import com.hit.playpal.profile.ui.activities.ProfileActivity;

import java.util.List;


public class ChatInfoMemberSearchFragment extends Fragment {
    private static final String TAG = "ChatInfoMemberSearchFragment";
    private static final String ARG_USER_ID = "userId";
    private static final String ARG_CHAT_ROOM_ID = "chatRoomId";
    private static final String MEMBER_SEARCH_TITLE = "Members";
    private RecyclerView mUsersRecyclerView;
    private UsersInGroupChatAdapter mUsersInGroupChatAdapter;
    private ChatInfoViewModel mChatInfoViewModel;

    private ProgressBar mProgressBar;
    private TextView mNoUsersFound;
    private TextView mDbError;

    private SearchView mSearchView;
    private Button mBtnSearch;
    private Button mBtnClear;

    private TextView mSearchTitle;

    private String mCurrentSearchQuery = "";
    private String mChatRoomId;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_users, container, false);

        getChatRoomIdFromArguments();
        initializeUI(view);
        initializeViewModel();
        initializeObservers();
        getMembersList();
        setLoadingState();

        return view;
    }

    private void getChatRoomIdFromArguments()
    {
        if(getArguments() != null)
        {
            mChatRoomId = getArguments().getString(ARG_CHAT_ROOM_ID);
        }
        else
        {
            throw new IllegalArgumentException("ChatRoomId is not passed to ChatInfoMemberSearchFragment");
        }
    }

    private void initializeUI(View iView)
    {
        mUsersRecyclerView = iView.findViewById(R.id.fragment_search_users_list);
        mProgressBar = iView.findViewById(R.id.fragment_search_users_progressBar);
        mNoUsersFound = iView.findViewById(R.id.fragment_search_users_not_found);
        mDbError = iView.findViewById(R.id.fragment_search_users_db_error);
        mBtnSearch = iView.findViewById(R.id.fragment_search_searchBtn);
        mBtnClear = iView.findViewById(R.id.fragment_search_clearBtn);
        mSearchView = iView.findViewById(R.id.fragment_search_bar_users);
        mSearchTitle = iView.findViewById(R.id.fragment_search_title);
        mSearchTitle.setText(MEMBER_SEARCH_TITLE);

        initializeButtonEvents();
    }

    private void initializeRecyclerView(List<GroupProfile.Participant> iUsersList)
    {
        mUsersInGroupChatAdapter = new UsersInGroupChatAdapter(iUsersList, new IUserAdapter() {
            @Override
            public void onUserClick(String iUserId) {
                Intent intent = new Intent(getContext(), ProfileActivity.class);
                intent.putExtra(ARG_USER_ID, iUserId);
                startActivity(intent);
            }
        });
        mUsersRecyclerView.setAdapter(mUsersInGroupChatAdapter);
        mUsersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        updateUIAfterFilteringOrFetching();
    }

    private void initializeViewModel()
    {
        mChatInfoViewModel = new ViewModelProvider(this).get(ChatInfoViewModel.class);
    }

    private void initializeObservers()
    {
        mChatInfoViewModel.getGetMemberListSuccess().observe(getViewLifecycleOwner()  ,
                iSuccessMessage -> initializeRecyclerView(mChatInfoViewModel.getMembersList().getValue()));

        mChatInfoViewModel.getGetMemberListError().observe(getViewLifecycleOwner() ,
                iErrorMessage -> setDbErrorState());
    }

    private void initializeButtonEvents()
    {
        mBtnSearch.setOnClickListener(v -> searchUsers());
        mBtnClear.setOnClickListener(v -> clearSearch());
    }

    private void searchUsers()
    {
        String updatedQuery = mSearchView.getQuery().toString();

        if (updatedQuery.trim().equals(mCurrentSearchQuery.trim())) return;

        mUsersInGroupChatAdapter.applyFilters(updatedQuery);

        mCurrentSearchQuery = updatedQuery;

        mSearchView.clearFocus();

        updateUIAfterFilteringOrFetching();
    }

    private void clearSearch()
    {
        if (mCurrentSearchQuery.trim().isEmpty()) return;

        mCurrentSearchQuery = "";

        mUsersInGroupChatAdapter.applyFilters(mCurrentSearchQuery);

        mSearchView.setQuery("", false);
        mSearchView.clearFocus();

        updateUIAfterFilteringOrFetching();
    }

    private void updateUIAfterFilteringOrFetching()
    {
        if(mUsersInGroupChatAdapter.getItemCount() == 0)
        {
            setNoUsersState();
        }
        else
        {
            setLoadedState();
        }
    }
    private void getMembersList()
    {
        mChatInfoViewModel.getMembersListOfGroupChat(mChatRoomId);
    }

    private void setLoadingState()
    {
        mProgressBar.setVisibility(View.VISIBLE);
        mUsersRecyclerView.setVisibility(View.GONE);
        mNoUsersFound.setVisibility(View.GONE);
        mDbError.setVisibility(View.GONE);
    }
    private void setNoUsersState()
    {
        mNoUsersFound.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mUsersRecyclerView.setVisibility(View.GONE);
        mDbError.setVisibility(View.GONE);
    }
    private void setDbErrorState()
    {
        mDbError.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mUsersRecyclerView.setVisibility(View.GONE);
        mNoUsersFound.setVisibility(View.GONE);
    }
    private void setLoadedState()
    {
        mUsersRecyclerView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.GONE);
        mNoUsersFound.setVisibility(View.GONE);
        mDbError.setVisibility(View.GONE);
    }
}