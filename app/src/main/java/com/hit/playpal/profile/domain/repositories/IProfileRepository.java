package com.hit.playpal.profile.domain.repositories;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;

import java.util.Map;

public interface IProfileRepository {
    Task<DocumentSnapshot> getUserByUid(String iUid);
    Task<String> getStatus(String iUid, String iOtherUserUid);
    Task<Void> addPendingFriend(String iUid, Map<String, Object> otherUserData);
    Task<Void> removeFriend(String iUid, String otherUserUid);
    Task<Void> sendFriendRequest(String iReceiverId, String iSenderUid, String iSenderDisplayName, String iSenderProfileImage);
}


