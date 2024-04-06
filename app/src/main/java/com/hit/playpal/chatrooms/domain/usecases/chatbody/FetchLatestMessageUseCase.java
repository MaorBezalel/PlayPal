package com.hit.playpal.chatrooms.domain.usecases.chatbody;

import com.hit.playpal.chatrooms.domain.listeners.INewMessageEventListener;
import com.hit.playpal.chatrooms.domain.listeners.INewMessageRegistrationListener;
import com.hit.playpal.chatrooms.domain.repositories.IChatRoomRepository;

public class FetchLatestMessageUseCase {
    private final IChatRoomRepository REPOSITORY;

    public FetchLatestMessageUseCase(IChatRoomRepository iRepository) {
        REPOSITORY = iRepository;
    }

    public INewMessageRegistrationListener execute(String iChatRoomId, INewMessageEventListener iEventListener) {
        return REPOSITORY.listenForTheLatestMessage(iChatRoomId, iEventListener);
    }
}
