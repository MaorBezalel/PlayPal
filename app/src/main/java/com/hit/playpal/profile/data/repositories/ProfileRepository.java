package com.hit.playpal.profile.data.repositories;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hit.playpal.entities.chats.o2o.OneToOneChatRoom;
import com.hit.playpal.entities.relationships.OneToOneChatRelationship;
import com.hit.playpal.profile.data.datasources.ProfileFirebaseFirestoreDataSource;
import com.hit.playpal.profile.domain.repositories.IProfileRepository;

import java.util.HashMap;
import java.util.Map;

public class ProfileRepository implements IProfileRepository {
    private final ProfileFirebaseFirestoreDataSource DB = new ProfileFirebaseFirestoreDataSource();


    @Override
    public Task<DocumentSnapshot> getUserByUid(String iUid) {
        return DB.getUserByUid(iUid);
    }


    @Override
    public Task<String> getStatus(String iUid, String iOtherUserUid) {
        return DB.getStatus(iUid, iOtherUserUid);
    }

    @Override
    public Task<Void> addPendingFriend(String iUid, Map<String, Object> otherUserData) {
        Map<String, Object> data = new HashMap<>();
        data.put("other_user", otherUserData);
        data.put("status", "pending");

        return DB.addPendingFriend(iUid, data).continueWith(task -> null);
    }

    @Override
    public Task<Void> removeFriend(String iUid, String otherUserUid) {
        return DB.deleteRelationshipDocument(iUid, otherUserUid);
    }

    @Override
    public Task<Void> sendFriendRequest(String iReceiverId, String iSenderUid, String iSenderDisplayName, String iSenderProfileImage) {
        return DB.sendFriendRequest( iReceiverId,  iSenderUid,  iSenderDisplayName,  iSenderProfileImage).continueWith(task -> null);
    }


    @Override
    public Task<QuerySnapshot> tryToGetOneToOneChatRelationship(String iUid1, String iUid2) {
        return DB.tryToGetOneToOneChatRelationship(iUid1, iUid2);
    }

    @Override
    public Task<DocumentSnapshot> getTheExistingOneToOneChatRoom(String iChatRoomId) {
        return DB.getTheExistingOneToOneChatRoom(iChatRoomId);
    }

    @Override
    public Task<DocumentReference> createAndGetNewOneToOneChatRoom(OneToOneChatRoom iOneToOneChatRoom) {
        return DB.createAndGetNewOneToOneChatRoom(iOneToOneChatRoom);
    }

    @Override
    public Task<Void> createNewOneToOneChatRelationship(OneToOneChatRelationship iOneToOneChatRelationship) {
        return DB.createNewOneToOneChatRelationship(iOneToOneChatRelationship);
    }

}

