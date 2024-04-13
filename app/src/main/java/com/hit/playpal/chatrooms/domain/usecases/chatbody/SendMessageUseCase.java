package com.hit.playpal.chatrooms.domain.usecases.chatbody;

import com.hit.playpal.chatrooms.domain.repositories.IChatRoomRepository;
import com.hit.playpal.chatrooms.domain.utils.enums.ChatBodyFailure;
import com.hit.playpal.entities.messages.Message;
import com.hit.playpal.utils.UseCaseResult;

import java.util.concurrent.CompletableFuture;

public class SendMessageUseCase {
    private final IChatRoomRepository REPOSITORY;

    public SendMessageUseCase(IChatRoomRepository iRepository) {
        REPOSITORY = iRepository;
    }

    public CompletableFuture<UseCaseResult<Message, ChatBodyFailure>> execute(String iChatRoomId, Message iMessage) {
        CompletableFuture<UseCaseResult<Message, ChatBodyFailure>> future = new CompletableFuture<>();

        REPOSITORY.sendMessage(iChatRoomId, iMessage)
                .addOnSuccessListener(documentReference -> future.complete(UseCaseResult.forSuccess(iMessage)))
                .addOnFailureListener(e -> future.complete(UseCaseResult.forFailure(ChatBodyFailure.MESSAGE_SEND_FAILED)));

        return future;
    }
}
