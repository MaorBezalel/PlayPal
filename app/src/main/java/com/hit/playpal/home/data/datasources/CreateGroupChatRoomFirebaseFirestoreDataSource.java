package com.hit.playpal.home.data.datasources;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.hit.playpal.entities.chats.group.GroupChatRoom;
import com.hit.playpal.entities.chats.group.GroupProfile;

public class CreateGroupChatRoomFirebaseFirestoreDataSource {
    private final FirebaseFirestore DB = FirebaseFirestore.getInstance();

    public Query getAllGamesQuery() {
        return DB.collection("games");
    }

    public Query getAllUsersQuery(String iUsername) {
        return DB.collection("users").whereNotEqualTo("username", iUsername);
    }
    public Task<DocumentReference> createGroupChatRoom(GroupChatRoom iGroupChatRoom) {
        return DB
                .collection("chat_rooms")
                .add(iGroupChatRoom);
    }

    public Task<Void> storeGroupChatRoomProfileImage(String iChatRoomId, String iImageUri) {
        return DB.collection("chat_rooms")
                .document(iChatRoomId)
                .update("profile_picture", iImageUri);
    }

    public Task<Void> createGroupProfile(String iChatRoomId, GroupProfile iGroupProfile) {
        return DB.collection("chat_rooms")
                .document(iChatRoomId)
                .collection("group_profile")
                .document("data")
                .set(iGroupProfile);
    }
}