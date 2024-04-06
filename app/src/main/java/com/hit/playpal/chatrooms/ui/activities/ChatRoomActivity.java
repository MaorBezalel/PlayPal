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
import com.hit.playpal.chatrooms.ui.fragments.ChatRoomBodyFragment;
import com.hit.playpal.chatrooms.ui.viewmodels.ChatRoomViewModel;
import com.hit.playpal.entities.chats.ChatRoom;
import com.hit.playpal.entities.users.User;
import com.hit.playpal.utils.Out;

public class ChatRoomActivity extends AppCompatActivity {

    private static final String TAG = "ChatRoomActivity";
    private ChatRoomViewModel mChatRoomViewModel;

    @Override
    protected void onCreate(Bundle iSavedInstanceState) {
        super.onCreate(iSavedInstanceState);
        defaultOnCreate();
        initViewModels();
        loadChatRoomBodyFragment();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mChatRoomViewModel.removeNewMessageListener();
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

        getDataFromIntent(user, chatRoom);
        //new ChatRoomViewModel(user.get(), chatRoom.get());
        ChatRoomViewModel.Factory factory = new ChatRoomViewModel.Factory(user.get(), chatRoom.get());
        mChatRoomViewModel = new ViewModelProvider(this, factory).get(ChatRoomViewModel.class);
    }

    private void getDataFromIntent(@NonNull Out<User> oUser, @NonNull Out<ChatRoom> oChatRoom) {
        Intent intent = getIntent();
        User parcelableUser = intent.getParcelableExtra("user");
        ChatRoom parcelableChatRoom = intent.getParcelableExtra("chatRoom");

        if (parcelableUser == null || parcelableChatRoom == null) {
            throw new IllegalArgumentException(TAG + ": user or chatRoom is null while trying to get data from intent");
        }

        oUser.set(parcelableUser);
        oChatRoom.set(parcelableChatRoom);
    }

    private void loadChatRoomBodyFragment() {
        getSupportFragmentManager()
                .beginTransaction()
                .replace(R.id.main, new ChatRoomBodyFragment())
                .commit();
    }
}