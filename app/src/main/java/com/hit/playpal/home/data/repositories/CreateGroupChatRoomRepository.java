package com.hit.playpal.home.data.repositories;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.hit.playpal.entities.chats.ChatRoom;
import com.hit.playpal.home.data.datasources.GamesFirebaseFirestoreDataSource;
import com.hit.playpal.home.domain.repositories.ICreateGroupChatRoomRepository;

public class CreateGroupChatRoomRepository implements ICreateGroupChatRoomRepository {
    private final GamesFirebaseFirestoreDataSource DB = new GamesFirebaseFirestoreDataSource();

    @Override
    public Query getAllGamesQuery() {
        return DB.getAllGamesQuery();
    }

    @Override
    public Query getAllUsersQuery(String iUsername) {
        return DB.getAllUsersQuery(iUsername);
    }

    @Override
    public Task<DocumentReference> createGroupChatRoom(ChatRoom iChatRoom) {
        return DB.createGroupChatRoom(iChatRoom);
    }

}