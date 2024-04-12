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
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.google.android.material.imageview.ShapeableImageView;
import com.hit.playpal.R;
import com.hit.playpal.chatrooms.domain.listeners.INewMessageRegistrationListener;
import com.hit.playpal.chatrooms.ui.adapters.MessageAdapter;
import com.hit.playpal.chatrooms.ui.viewmodels.ChatRoomViewModel;
import com.hit.playpal.entities.chats.ChatRoom;
import com.hit.playpal.entities.chats.GroupChatRoom;
import com.hit.playpal.entities.chats.Message;
import com.hit.playpal.entities.chats.OneToOneChatRoom;
import com.hit.playpal.entities.users.User;
import com.hit.playpal.utils.CurrentlyLoggedUser;
import com.hit.playpal.utils.EndlessRecyclerViewScrollListener;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

public class ChatRoomBodyFragment extends Fragment {
    private static final String TAG = "ChatRoomBodyFragment";
    private final int PAGE_SIZE = 10;

    private ChatRoomViewModel mChatRoomViewModel;

    private RelativeLayout mChatRoomNameAndImageContainer;
    private TextView mChatRoomNameTextView;
    private ShapeableImageView mChatRoomImageImageView;
    private ImageButton mChatRoomBackButton;
    private RecyclerView mChatRoomMessagesRecyclerView;
    private EditText mChatRoomMessageInputEditText;
    private ImageButton mChatRoomSendMessageButton;

    private EndlessRecyclerViewScrollListener mEndlessRecyclerViewScrollListener;
    private int mInitializeListenerCounter = 0;


    private MessageAdapter mMessageAdapter;

    private INewMessageRegistrationListener mNewMessageRegistrationListener;


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
    }

    private void initViews(@NonNull View iView) {
        initChatRoomNameAndImageContainer(iView);
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
        observePaginatedMessages();
        observeWhenThisUserMessageSent();
        observeWhenNewMessageReceived();
    }

    private void observePaginatedMessages() {
        mChatRoomViewModel.getFetchMessagesSuccess().observe(getViewLifecycleOwner(), message -> {
            List<Message> messages = mChatRoomViewModel.getNewMessages().getValue();
            mMessageAdapter.addNewMessages(messages);
        });

        mChatRoomViewModel.getFetchMessagesError().observe(getViewLifecycleOwner(), message -> {
            mChatRoomMessagesRecyclerView.removeOnScrollListener(mEndlessRecyclerViewScrollListener);
        });
    }

    private void observeWhenThisUserMessageSent() {
        mChatRoomViewModel.onMessageSent().observe(getViewLifecycleOwner(), message -> {
            mChatRoomMessageInputEditText.setText("");
            mChatRoomMessagesRecyclerView.scrollToPosition(0);
            mMessageAdapter.addNewMessage(message);
        });
    }

    private void observeWhenNewMessageReceived() {
        mChatRoomViewModel.getChatRoomLiveData().observe(getViewLifecycleOwner(), chatRoom -> {
            if(mInitializeListenerCounter < 2) {
                mInitializeListenerCounter++;
                return;
            }

            if (!chatRoom.getLastMessage().getSender().getUid().equals(CurrentlyLoggedUser.getCurrentlyLoggedUser().getUid())) {
                  mMessageAdapter.addNewMessage(chatRoom.getLastMessage());
            }
        });
    }

    private void initChatRoomNameAndImageContainer(@NonNull View iView) {
        if (mChatRoomViewModel.getChatRoomLiveData().getValue() instanceof GroupChatRoom) {
            mChatRoomNameAndImageContainer = iView.findViewById(R.id.relativelayout_chat_room_name_and_profile_picture);
            mChatRoomNameAndImageContainer.setOnClickListener(v -> {
                ChatRoomProfileFragment chatRoomProfileFragment = new ChatRoomProfileFragment();

                getParentFragmentManager().beginTransaction()
                        .replace(R.id.main, chatRoomProfileFragment)
                        .addToBackStack(null)
                        .commit();
            });
        }
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

        if (imageUrl == null || imageUrl.isEmpty()) {
            mChatRoomImageImageView.setImageResource(R.drawable.ic_home_nav_search_groupchats);
        } else {
            Picasso.get()
                    .load(imageUrl)
                    .into(mChatRoomImageImageView);
        }
    }

    private void initChatRoomBackButton(@NonNull View iView) {
        mChatRoomBackButton = iView.findViewById(R.id.imagebutton_chat_room_back);
        mChatRoomBackButton.setOnClickListener(v -> requireActivity().finish());
    }

    private void initChatRoomMessagesRecyclerView(@NonNull View iView) {
        mChatRoomMessagesRecyclerView = iView.findViewById(R.id.recyclerview_chat_room_messages);
        mMessageAdapter = new MessageAdapter();
        LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getContext());
        linearLayoutManager.setReverseLayout(true);
        mChatRoomMessagesRecyclerView.setAdapter(mMessageAdapter);
        mChatRoomMessagesRecyclerView.setLayoutManager(linearLayoutManager);

        mEndlessRecyclerViewScrollListener =new EndlessRecyclerViewScrollListener(linearLayoutManager) {
            @Override
            public void onLoadMore(int page, int totalItemsCount, RecyclerView view) {
                mChatRoomViewModel.fetchMessages(PAGE_SIZE);
            }
        };
        mChatRoomMessagesRecyclerView.addOnScrollListener(mEndlessRecyclerViewScrollListener);

        mChatRoomViewModel.fetchMessages(PAGE_SIZE);
        mNewMessageRegistrationListener = mChatRoomViewModel.listenToLatestMessages();
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

    @Override
    public void onDestroy()
    {
        mNewMessageRegistrationListener.remove();
        super.onDestroy();
    }
}