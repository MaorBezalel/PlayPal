package com.hit.playpal.home.domain.usecases.users;

import com.google.firebase.firestore.Query;
import com.hit.playpal.home.data.repositories.CreateGroupChatRoomRepository;
import com.hit.playpal.home.domain.repositories.ICreateGroupChatRoomRepository;

public class GenerateQueryForAllUsersUseCase {
    private final ICreateGroupChatRoomRepository REPOSITORY;

    public GenerateQueryForAllUsersUseCase(ICreateGroupChatRoomRepository iRepository) {
        REPOSITORY = iRepository;
    }

    public Query execute(String iUsername) {
        return REPOSITORY.getAllUsersQuery(iUsername);
    }

    public static Query invoke(String iUsername) {
        return new GenerateQueryForAllUsersUseCase(new CreateGroupChatRoomRepository()).execute(iUsername);
    }
}
