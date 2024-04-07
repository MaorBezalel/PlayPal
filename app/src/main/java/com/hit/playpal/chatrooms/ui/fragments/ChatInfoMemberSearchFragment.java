package com.hit.playpal.chatrooms.ui.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hit.playpal.R;
import com.hit.playpal.chatrooms.ui.adapters.UsersInGroupChatAdapter;
import com.hit.playpal.chatrooms.ui.viewmodels.ChatInfoViewModel;
import com.hit.playpal.entities.users.User;

import java.util.List;


public class ChatInfoMemberSearchFragment extends Fragment {
    private static final String TAG = "ChatInfoMemberSearchFragment";
    private static final String ARG_CHAT_ROOM_ID = "chatRoomId";
    private RecyclerView mUsersRecyclerView;
    private UsersInGroupChatAdapter mUsersInGroupChatAdapter;
    private ChatInfoViewModel mChatInfoViewModel;

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
    }

    private void initializeRecyclerView(List<User> iUsersList)
    {
        mUsersInGroupChatAdapter = new UsersInGroupChatAdapter(iUsersList);
        mUsersRecyclerView.setAdapter(mUsersInGroupChatAdapter);
        mUsersRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void initializeViewModel()
    {
        mChatInfoViewModel = new ViewModelProvider(this).get(ChatInfoViewModel.class);
    }

    private void initializeObservers()
    {
        mChatInfoViewModel.getGetMemberListSuccess().observe(getViewLifecycleOwner()  ,
                iSuccessMessage -> initializeRecyclerView(mChatInfoViewModel.getMembersList().getValue()));
    }

    private void getMembersList()
    {
        mChatInfoViewModel.getMembersListOfGroupChat(mChatRoomId);
    }
}