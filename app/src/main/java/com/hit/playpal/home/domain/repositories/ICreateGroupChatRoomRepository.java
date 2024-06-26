package com.hit.playpal.home.domain.repositories;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.Query;
import com.hit.playpal.entities.chats.group.GroupChatRoom;
import com.hit.playpal.entities.chats.group.GroupProfile;

public interface ICreateGroupChatRoomRepository {
    Query getAllGamesQuery();
    Query getAllUsersQuery(String iUsername);
    Task<DocumentReference> createGroupChatRoom(GroupChatRoom iGroupChatRoom);
    Task<Uri> uploadGroupChatRoomProfileImage(String iChatRoomId, Uri iImageUri);
    Task<Void> storeGroupChatRoomProfileImage(String iChatRoomId, String iImageUri);
    Task<Void> createGroupProfile(String iChatRoomId, GroupProfile iGroupProfile);
}
