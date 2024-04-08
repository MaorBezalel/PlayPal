package com.hit.playpal.home.domain.repositories;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.hit.playpal.entities.chats.ChatRoom;

public interface ICreateGroupChatRoomRepository {
    Query getAllGamesQuery();
    Query getAllUsersQuery(String iUsername);
    Task<DocumentReference> createGroupChatRoom(ChatRoom iChatRoom);
}
