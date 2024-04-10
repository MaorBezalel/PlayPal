package com.hit.playpal.profile.data.datasources;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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
                                // Handle the case where the document/sub collection does not exist
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

    public Task<DocumentReference> sendFriendRequest(String iReceiverUid, String iSenderUid, String iSenderDisplayName, String iSenderProfileImage) {
        Map<String, Object> data = new HashMap<>();
        data.put("type", "FRIEND_REQUEST");
        data.put("happened_at", Date.from(new Date().toInstant()));
        data.put("sender_uid", iSenderUid);
        data.put("sender_display_name", iSenderDisplayName);
        data.put("sender_profile_image", iSenderProfileImage);

        return DB.collection("users").document(iReceiverUid).collection("notifications").add(data);
    }

    public Task<Void> deleteRelationshipDocument(String iUid, String otherUserUid) {
        return DB.collection("users").document(iUid).collection("relationships")
                .whereEqualTo("other_user.uid", otherUserUid).get()
                .continueWithTask(new Continuation<QuerySnapshot, Task<Void>>() {
                    @Override
                    public Task<Void> then(@NonNull Task<QuerySnapshot> task) throws Exception {
                        if (task.isSuccessful()) {
                            List<Task<Void>> deleteTasks = new ArrayList<>();
                            for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                                deleteTasks.add(document.getReference().delete());
                            }
                            return Tasks.whenAll(deleteTasks);
                        } else {
                            Log.d("Firestore", "Error getting documents: ", task.getException());
                            throw task.getException();
                        }
                    }
                });
    }




}


