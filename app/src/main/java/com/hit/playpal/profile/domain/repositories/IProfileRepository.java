package com.hit.playpal.profile.domain.repositories;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hit.playpal.entities.chats.GroupChatRoom;
import com.hit.playpal.entities.games.Game;
import com.hit.playpal.entities.users.User;

import java.util.Date;
import java.util.List;
import java.util.Map;

public interface IProfileRepository {
    Task<DocumentSnapshot> getUserByUid(String iUid);
    Task<QuerySnapshot> getUserFriendsByDisplayName(String iUid, DocumentSnapshot lastVisible, int limit);
    Task<String> getStatus(String iUid, String iOtherUserUid);
    Task<Void> addPendingFriend(String iUid, Map<String, Object> otherUserData);
    Task<Void> removeFriend(String iUid, String otherUserUid);
    Task<Void> sendFriendRequest(String iReceiverId, String iSenderUid, String iSenderDisplayName, String iSenderProfileImage);
}


