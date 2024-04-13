package com.hit.playpal.profile.domain.usecases;

import com.hit.playpal.entities.relationships.OneToOneChatRelationship;
import com.hit.playpal.profile.data.repositories.ProfileRepository;
import com.hit.playpal.profile.domain.repositories.IProfileRepository;

import java.util.concurrent.CompletableFuture;

public class CreateNewOneToOneChatRelationshipUseCase {

    private final IProfileRepository mProfileRepository;

    public CreateNewOneToOneChatRelationshipUseCase(IProfileRepository iProfileRepository) {
        mProfileRepository = iProfileRepository;
    }

    public CompletableFuture<Void> execute(OneToOneChatRelationship iOneToOneChatRelationship) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        mProfileRepository.createNewOneToOneChatRelationship(iOneToOneChatRelationship).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                future.complete(null);
            } else {
                future.completeExceptionally(task.getException());
            }
        });

        return future;
    }


    public static CompletableFuture<Void> invoke(OneToOneChatRelationship iOneToOneChatRelationship) {
        return new CreateNewOneToOneChatRelationshipUseCase(new ProfileRepository()).execute(iOneToOneChatRelationship);
    }
}
