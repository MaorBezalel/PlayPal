package com.hit.playpal.profile.domain.usecases;

import com.hit.playpal.entities.chats.o2o.OneToOneChatRoom;
import com.hit.playpal.profile.data.repositories.ProfileRepository;
import com.hit.playpal.profile.domain.repositories.IProfileRepository;

import java.util.concurrent.CompletableFuture;

public class GetTheExistingOneToOneChatRoomUseCase {
    private final IProfileRepository mProfileRepository;

    public GetTheExistingOneToOneChatRoomUseCase(IProfileRepository iProfileRepository) {
        mProfileRepository = iProfileRepository;
    }

    public CompletableFuture<OneToOneChatRoom> execute(String iChatRoomId) {
        CompletableFuture<OneToOneChatRoom> future = new CompletableFuture<>();

        mProfileRepository.getTheExistingOneToOneChatRoom(iChatRoomId).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                future.complete(task.getResult().toObject(OneToOneChatRoom.class));
            } else {
                future.completeExceptionally(task.getException());
            }
        });

        return future;
    }

    public static CompletableFuture<OneToOneChatRoom> invoke(String iChatRoomId) {
        return new GetTheExistingOneToOneChatRoomUseCase(new ProfileRepository()).execute(iChatRoomId);
    }
}
