package com.hit.playpal.home.domain.usecases.notifications;

import com.hit.playpal.entities.users.Notification;
import com.hit.playpal.home.data.repositories.NotificationsRepository;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GetFriendRequestsOfUserUseCase {
    private final NotificationsRepository notificationsRepository;

    public GetFriendRequestsOfUserUseCase(NotificationsRepository iNotificationsRepository) {
        notificationsRepository = iNotificationsRepository;
    }

    public CompletableFuture<List<Notification>> execute(String iUid) {
        CompletableFuture<List<Notification>> future = new CompletableFuture<>();

        notificationsRepository.getFriendRequestsOfUser(iUid).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                future.complete(task.getResult());
            } else {
                future.completeExceptionally(task.getException());
            }
        });

        return future;
    }
}
