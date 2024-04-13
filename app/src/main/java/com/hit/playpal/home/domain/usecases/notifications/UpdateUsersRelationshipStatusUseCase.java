package com.hit.playpal.home.domain.usecases.notifications;

import com.hit.playpal.entities.relationships.enums.RelationshipStatus;
import com.hit.playpal.home.data.repositories.NotificationsRepository;

import java.util.concurrent.CompletableFuture;

public class UpdateUsersRelationshipStatusUseCase {
    private final NotificationsRepository mNotificationsRepository;

    public UpdateUsersRelationshipStatusUseCase(NotificationsRepository iNotificationsRepository) {
        this.mNotificationsRepository = iNotificationsRepository;
    }

    public CompletableFuture<Void> execute(String iCurrentUserId, String iOtherUserId, RelationshipStatus iNewStatus) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        mNotificationsRepository.updateUsersRelationshipStatus(iCurrentUserId, iOtherUserId, iNewStatus)
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
