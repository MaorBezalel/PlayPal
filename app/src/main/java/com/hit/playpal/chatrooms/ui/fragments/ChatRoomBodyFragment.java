package com.hit.playpal.chatrooms.ui.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.hit.playpal.R;
import com.hit.playpal.chatrooms.ui.adapters.MessageAdapter;
import com.hit.playpal.chatrooms.ui.adapters.MessageFirestoreAdapter;
import com.hit.playpal.chatrooms.ui.viewmodels.ChatRoomViewModel;
import com.hit.playpal.entities.chats.ChatRoom;
import com.hit.playpal.entities.chats.GroupChatRoom;
import com.hit.playpal.entities.chats.Message;
import com.hit.playpal.entities.chats.OneToOneChatRoom;
import com.hit.playpal.entities.users.User;
import com.squareup.picasso.Picasso;

import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicInteger;

public class ChatRoomBodyFragment extends Fragment {
    private static final String TAG = "ChatRoomBodyFragment";
    private final int PAGE_SIZE = 20;

    private ChatRoomViewModel mChatRoomViewModel;

    private TextView mChatRoomNameTextView;
    private ImageView mChatRoomImageImageView;
    private ImageButton mChatRoomBackButton;
    private RecyclerView mChatRoomMessagesRecyclerView;
    private EditText mChatRoomMessageInputEditText;
    private ImageButton mChatRoomSendMessageButton;

    private MessageAdapter mMessageAdapter;
    private MessageFirestoreAdapter mMessageFirestoreAdapter;

    private AtomicInteger mCount = new AtomicInteger(0);

    @Override
    public View onCreateView(@NonNull LayoutInflater iInflater, ViewGroup iContainer, Bundle iSavedInstanceState) {
        return iInflater.inflate(R.layout.fragment_chat_room_body, iContainer, false);
    }

    @Override
    public void onViewCreated(@NonNull View iView, Bundle iSavedInstanceState) {
        super.onViewCreated(iView, iSavedInstanceState);
        getViewModelFromActivity();
        initViews(iView);
        observeViewModel();
        Log.d(TAG, "onViewCreated: RecyclerView count: " + mChatRoomMessagesRecyclerView.getChildCount() + ", Adapter count: " + mMessageAdapter.getItemCount());
    }

    private void initViews(@NonNull View iView) {
        initChatRoomNameView(iView);
        initChatRoomProfilePictureView(iView);
        initChatRoomBackButton(iView);
        initChatRoomMessagesRecyclerView(iView);
        initChatRoomMessageInput(iView);
        initChatRoomSendMessageButton(iView);
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

    private void observeViewModel() {
        //observePaginatedMessages();
        observeWhenMessageSent();
        observeWhenNewMessageReceived();
    }

    private void observePaginatedMessages() {
//        mChatRoomViewModel.fetchMessages(PAGE_SIZE).observe(getViewLifecycleOwner(), messages -> {
//            Log.d(TAG, "observePaginatedMessages: has new data: " + messages);
//            mMessageAdapter.submitData(getViewLifecycleOwner().getLifecycle(), messages);
//        });

        mChatRoomViewModel.fetchMessages2(PAGE_SIZE).observe(getViewLifecycleOwner(), messages -> {
            Log.d(TAG, "observePaginatedMessages: has new data: " + messages);
            mMessageAdapter.submitData(getViewLifecycleOwner().getLifecycle(), messages);
        });
    }

    private void observeWhenMessageSent() {
        mChatRoomViewModel.onMessageSent().observe(getViewLifecycleOwner(), aVoid -> {
            mChatRoomMessageInputEditText.setText(""); // clear the message input
        });
    }

    private void observeWhenNewMessageReceived() {
        mChatRoomViewModel.getChatRoomLiveData().observe(getViewLifecycleOwner(), chatRoom -> {
            Toast.makeText(getContext(), "New message received", Toast.LENGTH_SHORT).show();

            if (mCount.get() != 2) {
                mCount.set(mCount.get() + 1);
            } else {
                mMessageFirestoreAdapter.refresh();
                mChatRoomMessagesRecyclerView.scrollToPosition(0);
            }
        });
    }

    private void initChatRoomNameView(@NonNull View iView) {
        mChatRoomNameTextView = iView.findViewById(R.id.textview_chat_room_name);
        ChatRoom chatRoom = mChatRoomViewModel.getChatRoomLiveData().getValue();

        if (chatRoom instanceof GroupChatRoom) {
            String groupName = ((GroupChatRoom) chatRoom).getName();
            mChatRoomNameTextView.setText(groupName);
        } else {
            String thisUserUid = mChatRoomViewModel.getUser().getUid();
            String otherUserDisplayName = ((OneToOneChatRoom) chatRoom).getOtherUserDisplayName(thisUserUid);
            mChatRoomNameTextView.setText(otherUserDisplayName);
        }
    }

    private void initChatRoomProfilePictureView(@NonNull View iView) {
        mChatRoomImageImageView = iView.findViewById(R.id.imageview_chat_room_profile_picture);
        ChatRoom chatRoom = mChatRoomViewModel.getChatRoomLiveData().getValue();
        String imageUrl;

        if (chatRoom instanceof GroupChatRoom) {
            imageUrl = ((GroupChatRoom) chatRoom).getProfilePicture();
        } else {
            String thisUserUid = mChatRoomViewModel.getUser().getUid();
            imageUrl = ((OneToOneChatRoom) chatRoom).getOtherUserProfilePicture(thisUserUid);
        }

        Picasso.get()
                .load(imageUrl)
                .into(mChatRoomImageImageView);
    }

    private void initChatRoomBackButton(@NonNull View iView) {
        mChatRoomBackButton = iView.findViewById(R.id.imagebutton_chat_room_back);
        mChatRoomBackButton.setOnClickListener(v -> {
            getParentFragmentManager().popBackStack();
        });
    }

    private void initChatRoomMessagesRecyclerView(@NonNull View iView) {
        mChatRoomMessagesRecyclerView = iView.findViewById(R.id.recyclerview_chat_room_messages);

        // Set MessageFirestoreAdapter
        mMessageAdapter = new MessageAdapter();
        mMessageFirestoreAdapter = new MessageFirestoreAdapter(mChatRoomViewModel.getChatRoomLiveData().getValue().getId(), this);
        mChatRoomMessagesRecyclerView.setAdapter(mMessageFirestoreAdapter);

        // Set LinearLayoutManager
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        mChatRoomMessagesRecyclerView.setLayoutManager(linearLayoutManager);

        // Begin at the bottom of the RecyclerView
        mChatRoomMessagesRecyclerView.scrollToPosition(mMessageFirestoreAdapter.getItemCount() - 1);
    }

    private void initChatRoomMessageInput(@NonNull View iView) {
        mChatRoomMessageInputEditText = iView.findViewById(R.id.edittext_chat_room_message_input);
    }

    private void initChatRoomSendMessageButton(@NonNull View iView) {
        mChatRoomSendMessageButton = iView.findViewById(R.id.imagebutton_chat_room_send_message);

        mChatRoomSendMessageButton.setOnClickListener(v -> {
            String messageContent = mChatRoomMessageInputEditText.getText().toString().trim();
            if (!messageContent.isEmpty()) {
                String chatRoomId = mChatRoomViewModel.getChatRoomLiveData().getValue().getId();
                User sender = mChatRoomViewModel.getUser();
                Message message = new Message(chatRoomId, sender, messageContent);

                mChatRoomViewModel.sendMessage(message);
            }
        });
    }
}