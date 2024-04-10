package com.hit.playpal.home.data.datasources;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hit.playpal.entities.users.enums.NotificationType;
import com.hit.playpal.entities.users.enums.RelationshipStatus;
import com.hit.playpal.home.domain.repositories.INotificationsRepository;

public class NotificationsFirebaseFirestoreDataSource {
    private final FirebaseFirestore DB = FirebaseFirestore.getInstance();
    public Task<QuerySnapshot> getFriendRequestsOfUser(String iUid) {
        return DB.collection("users")
                .document(iUid)
                .collection("notifications")
                .whereEqualTo("type", NotificationType.FRIEND_REQUEST)
                .get();
    }

    public Task<QuerySnapshot> getFriendRequestNotification(String iCurrentUserId, String iCurrentUserIdWhoSentRequest)
    {
        return DB.collection("users")
                .document(iCurrentUserId)
                .collection("notifications")
                .whereEqualTo("type", NotificationType.FRIEND_REQUEST)
                .whereEqualTo("sender_uid", iCurrentUserIdWhoSentRequest)
                .get();
    }

    public Task<Void> deleteNotification(String iCurrentUserId, String iNotificationId)
    {
        return DB.collection("users")
                .document(iCurrentUserId)
                .collection("notifications")
                .document(iNotificationId)
                .delete();
    }

    public Task<Void> updateRelationshipStatus(String iCurrentUserId, String iDocumentId, RelationshipStatus iNewStatus)
    {
        return DB.collection("users")
                .document(iCurrentUserId)
                .collection("relationships")
                .document(iDocumentId)
                .update("status", iNewStatus);
    }


    public Task<QuerySnapshot> getRelationshipStatus(String iCurrentUserId, String iOtherUserId)
    {
        return DB.collection("users")
                .document(iCurrentUserId)
                .collection("relationships")
                .whereEqualTo("other_user.uid", iOtherUserId)
                .get();
    }

    public Task<Void> removeRelationshipStatus(String iCurrentUserId, String iDocumentId)
    {
        return DB.collection("users")
                .document(iCurrentUserId)
                .collection("relationships")
                .document(iDocumentId)
                .delete();
    }
}
