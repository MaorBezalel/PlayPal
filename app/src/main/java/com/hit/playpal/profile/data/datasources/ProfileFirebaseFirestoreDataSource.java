package com.hit.playpal.profile.data.datasources;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

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



    public Task<QuerySnapshot> getUserFriends(String iUid, DocumentSnapshot lastVisible, int limit) {
        Query query = DB.collection("users").document(iUid).collection("relationships")
                .whereEqualTo("status", "friends");

        if (lastVisible != null) {
            query = query.startAfter(lastVisible);
        }
        query = query.limit(limit);
        return query.get();
    }

    public Task<String> getStatus(String iUid, String iOtherUserUid) {
        return DB.collection("users").document(iUid).collection("relationships")
                .whereEqualTo("other_user.uid", iOtherUserUid).get()
                .continueWith(new Continuation<QuerySnapshot, String>() {
                    @Override
                    public String then(@NonNull Task<QuerySnapshot> task) throws Exception {
                        if (task.isSuccessful()) {
                            QuerySnapshot querySnapshot = task.getResult();
                            if (!querySnapshot.isEmpty()) {
                                DocumentSnapshot document = querySnapshot.getDocuments().get(0);
                                String status = document.getString("status");
                                return status;
                            } else {
                                // Handle the case where the document does not exist
                                return "noStatus";
                            }
                        } else {
                            // Handle the failure
                            throw Objects.requireNonNull(task.getException());
                        }
                    }
                });
    }

    public Task<DocumentReference> addPendingFriend(String iUid, Map<String, Object> data) {
        return DB.collection("users").document(iUid).collection("relationships").add(data);
    }






}


