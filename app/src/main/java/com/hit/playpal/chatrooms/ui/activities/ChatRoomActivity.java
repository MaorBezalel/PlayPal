package com.hit.playpal.chatrooms.ui.activities;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.hit.playpal.R;
import com.hit.playpal.chatrooms.ui.enums.ChatRoomLocation;
import com.hit.playpal.chatrooms.ui.fragments.ChatRoomBodyFragment;
import com.hit.playpal.chatrooms.ui.fragments.ChatRoomProfileFragment;
import com.hit.playpal.chatrooms.ui.viewmodels.ChatRoomViewModel;
import com.hit.playpal.entities.chats.ChatRoom;
import com.hit.playpal.entities.chats.group.GroupChatRoom;
import com.hit.playpal.entities.users.User;
import com.hit.playpal.utils.CurrentlyLoggedUser;
import com.hit.playpal.utils.Out;

public class ChatRoomActivity extends AppCompatActivity {

    private static final String TAG = "ChatRoomActivity";

    private static final String ARG_CHAT_ROOM = "chatRoom";
    private static final String ARG_USER = "user";
    private static final String ARG_CHAT_ROOM_LOCATION = "chatRoomLocation";

    private ChatRoomViewModel mChatRoomViewModel;

    @Override
    protected void onCreate(Bundle iSavedInstanceState) {
        super.onCreate(iSavedInstanceState);
        defaultOnCreate();
        initViewModels();
        loadTheInitialFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //mChatRoomViewModel.removeNewMessageListener();
    }

    protected void defaultOnCreate() {
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_chat_room);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initViewModels() {
        Out<User> user = Out.of(User.class);
        Out<ChatRoom> chatRoom = Out.of(ChatRoom.class);
        Out<ChatRoomLocation> chatRoomLocation = Out.of(ChatRoomLocation.class);

        getDataFromIntent(user, chatRoom, chatRoomLocation);
        ChatRoomViewModel.Factory factory = new ChatRoomViewModel.Factory(user.get(), chatRoom.get(), chatRoomLocation.get());
        mChatRoomViewModel = new ViewModelProvider(this, factory).get(ChatRoomViewModel.class);
    }

    private void getDataFromIntent(@NonNull Out<User> oUser, @NonNull Out<ChatRoom> oChatRoom, @NonNull Out<ChatRoomLocation> oChatRoomLocation) {
        Intent intent = getIntent();
        User parcelableUser = CurrentlyLoggedUser.get();
        ChatRoom parcelableChatRoom = intent.getParcelableExtra(ARG_CHAT_ROOM);
        ChatRoomLocation serializedChatRoomLocation = (ChatRoomLocation) intent.getSerializableExtra(ARG_CHAT_ROOM_LOCATION);

        if (parcelableUser == null || parcelableChatRoom == null || serializedChatRoomLocation == null) {
            throw new IllegalArgumentException(TAG + "getDataFromIntent: User, ChatRoom or ChatRoomLocation is null");
        }

        oUser.set(parcelableUser);
        oChatRoom.set(parcelableChatRoom);
        oChatRoomLocation.set(serializedChatRoomLocation);
    }

    private void loadTheInitialFragment() {
        switch (mChatRoomViewModel.getInitialChatRoomLocation()) {
            case CHAT_BODY:
                loadChatRoomBodyFragment();
                break;
            case GROUP_CHAT_PROFILE:
                loadChatRoomProfileFragment();
                break;
            default:
                throw new IllegalStateException(TAG + "loadTheMainFragment: ChatRoomLocation is not valid");
        }
    }

    private void loadChatRoomBodyFragment() {
        User user = mChatRoomViewModel.getUser();
        ChatRoom chatRoom = mChatRoomViewModel.getChatRoomLiveData().getValue();

        if (user == null || chatRoom == null) {
            throw new IllegalStateException(TAG + "loadChatRoomBodyFragment: User or ChatRoom is null");
        } else if (!chatRoom.getMembersUid().contains(user.getUid())) {
            throw new IllegalStateException(
                    TAG
                    + "loadChatRoomBodyFragment: User is not a member of the chat room, therefore shouldn't have been able to get access to this fragment"
            );
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main, new ChatRoomBodyFragment())
                    .commit();
        }
    }

    private void loadChatRoomProfileFragment() {
        ChatRoom chatRoom = mChatRoomViewModel.getChatRoomLiveData().getValue();

        if (chatRoom == null || !(chatRoom instanceof GroupChatRoom)) {
            throw new IllegalStateException(TAG + "loadChatRoomProfileFragment: ChatRoom is null or is not a GroupChatRoom");
        } else {
            getSupportFragmentManager()
                    .beginTransaction()
                    .replace(R.id.main, new ChatRoomProfileFragment())
                    .commit();
        }
    }
}