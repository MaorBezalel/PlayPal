package com.hit.playpal.home.data.datasources;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.hit.playpal.entities.chats.enums.ChatRoomType;

public class ChatsFirebaseFirestoreDataSource {
    private final FirebaseFirestore DB = FirebaseFirestore.getInstance();

    public Query generateQueryForAllChatRooms(@NonNull String iUserId) {
        return DB
                .collection("chat_rooms")
                .whereArrayContains("members_uid", iUserId)
                .orderBy("last_message.sent_at", Query.Direction.DESCENDING);
    }

    public Query generateQueryForSpecificChatRooms(@NonNull String iUserId, @NonNull ChatRoomType iChatRoomType) {
        return DB
                .collection("chat_rooms")
                .whereEqualTo("type", iChatRoomType.name())
                .whereArrayContains("members_uid", iUserId)
                .orderBy("last_message.sent_at", Query.Direction.DESCENDING);
    }
}
