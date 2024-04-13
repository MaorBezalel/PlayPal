package com.hit.playpal.profile.domain.usecases;

import com.hit.playpal.entities.chats.o2o.OneToOneChatRoom;
import com.hit.playpal.profile.data.repositories.ProfileRepository;
import com.hit.playpal.profile.domain.repositories.IProfileRepository;

import java.util.concurrent.CompletableFuture;

public class CreateAndGetNewOneToOneChatRoomUseCase {
    private final IProfileRepository mProfileRepository;

    public CreateAndGetNewOneToOneChatRoomUseCase(IProfileRepository iProfileRepository) {
        mProfileRepository = iProfileRepository;
    }

    public CompletableFuture<String> execute(OneToOneChatRoom iOneToOneChatRoom) {
        CompletableFuture<String> future = new CompletableFuture<>();

        mProfileRepository.createAndGetNewOneToOneChatRoom(iOneToOneChatRoom).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                future.complete(task.getResult().getId());
            } else {
                future.completeExceptionally(task.getException());
            }
        });

        return future;
    }

    public static CompletableFuture<String> invoke(OneToOneChatRoom iOneToOneChatRoom) {
        return new CreateAndGetNewOneToOneChatRoomUseCase(new ProfileRepository()).execute(iOneToOneChatRoom);
    }
}
