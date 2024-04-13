package com.hit.playpal.home.domain.repositories;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Query;
import com.hit.playpal.entities.chats.enums.ChatRoomType;

public interface IChatsRepository {
    Query generateQueryForAllChatRooms(@NonNull String iUserId);
    Query generateQueryForSpecificChatRooms(@NonNull String iUserId, @NonNull ChatRoomType iChatRoomType);
}
