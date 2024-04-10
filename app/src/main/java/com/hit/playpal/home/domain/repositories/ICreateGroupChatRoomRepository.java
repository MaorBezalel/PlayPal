package com.hit.playpal.home.domain.repositories;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.hit.playpal.entities.chats.ChatRoom;
import com.hit.playpal.entities.chats.GroupChatRoom;
import com.hit.playpal.entities.chats.GroupProfile;

public interface ICreateGroupChatRoomRepository {
    Query getAllGamesQuery();
    Query getAllUsersQuery(String iUsername);
    Task<DocumentReference> createGroupChatRoom(GroupChatRoom iGroupChatRoom);
    Task<Uri> uploadGroupChatRoomProfileImage(String iChatRoomId, Uri iImageUri);
    Task<Void> storeGroupChatRoomProfileImage(String iChatRoomId, String iImageUri);
    Task<Void> createGroupProfile(String iChatRoomId, GroupProfile iGroupProfile);
}
