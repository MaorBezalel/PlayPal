package com.hit.playpal.home.data.repositories;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hit.playpal.entities.users.Notification;
import com.hit.playpal.entities.users.enums.RelationshipStatus;
import com.hit.playpal.game.data.utils.exceptions.DatabaseErrorException;
import com.hit.playpal.home.data.datasources.NotificationsFirebaseFirestoreDataSource;
import com.hit.playpal.home.domain.repositories.INotificationsRepository;
import com.hit.playpal.home.domain.utils.FriendRequestNotFoundException;
import com.hit.playpal.home.domain.utils.FriendRequestRemovalFailedException;
import com.hit.playpal.home.domain.utils.RelationshipUpdateFailedException;

import java.util.List;

public class NotificationsRepository  implements INotificationsRepository {
    private final NotificationsFirebaseFirestoreDataSource DB = new NotificationsFirebaseFirestoreDataSource();
    @Override
    public Task<List<Notification>> getFriendRequestsOfUser(String iUserId) {
        return DB.getFriendRequestsOfUser(iUserId).continueWith(task -> {
            if(task.isSuccessful())
            {
                QuerySnapshot querySnapshot = task.getResult();
                return querySnapshot.toObjects(Notification.class);
            }
            else
            {
                throw new DatabaseErrorException();
            }
        });
    }

    @Override
    public Task<Void> removeFriendRequestNotification(String iCurrentUserId, String iUserFriendRequestIdToReject) {
        return DB.getFriendRequestNotification(iCurrentUserId, iUserFriendRequestIdToReject)
                .continueWith(task -> {
                    if(task.isSuccessful())
                    {
                        QuerySnapshot querySnapshot = task.getResult();
                        if(!querySnapshot.getDocuments().isEmpty())
                        {
                            DocumentSnapshot documentSnapshot = querySnapshot.getDocuments().get(0);
                            DB.deleteNotification(iCurrentUserId, documentSnapshot.getId()).continueWith(task1 -> {
                                if(task1.isSuccessful())
                                {
                                    return null;
                                }
                                else
                                {
                                    throw new FriendRequestRemovalFailedException();
                                }
                            });
                        }
                        else
                        {
                            throw new FriendRequestNotFoundException();
                        }
                    }
                    else
                    {
                        throw new DatabaseErrorException();
                    }
                    return null;
                });
    }

    @Override
    public Task<Void> updateUsersRelationshipStatus(String iCurrentUserId, String iOtherUserId, RelationshipStatus iNewStatus) {

        Task<QuerySnapshot> currentToOther = DB.getRelationshipStatus(iCurrentUserId, iOtherUserId);
        Task<QuerySnapshot> otherToCurrent = DB.getRelationshipStatus(iOtherUserId, iCurrentUserId);

        return Tasks.whenAll(currentToOther, otherToCurrent).continueWith(task -> {
            if(task.isSuccessful())
            {

                QuerySnapshot currentToOtherQuerySnapshot = currentToOther.getResult();
                QuerySnapshot otherToCurrentQuerySnapshot = otherToCurrent.getResult();

                if(!currentToOtherQuerySnapshot.getDocuments().isEmpty() && !otherToCurrentQuerySnapshot.getDocuments().isEmpty())
                {
                    DocumentSnapshot currentToOtherDoc = currentToOtherQuerySnapshot.getDocuments().get(0);
                    DocumentSnapshot otherToCurrentDoc = otherToCurrentQuerySnapshot.getDocuments().get(0);

                    if(iNewStatus == RelationshipStatus.none)
                    {
                        Task<Void> removeFromCurrentToOther = DB.removeRelationshipStatus(iCurrentUserId, currentToOtherDoc.getId());
                        Task<Void> removeFromOtherToCurrent = DB.removeRelationshipStatus(iOtherUserId, otherToCurrentDoc.getId());

                        Tasks.whenAll(removeFromCurrentToOther, removeFromOtherToCurrent).continueWith(task1 -> {
                            if(task1.isSuccessful())
                            {
                                return null;
                            }
                            else
                            {
                                throw new RelationshipUpdateFailedException(iNewStatus);
                            }
                        });
                    }
                    else
                    {
                        Task<Void> updateCurrentToOther = DB.updateRelationshipStatus(iCurrentUserId, currentToOtherDoc.getId(), iNewStatus);
                        Task<Void> updateOtherToCurrent = DB.updateRelationshipStatus(iOtherUserId, otherToCurrentDoc.getId(), iNewStatus);

                        Tasks.whenAll(updateCurrentToOther, updateOtherToCurrent).continueWith(task1 -> {
                            if(task1.isSuccessful())
                            {
                                return null;
                            }
                            else
                            {
                                throw new RelationshipUpdateFailedException(iNewStatus);
                            }
                        });
                    }
                }
                else
                {
                    throw new RelationshipUpdateFailedException(iNewStatus);
                }
            }
            else
            {
                throw new DatabaseErrorException();
            }
            return null;
        });
    }
}
