package com.hit.playpal.chatrooms.domain.usecases.chatbody;

import com.hit.playpal.chatrooms.domain.repositories.IChatRoomRepository;
import com.hit.playpal.entities.users.User;

import java.util.concurrent.CompletableFuture;

public class AddNewMemberToGroupChatRoomUseCase {
    private final IChatRoomRepository REPOSITORY;

    public AddNewMemberToGroupChatRoomUseCase(IChatRoomRepository iRepository) {
        REPOSITORY = iRepository;
    }

    public CompletableFuture<Void> execute(String iChatRoomId,User iUser) {
        CompletableFuture<Void> future = new CompletableFuture<>();

        REPOSITORY.addNewMemberToGroupChatRoom(iChatRoomId, iUser)
                .addOnSuccessListener(future::complete)
                .addOnFailureListener(future::completeExceptionally);

        return future;
    }
}
