package com.hit.playpal.home.data.datasources;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.hit.playpal.entities.chats.ChatRoom;

public class GamesFirebaseFirestoreDataSource {
    private final FirebaseFirestore DB = FirebaseFirestore.getInstance();

    public Query getAllGamesQuery() {
        return DB.collection("games");
    }

    public Query getAllUsersQuery(String iUsername) {
        return DB.collection("users").whereNotEqualTo("username", iUsername);
    }

    public Task<DocumentReference> createGroupChatRoom(ChatRoom iChatRoom) {
        return DB.collection("chatRooms").add(iChatRoom);
    }
}