package com.hit.playpal.chatrooms.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.TextView;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.hit.playpal.R;
import com.hit.playpal.chatrooms.ui.viewmodels.ChatRoomViewModel;
import com.hit.playpal.entities.chats.ChatRoom;
import com.hit.playpal.entities.chats.GroupChatRoom;
import com.hit.playpal.entities.users.User;
import com.squareup.picasso.Picasso;


public class ChatRoomProfileFragment extends Fragment {
    private static final String TAG = "ChatRoomProfileFragment";

    private ChatRoomViewModel mChatRoomViewModel;
    private ImageButton mChatRoomBackButton;
    private ShapeableImageView mChatRoomImageImageView;
    private TextView mChatRoomNameTextView;
    private TextView mChatRoomDescriptionTextView;
    private MaterialCardView mChatRoomGameCardView;
    private MaterialButton mChatRoomMembersButton;
    private MaterialButton mChatRoomJoinButton;

    @Override
    public View onCreateView(@NonNull LayoutInflater iInflater, ViewGroup iContainer, Bundle iSavedInstanceState) {
        return iInflater.inflate(R.layout.fragment_chat_room_profile, iContainer, false);
    }

    private void getViewModelFromActivity() {
        mChatRoomViewModel = new ViewModelProvider(requireActivity()).get(ChatRoomViewModel.class);

        // DEBUG CHECK:
        User user = mChatRoomViewModel.getUser();
        ChatRoom chatRoom = mChatRoomViewModel.getChatRoomLiveData().getValue();

        if (user == null || chatRoom == null) {
            throw new IllegalStateException("User or ChatRoom is null");
        }
    }

    private void initViews(@NonNull View iView) {
        // TODO: Implement this method
    }

    private void initChatRoomBackButton(@NonNull View iView) {
        mChatRoomBackButton = iView.findViewById(R.id.imagebutton_chat_room_back);

        mChatRoomBackButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View iView) { // TODO: Implement this method
                getActivity().onBackPressed();
            }
        });
    }

    private void initChatRoomImageImageView(@NonNull View iView) {
        GroupChatRoom chatRoom = (GroupChatRoom) mChatRoomViewModel.getChatRoomLiveData().getValue();

        mChatRoomImageImageView = iView.findViewById(R.id.imageview_chat_room_profile_chat_room_image);

        if (chatRoom.getProfilePicture() == null || chatRoom.getProfilePicture().isEmpty()) {
            mChatRoomImageImageView.setImageResource(R.drawable.ic_home_nav_search_groupchats);
        } else {
            Picasso.get().load(chatRoom.getProfilePicture()).into(mChatRoomImageImageView);
        }
    }

    private void initChatRoomNameTextView(@NonNull View iView) {
        GroupChatRoom chatRoom = (GroupChatRoom) mChatRoomViewModel.getChatRoomLiveData().getValue();

        mChatRoomNameTextView = iView.findViewById(R.id.textview_chat_room_profile_chat_room_name);
        mChatRoomNameTextView.setText(chatRoom.getName());
    }

    private void initChatRoomDescriptionTextView(@NonNull View iView) {
        // TODO: Need to get access to the group chat room description
        mChatRoomDescriptionTextView = iView.findViewById(R.id.textview_chat_room_profile_chat_room_description);
    }

    private void mChatRoomGameCardView(@NonNull View iView) {
        // TODO: Implement this method
    }

    private void initChatRoomMembersButton(@NonNull View iView) {
        // TODO: Implement this method
    }

    private void initChatRoomJoinButton(@NonNull View iView) {
        // TODO: Implement this method
    }
}