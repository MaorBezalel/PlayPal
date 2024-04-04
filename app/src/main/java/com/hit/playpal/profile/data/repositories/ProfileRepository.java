package com.hit.playpal.profile.data.repositories;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hit.playpal.profile.data.datasources.ProfileFirebaseFirestoreDataSource;
import com.hit.playpal.profile.domain.repositories.IProfileRepository;

import java.util.List;

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
    public Task<QuerySnapshot> getRoomsByParticipantUid(String uid, DocumentSnapshot lastVisible, int limit) {
        return DB.getRoomsByParticipantUid(uid, lastVisible, limit);
    }

    @Override
    public Task<QuerySnapshot> getUserFriendsByDisplayName(String iUid, DocumentSnapshot lastVisible, int limit) {
        return DB.getUserFriends(iUid, lastVisible, limit);
    }


}

