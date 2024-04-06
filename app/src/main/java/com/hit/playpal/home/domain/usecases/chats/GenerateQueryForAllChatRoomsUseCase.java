package com.hit.playpal.home.domain.usecases.chats;

import com.google.firebase.firestore.Query;
import com.hit.playpal.home.data.repositories.ChatsRepository;
import com.hit.playpal.home.domain.repositories.IChatsRepository;

public class GenerateQueryForAllChatRoomsUseCase {
    private final IChatsRepository REPOSITORY;

    public GenerateQueryForAllChatRoomsUseCase(IChatsRepository iRepository) {
        REPOSITORY = iRepository;
    }

    public Query execute(String iUserId) {
        return REPOSITORY.generateQueryForAllChatRooms(iUserId);
    }

    public static Query invoke(String iUserId) {
        return new GenerateQueryForAllChatRoomsUseCase(new ChatsRepository()).execute(iUserId);
    }
}
