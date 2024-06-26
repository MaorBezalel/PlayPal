package com.hit.playpal.home.domain.repositories;

import com.google.android.gms.tasks.Task;
import com.hit.playpal.entities.notifications.Notification;
import com.hit.playpal.entities.relationships.enums.RelationshipStatus;

import java.util.List;

public interface INotificationsRepository {
    Task<List<Notification>> getFriendRequestsOfUser(String iUserId);
    Task<Void> removeFriendRequestNotification(String iCurrentUserId, String iUserFriendRequestIdToReject);
    Task<Void> updateUsersRelationshipStatus(String iCurrentUserId, String iOtherUserId, RelationshipStatus iNewStatus);
}
