package com.hit.playpal.home.domain.usecases.chats;

import com.google.firebase.firestore.Query;
import com.hit.playpal.entities.chats.enums.ChatRoomType;
import com.hit.playpal.home.data.repositories.ChatsRepository;
import com.hit.playpal.home.domain.repositories.IChatsRepository;

public class GenerateQueryForSpecificChatRoomsUseCase {
    private final IChatsRepository REPOSITORY;

    public GenerateQueryForSpecificChatRoomsUseCase(IChatsRepository iRepository) {
        REPOSITORY = iRepository;
    }

    public Query execute(String iUserId, ChatRoomType iChatRoomType) {
        return REPOSITORY.generateQueryForSpecificChatRooms(iUserId, iChatRoomType);
    }

    public static Query invoke(String iUserId, ChatRoomType iChatRoomType) {
        return new GenerateQueryForSpecificChatRoomsUseCase(new ChatsRepository()).execute(iUserId, iChatRoomType);
    }
}
