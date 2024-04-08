package com.hit.playpal.home.domain.usecases.chats;

import com.hit.playpal.entities.chats.ChatRoom;
import com.hit.playpal.home.data.repositories.CreateGroupChatRoomRepository;
import com.hit.playpal.home.domain.enums.CreateGroupChatRoomFailure;
import com.hit.playpal.home.domain.repositories.ICreateGroupChatRoomRepository;
import com.hit.playpal.utils.UseCaseResult;

import java.util.concurrent.CompletableFuture;

public class CreateGroupChatRoomUseCase {
    private final ICreateGroupChatRoomRepository REPOSITORY;

    public CreateGroupChatRoomUseCase(ICreateGroupChatRoomRepository iRepository) {
        REPOSITORY = iRepository;
    }

    public CompletableFuture<UseCaseResult<Void, CreateGroupChatRoomFailure>> execute(ChatRoom iChatRoom) {
        CompletableFuture<UseCaseResult<Void, CreateGroupChatRoomFailure>> future = new CompletableFuture<>();

        REPOSITORY.createGroupChatRoom(iChatRoom).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                future.complete(UseCaseResult.forSuccessWithoutResult());
            } else {
                future.complete(UseCaseResult.forFailure(CreateGroupChatRoomFailure.UNKNOWN_ERROR));
            }
        });

        return future;
    }

    public static CompletableFuture<UseCaseResult<Void, CreateGroupChatRoomFailure>> invoke(ChatRoom iChatRoom) {
        return new CreateGroupChatRoomUseCase(new CreateGroupChatRoomRepository()).execute(iChatRoom);
    }
}
