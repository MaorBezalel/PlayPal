package com.hit.playpal.home.domain.usecases.notifications;

import com.hit.playpal.home.data.repositories.NotificationsRepository;

import java.util.concurrent.CompletableFuture;

public class RemoveFriendRequestNotificationUseCase {
    private final NotificationsRepository mNotificationsRepository;

    public RemoveFriendRequestNotificationUseCase(NotificationsRepository iNotificationsRepository) {
        this.mNotificationsRepository = iNotificationsRepository;
    }

    public CompletableFuture<Void> execute(String iCurrentUserId, String iUserFriendRequestIdToReject) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        mNotificationsRepository.removeFriendRequestNotification(iCurrentUserId, iUserFriendRequestIdToReject)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        future.complete(null);
                    } else {
                        future.completeExceptionally(task.getException());
                    }
                });

        return future;
    }
}
