package com.hit.playpal.chatrooms.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.imageview.ShapeableImageView;
import com.hit.playpal.R;
import com.hit.playpal.chatrooms.ui.enums.ChatRoomLocation;
import com.hit.playpal.chatrooms.ui.viewmodels.ChatRoomViewModel;
import com.hit.playpal.entities.chats.GroupChatRoom;
import com.hit.playpal.entities.users.User;
import com.hit.playpal.game.ui.activities.GameActivity;
import com.hit.playpal.utils.CurrentlyLoggedUser;
import com.squareup.picasso.Picasso;


public class ChatRoomProfileFragment extends Fragment {
    private static final String TAG = "ChatRoomProfileFragment";

    private static final String ARG_CHAT_ROOM_ID = "chatRoomId";
    private static final String ARG_GAME_ID = "gameId";

    private ChatRoomViewModel mChatRoomViewModel;
    private GroupChatRoom mGroupChatRoom;
    private User mCurrentUser;
    private ChatRoomLocation mInitialChatRoomLocation;

    private ImageButton mChatRoomBackButton;
    private ShapeableImageView mChatRoomImageImageView;
    private TextView mChatRoomNameTextView;
    private TextView mChatRoomDescriptionTextView;
    private MaterialCardView mChatRoomGameCardView;
    private MaterialButton mChatRoomMembersButton;
    private MaterialButton mChatRoomJoinButton;
    private ProgressBar mChatRoomJoinProgressBar;

    @Override
    public View onCreateView(@NonNull LayoutInflater iInflater, ViewGroup iContainer, Bundle iSavedInstanceState) {
        return iInflater.inflate(R.layout.fragment_chat_room_profile, iContainer, false);
    }

    @Override
    public void onViewCreated(@NonNull View iView, Bundle iSavedInstanceState) {
        super.onViewCreated(iView, iSavedInstanceState);

        getViewModelFromActivity();
        initChatRoomBackButton(iView);
        initChatRoomImageImageView(iView);
        initChatRoomNameTextView(iView);
        initChatRoomDescriptionTextView(iView);
        initChatRoomGameCardView(iView);
        initChatRoomMembersButton(iView);
        initChatRoomJoinButtonAndProgressBar(iView);
        // TODO: Implement this method
    }

    private void getViewModelFromActivity() {
        mChatRoomViewModel = new ViewModelProvider(requireActivity()).get(ChatRoomViewModel.class);
        mCurrentUser = CurrentlyLoggedUser.getCurrentlyLoggedUser();
        mGroupChatRoom = (GroupChatRoom) mChatRoomViewModel.getChatRoomLiveData().getValue();
        mInitialChatRoomLocation = mChatRoomViewModel.getInitialChatRoomLocation();
    }

    private void initChatRoomBackButton(@NonNull View iView) {
        mChatRoomBackButton = iView.findViewById(R.id.imagebutton_chat_room_back);

        if (mInitialChatRoomLocation == ChatRoomLocation.GROUP_CHAT_PROFILE) {
            mChatRoomBackButton.setOnClickListener(v -> requireActivity().finish());
        } else {
            mChatRoomBackButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());
        }
    }

    private void initChatRoomImageImageView(@NonNull View iView) {
        mChatRoomImageImageView = iView.findViewById(R.id.imageview_chat_room_profile_chat_room_image);

        if (mGroupChatRoom.getProfilePicture() == null || mGroupChatRoom.getProfilePicture().isEmpty()) {
            mChatRoomImageImageView.setImageResource(R.drawable.ic_home_nav_search_groupchats);
        } else {
            Picasso.get().load(mGroupChatRoom.getProfilePicture()).into(mChatRoomImageImageView);
        }
    }

    private void initChatRoomNameTextView(@NonNull View iView) {
        mChatRoomNameTextView = iView.findViewById(R.id.textview_chat_room_profile_chat_room_name);
        mChatRoomNameTextView.setText(mGroupChatRoom.getName());
    }

    private void initChatRoomDescriptionTextView(@NonNull View iView) {
        mChatRoomDescriptionTextView = iView.findViewById(R.id.textview_chat_room_profile_chat_room_description);

        if (mGroupChatRoom.getDescription() != null && !mGroupChatRoom.getDescription().isEmpty()) {
            mChatRoomDescriptionTextView.setText(mGroupChatRoom.getDescription());
        } else {
            mChatRoomDescriptionTextView.setText("");
        }
    }

    private void initChatRoomGameCardView(@NonNull View iView) {
        mChatRoomGameCardView = iView.findViewById(R.id.cardview_chat_room_profile_chat_room_game);

        TextView gameTitle = iView.findViewById(R.id.textview_create_group_chat_game_title);
        gameTitle.setText(mGroupChatRoom.getGame().getName());

        ImageView gameBackgroundImage = iView.findViewById(R.id.imageview_create_group_chat_game_image);
        if (mGroupChatRoom.getGame().getBackgroundImage() == null || mGroupChatRoom.getGame().getBackgroundImage().isEmpty()) {
            gameBackgroundImage.setImageResource(R.drawable.ic_home_nav_search_groupchats);
        } else {
            Picasso.get().load(mGroupChatRoom.getGame().getBackgroundImage()).into(gameBackgroundImage);
        }

        mChatRoomGameCardView.setOnClickListener(v -> {
            Intent intent = new Intent(getContext(), GameActivity.class);

            intent.putExtra(ARG_GAME_ID, mGroupChatRoom.getGame().getGameId());
            startActivity(intent);
        });
    }

    private void initChatRoomMembersButton(@NonNull View iView) {
        mChatRoomMembersButton = iView.findViewById(R.id.button_chat_room_profile_chat_room_members);

        mChatRoomMembersButton.setOnClickListener(v -> {
            ChatInfoMemberSearchFragment chatInfoMemberSearchFragment = new ChatInfoMemberSearchFragment();

            Bundle bundle = new Bundle();
            bundle.putString(ARG_CHAT_ROOM_ID, mGroupChatRoom.getId());
            chatInfoMemberSearchFragment.setArguments(bundle);

            getParentFragmentManager().beginTransaction()
                    .replace(R.id.main, chatInfoMemberSearchFragment)
                    .addToBackStack(null)
                    .commit();
        });
    }

    private void initChatRoomJoinButtonAndProgressBar(@NonNull View iView) {
        mChatRoomJoinButton = iView.findViewById(R.id.button_chat_room_profile_chat_room_join);
        mChatRoomJoinProgressBar = iView.findViewById(R.id.progressbar_chat_room_profile_chat_room_join);

        // check if user is already a member of the group chat room
        if (mGroupChatRoom.getMembersUid().contains(mCurrentUser.getUid())) {
            mChatRoomJoinButton.setEnabled(false);
            mChatRoomJoinButton.setText("Already a member");
        }

        // Listeners
        mChatRoomJoinButton.setOnClickListener(v -> {
            mChatRoomJoinProgressBar.setVisibility(View.VISIBLE);
            mChatRoomJoinButton.setVisibility(View.INVISIBLE);
            mChatRoomViewModel.addThisUserToGroupChatRoom();
        });

        mChatRoomViewModel.onJoiningGroupChatRoomSuccess().observe(getViewLifecycleOwner(), message -> {
            mChatRoomJoinProgressBar.setVisibility(View.INVISIBLE);
            mChatRoomJoinButton.setVisibility(View.VISIBLE);
            mChatRoomJoinButton.setEnabled(false);
            mChatRoomJoinButton.setText("Already a member");
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });

        mChatRoomViewModel.onJoiningGroupChatRoomError().observe(getViewLifecycleOwner(), message -> {
            mChatRoomJoinProgressBar.setVisibility(View.INVISIBLE);
            mChatRoomJoinButton.setVisibility(View.VISIBLE);
            Toast.makeText(getContext(), message, Toast.LENGTH_SHORT).show();
        });
    }
}