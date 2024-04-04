package com.hit.playpal.profile.data.datasources;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

public class ProfileFirebaseFirestoreDataSource {

    private final FirebaseFirestore DB = FirebaseFirestore.getInstance();

    public Task<DocumentSnapshot> getUserByUid(String iUid) {
        if (iUid == null) {
            throw new IllegalArgumentException("UID must not be null");
        }
        return DB.collection("users").document(iUid).get();
    }

    public Task<DocumentSnapshot> getUserPrivateByUid(String iUid) {
        return DB.collection("users").document(iUid).collection("private").document("data").get();
    }

    public Task<QuerySnapshot> getUserFavoriteGames(String iUid, DocumentSnapshot lastVisible, int limit) {
        Query query = DB.collection("fav_games")
                .whereEqualTo("user.uid", iUid)
                .orderBy("game_name")
                .startAfter(lastVisible)
                .limit(limit);
        return query.get();
    }

    public Task<QuerySnapshot> getUserFriends(String iUid, DocumentSnapshot lastVisible, int limit) {
        Query query = DB.collection("users").document(iUid).collection("relationships")
                .whereEqualTo("status", "friends");

        if (lastVisible != null) {
            query = query.startAfter(lastVisible);
        }
        query = query.limit(limit);
        return query.get();
    }

    public Task<QuerySnapshot> getRoomsByParticipantUid(String uid, DocumentSnapshot lastVisible, int limit) {
        Query query;
        if (lastVisible == null) {
            // This is the first page
            query = DB.collection("rooms").whereEqualTo("participants." + uid, true).limit(limit);
        } else {
            // This is a subsequent page
            query = DB.collection("rooms").whereEqualTo("participants." + uid, true).startAfter(lastVisible).limit(limit);
        }
        return query.get();
    }





}


