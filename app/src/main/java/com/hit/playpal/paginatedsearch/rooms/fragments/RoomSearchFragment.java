package com.hit.playpal.paginatedsearch.rooms.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.paging.LoadState;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hit.playpal.R;
import com.hit.playpal.entities.chats.AllChatRoom;
import com.hit.playpal.home.adapters.groupchats.AllGroupChatsAdapter;
import com.hit.playpal.profile.adapters.RoomsOfUserAdapter;
import com.hit.playpal.paginatedsearch.rooms.enums.RoomFilterType;
import com.hit.playpal.paginatedsearch.rooms.enums.RoomSearchType;
import com.hit.playpal.paginatedsearch.rooms.adapters.IRoomAdapter;
import com.hit.playpal.paginatedsearch.rooms.adapters.RoomAdapter;

public class RoomSearchFragment extends Fragment {
    private static final String TAG = "RoomSearchFragment";
    private static final String ARG_SEARCH_TYPE = "searchType";
    private static final String ARG_USER_ID = "userId";
    private static final String ARG_ROOM_ID = "roomId";
    private static final String ROOM_SEARCH_ALL_TITLE = "Room Browser";
    private static final String ROOM_SEARCH_JOINED_TITLE = "Joined Rooms";
    private RecyclerView mGroupChatRecyclerView;
    private RoomAdapter<?> mGroupChatAdapter;
    private SearchView mSearchView;
    private Button mSearchButton;
    private RoomFilterType mCurrentRoomFilterType;

    private LinearLayout mFilterLayout;

    private Button mFilterAllBtn;
    private Button mFilterByRoomNameBtn;
    private Button mFilterByGameNameBtn;

    private ProgressBar mProgressBar;
    private TextView mNoResultsFound;
    private TextView mDbError;

    private String mCurrentSearchQuery;



    private RoomSearchType mCurrentRoomSearchType;
    private String mCurrentUserId;

    private TextView mRoomsSearchTitle;

    private boolean isFragmentFirstCreation = true;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_search_rooms, container, false);

        if(getArguments() != null)
        {
            mCurrentRoomSearchType = RoomSearchType.valueOf(getArguments().getString(ARG_SEARCH_TYPE));

            if(mCurrentRoomSearchType == RoomSearchType.JOINED)
            {
                mCurrentUserId = getArguments().getString(ARG_USER_ID);
            }
        }

        initializeUI(view);
        initializeRecyclerView();
        handleOnClicksEvents();

        return view;
    }

    private void initializeUI(View iView)
    {
        mRoomsSearchTitle = iView.findViewById(R.id.group_chats_fragment_title);
        mGroupChatRecyclerView = iView.findViewById(R.id.group_chats_fragment_list);
        mSearchView = iView.findViewById(R.id.group_chats_fragment_search);

        mProgressBar = iView.findViewById(R.id.group_chats_fragment_progressBar);
        mNoResultsFound = iView.findViewById(R.id.group_chats_fragment_no_rooms_found_error);
        mDbError = iView.findViewById(R.id.group_chats_fragment_db_error);

        mSearchButton = iView.findViewById(R.id.group_chats_fragment_searchBtn);

        mFilterAllBtn = iView.findViewById(R.id.group_chats_fragment_allBtn);
        mFilterByRoomNameBtn = iView.findViewById(R.id.group_chats_fragment_byRoomNameBtn);
        mFilterByGameNameBtn = iView.findViewById(R.id.group_chats_fragment_byGameNameBtn);

        mFilterLayout = iView.findViewById(R.id.group_chats_filter_layout);
    }

    private void initializeRecyclerView()
    {
        switch(mCurrentRoomSearchType)
        {
            case ALL:
                mGroupChatAdapter = new AllGroupChatsAdapter(new IRoomAdapter<AllChatRoom>() {
                    // TODO: implement onRoomClick after chat profile is created
                    @Override
                    public void onRoomClick(String roomId) {
                        // Intent intent = new Intent(getContext(), ChatInfoProfile.class);
                        // intent.putExtra(ARG_ROOM_ID, roomId);
                        // startActivity(intent);
                    }
                }, this);

                mRoomsSearchTitle.setText(ROOM_SEARCH_ALL_TITLE);
                break;
            case JOINED:
                mGroupChatAdapter = new RoomsOfUserAdapter(new IRoomAdapter<AllChatRoom>() {
                    // TODO: implement onRoomClick after chat profile is created
                    @Override
                    public void onRoomClick(String roomId) {
                        // Intent intent = new Intent(getContext(), ChatInfoProfile.class);
                        // intent.putExtra(ARG_ROOM_ID, roomId);
                        // startActivity(intent);
                    }
                }, this, mCurrentUserId);

                mRoomsSearchTitle.setText(ROOM_SEARCH_JOINED_TITLE);
                break;
        }

        mGroupChatRecyclerView.setAdapter(mGroupChatAdapter);
        mGroupChatRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));

        mCurrentSearchQuery = "";
        mCurrentRoomFilterType = RoomFilterType.NONE;

        mGroupChatAdapter.addLoadStateListener(loadStates -> {
            if (loadStates.getRefresh() instanceof LoadState.Loading)
            {
                mProgressBar.setVisibility(View.VISIBLE);
                mGroupChatRecyclerView.setVisibility(View.GONE);
                mNoResultsFound.setVisibility(View.GONE);
                mDbError.setVisibility(View.GONE);
            } else if (loadStates.getRefresh() instanceof LoadState.Error)
            {
                mProgressBar.setVisibility(View.GONE);
                mGroupChatRecyclerView.setVisibility(View.GONE);
                mNoResultsFound.setVisibility(View.GONE);
                mDbError.setVisibility(View.VISIBLE);
            } else if (loadStates.getRefresh() instanceof LoadState.NotLoading)
            {
                if(mGroupChatAdapter.getItemCount() == 0)
                {
                    mProgressBar.setVisibility(View.GONE);
                    mGroupChatRecyclerView.setVisibility(View.GONE);
                    mNoResultsFound.setVisibility(View.VISIBLE);
                    mDbError.setVisibility(View.GONE);
                }
                else
                {
                    mGroupChatRecyclerView.setVisibility(View.VISIBLE);
                    mProgressBar.setVisibility(View.GONE);
                    mNoResultsFound.setVisibility(View.GONE);
                    mDbError.setVisibility(View.GONE);
                }
            }
            return null;
        });
    }

    private void handleOnClicksEvents() {
        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startQuery(mSearchView.getQuery().toString(), mCurrentRoomFilterType);
            }
        });

        mFilterAllBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentRoomFilterType = RoomFilterType.NONE;
                startQuery("", mCurrentRoomFilterType);
                mFilterLayout.setVisibility(View.GONE);
            }
        });

        mFilterByGameNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentRoomFilterType = RoomFilterType.BY_GAME_NAME;
                mFilterLayout.setVisibility(View.VISIBLE);

                mSearchView.setQuery("", false);
                mSearchView.setQueryHint("Search by game name...");
            }
        });

        mFilterByRoomNameBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mCurrentRoomFilterType = RoomFilterType.BY_ROOM_NAME;
                mFilterLayout.setVisibility(View.VISIBLE);

                mSearchView.setQuery("", false);
                mSearchView.setQueryHint("Search by room name...");
            }
        });
    }

    private void startQuery(String iUpdatedQuery, RoomFilterType iRoomFilterType)
    {
        if(mCurrentRoomFilterType == iRoomFilterType && iUpdatedQuery.trim().equals(mCurrentSearchQuery.trim())) return;

        mCurrentSearchQuery = iUpdatedQuery;
        mCurrentRoomFilterType = iRoomFilterType;

        mGroupChatAdapter.applyFilters(iUpdatedQuery, iRoomFilterType);
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mGroupChatAdapter != null && !isFragmentFirstCreation && mCurrentRoomSearchType == RoomSearchType.JOINED)
        {
            mGroupChatAdapter.refresh();
        }
        isFragmentFirstCreation = false;
    }
}
