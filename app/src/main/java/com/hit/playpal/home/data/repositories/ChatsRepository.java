package com.hit.playpal.home.data.repositories;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.Query;
import com.hit.playpal.entities.chats.enums.ChatRoomType;
import com.hit.playpal.entities.users.Notification;
import com.hit.playpal.home.data.datasources.ChatsFirebaseFirestoreDataSource;
import com.hit.playpal.home.domain.repositories.IChatsRepository;

import java.util.List;

public class ChatsRepository implements IChatsRepository {
    private final ChatsFirebaseFirestoreDataSource DB = new ChatsFirebaseFirestoreDataSource();

    @Override
    public Query generateQueryForAllChatRooms(@NonNull String iUserId) {
        return DB.generateQueryForAllChatRooms(iUserId);
    }

    @Override
    public Query generateQueryForSpecificChatRooms(@NonNull String iUserId, @NonNull ChatRoomType iChatRoomType) {
        return DB.generateQueryForSpecificChatRooms(iUserId, iChatRoomType);
    }
}
