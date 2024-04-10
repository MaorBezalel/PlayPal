package com.hit.playpal.home.data.repositories;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.hit.playpal.entities.chats.ChatRoom;
import com.hit.playpal.entities.chats.GroupChatRoom;
import com.hit.playpal.entities.chats.GroupProfile;
import com.hit.playpal.home.data.datasources.CreateGroupChatRoomFirebaseFirestoreDataSource;
import com.hit.playpal.home.data.datasources.CreateGroupChatRoomFirebaseStorageDataSource;
import com.hit.playpal.home.domain.repositories.ICreateGroupChatRoomRepository;

public class CreateGroupChatRoomRepository implements ICreateGroupChatRoomRepository {
    private final CreateGroupChatRoomFirebaseFirestoreDataSource DB = new CreateGroupChatRoomFirebaseFirestoreDataSource();
    private final CreateGroupChatRoomFirebaseStorageDataSource STORAGE = new CreateGroupChatRoomFirebaseStorageDataSource();

    @Override
    public Query getAllGamesQuery() {
        return DB.getAllGamesQuery();
    }

    @Override
    public Query getAllUsersQuery(String iUsername) {
        return DB.getAllUsersQuery(iUsername);
    }

    @Override
    public Task<DocumentReference> createGroupChatRoom(GroupChatRoom iGroupChatRoom) {
        return DB.createGroupChatRoom(iGroupChatRoom);
    }

    @Override
    public Task<Uri> uploadGroupChatRoomProfileImage(String iChatRoomId, Uri iImageUri) {
        return STORAGE.uploadGroupChatRoomProfileImage(iChatRoomId, iImageUri);
    }

    @Override
    public Task<Void> storeGroupChatRoomProfileImage(String iChatRoomId, String iImageUri) {
        return DB.storeGroupChatRoomProfileImage(iChatRoomId, iImageUri);
    }

    @Override
    public Task<Void> createGroupProfile(String iChatRoomId, GroupProfile iGroupProfile) {
        return DB.createGroupProfile(iChatRoomId, iGroupProfile);
    }

}