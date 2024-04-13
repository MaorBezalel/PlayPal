package com.hit.playpal.home.ui.fragments.chats;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.paging.LoadState;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.firebase.firestore.Query;
import com.hit.playpal.R;
import com.hit.playpal.chatrooms.ui.activities.ChatRoomActivity;
import com.hit.playpal.chatrooms.ui.enums.ChatRoomLocation;
import com.hit.playpal.entities.chats.ChatRoom;
import com.hit.playpal.entities.chats.group.GroupChatRoom;
import com.hit.playpal.entities.chats.o2o.OneToOneChatRoom;
import com.hit.playpal.entities.chats.enums.ChatRoomType;
import com.hit.playpal.home.ui.adapters.recentchats.ChatsRecyclerViewAdapter;
import com.hit.playpal.home.ui.viewmodels.ChatsViewModel;

import javax.annotation.Nullable;

public class ChatsTabFragment extends Fragment {
    private static final String TAG = "AllChatRoomsTabFragment";
    private ChatsViewModel mChatsViewModel;

    private RecyclerView mRecyclerView;
    private ChatsRecyclerViewAdapter mChatsRecyclerViewAdapter;
    private LinearLayoutManager mLinearLayoutManager;
    private ProgressBar mProgressBar;
    private TextView mNoResultsFound;
    private TextView mDbError;

    @Nullable private ChatRoomType mChatRoomType;

    public ChatsTabFragment() { }

    public ChatsTabFragment(@Nullable ChatRoomType iChatRoomType) {
        mChatRoomType = iChatRoomType;
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater iInflater, ViewGroup iContainer, Bundle iSavedInstanceState) {
        return iInflater.inflate(R.layout.fragment_recent_chats_tab_recyclerview, iContainer, false);
    }

    @Override
    public void onViewCreated(@NonNull View iView, Bundle iSavedInstanceState) {
        super.onViewCreated(iView, iSavedInstanceState);

        initViewModel();
        initLinearLayoutManagerForRecyclerView();
        initAdapterForRecyclerView();
        initRecyclerView(iView);
        initProgressBarAndLoadingState(iView);
    }

    private void initViewModel() {
        mChatsViewModel = new ChatsViewModel();
    }

    private void initLinearLayoutManagerForRecyclerView() {
        mLinearLayoutManager = new LinearLayoutManager(getContext());
    }

    private void initAdapterForRecyclerView() {
        LifecycleOwner lifecycleOwner = getViewLifecycleOwner();
        Query query = (mChatRoomType == null)
                ? mChatsViewModel.generateQueryForAllChatRooms()
                : mChatsViewModel.generateQueryForSpecificChatRooms(mChatRoomType);

        mChatsRecyclerViewAdapter = new ChatsRecyclerViewAdapter(this::handleChatRoomCardClicked, lifecycleOwner, query);
    }

    private void initRecyclerView(@NonNull View iView) {
        mRecyclerView = iView.findViewById(R.id.recyclerview_chats_tab);
        mRecyclerView.setLayoutManager(mLinearLayoutManager);
        mRecyclerView.setAdapter(mChatsRecyclerViewAdapter);
    }

    private void initProgressBarAndLoadingState(@NonNull View iView) {
        mProgressBar = iView.findViewById(R.id.progressbar_chats_tab_loading_chats);
        mNoResultsFound = iView.findViewById(R.id.textview_chats_tab_no_results_found);
        mDbError = iView.findViewById(R.id.textview_chats_tab_error);

        if (mChatsRecyclerViewAdapter == null) {
            throw new IllegalStateException("Adapter not initialized");
        } else {
            mChatsRecyclerViewAdapter.addLoadStateListener(loadStates -> {
                if (loadStates.getRefresh() instanceof LoadState.Loading) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mNoResultsFound.setVisibility(View.INVISIBLE);
                    mDbError.setVisibility(View.INVISIBLE);
                    mRecyclerView.setVisibility(View.INVISIBLE);
                } else if (loadStates.getRefresh() instanceof LoadState.Error) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mNoResultsFound.setVisibility(View.INVISIBLE);
                    mDbError.setVisibility(View.VISIBLE);
                    mRecyclerView.setVisibility(View.INVISIBLE);
                } else if (loadStates.getRefresh() instanceof LoadState.NotLoading) {
                    if (mChatsRecyclerViewAdapter.getItemCount() == 0) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        mNoResultsFound.setVisibility(View.VISIBLE);
                        mDbError.setVisibility(View.INVISIBLE);
                        mRecyclerView.setVisibility(View.INVISIBLE);
                    } else {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        mNoResultsFound.setVisibility(View.INVISIBLE);
                        mDbError.setVisibility(View.INVISIBLE);
                        mRecyclerView.setVisibility(View.VISIBLE);
                    }
                }

                return null;
            });
        }
    }

    private void handleChatRoomCardClicked(ChatRoom iChatRoom) {
        Intent intent = new Intent(getContext(), ChatRoomActivity.class);
        intent.putExtra("chatRoomLocation", ChatRoomLocation.CHAT_BODY);

        if (iChatRoom instanceof GroupChatRoom) {
            intent.putExtra("chatRoom", (GroupChatRoom) iChatRoom);
            startActivity(intent);
        } else if (iChatRoom instanceof OneToOneChatRoom) {
            intent.putExtra("chatRoom", (OneToOneChatRoom) iChatRoom);
            startActivity(intent);
        } else {
            Log.e(TAG, "ChatRoom type not recognized");
            throw new IllegalStateException("ChatRoom type not recognized");
        }
    }
}