package com.hit.playpal.home.domain.repositories;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.hit.playpal.entities.chats.ChatRoom;
import com.hit.playpal.entities.chats.GroupChatRoom;
import com.hit.playpal.entities.chats.OneToOneChatRoom;
import com.hit.playpal.entities.chats.enums.ChatRoomType;
import com.hit.playpal.entities.users.Notification;

import java.util.List;

public interface IChatsRepository {
    Query generateQueryForAllChatRooms(@NonNull String iUserId);
    Query generateQueryForSpecificChatRooms(@NonNull String iUserId, @NonNull ChatRoomType iChatRoomType);
}
