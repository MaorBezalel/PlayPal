package com.hit.playpal.profile.data.repositories;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hit.playpal.profile.data.datasources.ProfileFirebaseFirestoreDataSource;
import com.hit.playpal.profile.domain.repositories.IProfileRepository;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ProfileRepository implements IProfileRepository {
    private final ProfileFirebaseFirestoreDataSource DB = new ProfileFirebaseFirestoreDataSource();


    @Override
    public Task<DocumentSnapshot> getUserByUid(String iUid) {
        return DB.getUserByUid(iUid);
    }

    @Override
    public Task<DocumentSnapshot> getUserPrivateByUid(String iUid) {
        return DB.getUserPrivateByUid(iUid);
    }



    @Override
    public Task<QuerySnapshot> getUserFriendsByDisplayName(String iUid, DocumentSnapshot lastVisible, int limit) {
        return DB.getUserFriends(iUid, lastVisible, limit);
    }

    @Override
    public Task<String> getStatus(String iUid, String iOtherUserUid){
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

}

