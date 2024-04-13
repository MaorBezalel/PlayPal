package com.hit.playpal.chatrooms.domain.usecases.chatbody;

import com.hit.playpal.chatrooms.domain.repositories.IChatRoomRepository;
import com.hit.playpal.chatrooms.domain.utils.enums.ChatBodyFailure;
import com.hit.playpal.entities.messages.Message;
import com.hit.playpal.utils.UseCaseResult;

import java.util.concurrent.CompletableFuture;

public class UpdateChatRoomLastMessageUseCase {
    private final IChatRoomRepository REPOSITORY;

    public UpdateChatRoomLastMessageUseCase(IChatRoomRepository iRepository) {
        REPOSITORY = iRepository;
    }

    public CompletableFuture<UseCaseResult<Void, ChatBodyFailure>> execute(String iChatRoomId, Message iMessage) {
        CompletableFuture<UseCaseResult<Void, ChatBodyFailure>> future = new CompletableFuture<>();

        REPOSITORY.updateLastMessage(iChatRoomId, iMessage)
                .addOnSuccessListener(aVoid -> future.complete(UseCaseResult.forSuccessWithoutResult()))
                .addOnFailureListener(e -> future.complete(UseCaseResult.forFailure(ChatBodyFailure.LAST_MESSAGE_UPDATE_FAILED)));

        return future;
    }
}
