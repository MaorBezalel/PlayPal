package com.hit.playpal.home.ui.fragments.chats;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.hit.playpal.R;
import com.hit.playpal.entities.chats.GroupChatRoom;
import com.hit.playpal.home.adapters.creategroupchat.InitialMembersAdapter;
import com.hit.playpal.home.data.repositories.CreateGroupChatRoomRepository;
import com.hit.playpal.home.domain.usecases.GenerateQueryForAllUsersUseCase;
import com.hit.playpal.home.domain.usecases.chats.CreateGroupChatRoomUseCase;
import com.hit.playpal.utils.CurrentlyLoggedUser;

public class GroupChatInitialMembersFormFragment extends Fragment {
    private static final String TAG = "GroupChatGameFormFragment";
    private final CreateGroupChatRoomDialogFragment FRAGMENT_PARENT = (CreateGroupChatRoomDialogFragment) getParentFragment();

    private ImageButton mBackButton;
    private MaterialButton mCreateGroupChatButton;
    private RecyclerView mInitialMembersRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private InitialMembersAdapter mInitialMembersAdapter;

    @Override
    public View onCreateView(@NonNull LayoutInflater iInflater, ViewGroup container, Bundle savedInstanceState) {
        return iInflater.inflate(R.layout.fragment_group_chat_initial_members_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View iView, Bundle savedInstanceState) {
        super.onViewCreated(iView, savedInstanceState);
    }

    private void initBackButton(@NonNull View iView) {
        mBackButton = iView.findViewById(R.id.imagebutton_create_group_chat_back);
        mBackButton.setOnClickListener(v -> requireActivity().onBackPressed());
    }

    private void initCreateGroupChatButton(@NonNull View iView) {
        mCreateGroupChatButton = iView.findViewById(R.id.button_create_group_chat_create);
        mCreateGroupChatButton.setOnClickListener(v -> {
            if (mInitialMembersAdapter.getSelectedUsers().size() < InitialMembersAdapter.MINIMUM_NUMBER_OF_MEMBERS) {
                Toast.makeText(requireContext(), "Please select at least 2 initial members", Toast.LENGTH_SHORT).show();
            } else {
                GroupChatRoom groupChatRoom = FRAGMENT_PARENT.getViewModel().GROUP_CHAT_ROOM_TO_CREATE;
                groupChatRoom.setInitialMembers(mInitialMembersAdapter.getSelectedUsers());

                CreateGroupChatRoomUseCase.invoke(groupChatRoom).thenAccept(result -> {
                    if (result.isSuccessful()) {
                        FRAGMENT_PARENT.dismiss();
                    } else {
                        Toast.makeText(requireContext(), "Failed to create group chat room", Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });
    }

    private void initInitialMembersRecyclerView(@NonNull View iView) {
        mInitialMembersRecyclerView = iView.findViewById(R.id.recyclerview_create_group_chat_initial_members);
        mLinearLayoutManager = new LinearLayoutManager(requireContext());
        mInitialMembersRecyclerView.setLayoutManager(mLinearLayoutManager);

        mInitialMembersAdapter = new InitialMembersAdapter(GenerateQueryForAllUsersUseCase.invoke(
                CurrentlyLoggedUser.getCurrentlyLoggedUser().getUsername()),
                getViewLifecycleOwner()
        );
        mInitialMembersRecyclerView.setAdapter(mInitialMembersAdapter);
    }
}