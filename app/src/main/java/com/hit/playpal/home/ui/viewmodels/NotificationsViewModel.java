package com.hit.playpal.home.ui.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hit.playpal.entities.notifications.Notification;
import com.hit.playpal.entities.relationships.enums.RelationshipStatus;
import com.hit.playpal.home.data.repositories.NotificationsRepository;
import com.hit.playpal.home.domain.usecases.notifications.GetFriendRequestsOfUserUseCase;
import com.hit.playpal.home.domain.usecases.notifications.RemoveFriendRequestNotificationUseCase;
import com.hit.playpal.home.domain.usecases.notifications.UpdateUsersRelationshipStatusUseCase;
import com.hit.playpal.utils.CurrentlyLoggedUser;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class NotificationsViewModel extends ViewModel {

    private final MutableLiveData<List<Notification>> friendRequests;
    public MutableLiveData<List<Notification>> getFriendRequests() {
        return friendRequests;
    }

    private final MutableLiveData<String> fetchFriendRequestsError = new MutableLiveData<>();
    public MutableLiveData<String> getFetchFriendRequestsError() {
        return fetchFriendRequestsError;
    }

    private final MutableLiveData<String> fetchFriendRequestsSuccess = new MutableLiveData<>();
    public MutableLiveData<String> getFetchFriendRequestsSuccess() {
        return fetchFriendRequestsSuccess;
    }

    private final MutableLiveData<RelationshipStatus> updateFriendRequestStatusSuccess = new MutableLiveData<>();
    public MutableLiveData<RelationshipStatus> getUpdateFriendRequestStatusSuccess() {
        return updateFriendRequestStatusSuccess;
    }

    private final MutableLiveData<String> updateFriendRequestStatusError = new MutableLiveData<>();
    public MutableLiveData<String> getUpdateFriendRequestStatusError() {
        return updateFriendRequestStatusError;
    }


    public NotificationsViewModel() {
        friendRequests = new MutableLiveData<>();
    }

    public void fetchFriendRequestsOfCurrentUser()
    {
        GetFriendRequestsOfUserUseCase useCase = new GetFriendRequestsOfUserUseCase(new NotificationsRepository());
        useCase.execute(CurrentlyLoggedUser.get().getUid()).
                whenComplete((notifications, throwable) -> {
                    if(throwable != null)
                    {
                        fetchFriendRequestsError.postValue(throwable.getMessage());
                    }
                    else
                    {
                        friendRequests.postValue(notifications);
                        fetchFriendRequestsSuccess.postValue("Friend requests fetched successfully");
                    }
                });
    }

    public CompletableFuture<Void> updatePendingFriendRequest(String iOtherUserId, RelationshipStatus iNewStatus)
    {
        UpdateUsersRelationshipStatusUseCase updateUsersRelationshipStatusUseCase = new UpdateUsersRelationshipStatusUseCase(new NotificationsRepository());
        RemoveFriendRequestNotificationUseCase removeFriendRequestNotificationUseCase = new RemoveFriendRequestNotificationUseCase(new NotificationsRepository());

        return updateUsersRelationshipStatusUseCase.execute(
                CurrentlyLoggedUser.get().getUid(),
                iOtherUserId,
                iNewStatus)
                .thenCompose(aVoid -> removeFriendRequestNotificationUseCase.execute(
                        CurrentlyLoggedUser.get().getUid(),
                        iOtherUserId))
                .whenComplete((result, throwable) -> {
                    if(throwable != null)
                    {
                        updateFriendRequestStatusError.postValue(throwable.getMessage());
                    }
                    else
                    {
                        updateFriendRequestStatusSuccess.postValue(iNewStatus);
                    }
                });
    }
}
