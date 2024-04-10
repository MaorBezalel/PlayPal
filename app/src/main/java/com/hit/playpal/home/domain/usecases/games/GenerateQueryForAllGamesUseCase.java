package com.hit.playpal.home.domain.usecases.games;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.Query;
import com.hit.playpal.home.data.repositories.CreateGroupChatRoomRepository;
import com.hit.playpal.home.domain.repositories.ICreateGroupChatRoomRepository;

import org.jetbrains.annotations.Contract;

public class GenerateQueryForAllGamesUseCase {
    private final ICreateGroupChatRoomRepository REPOSITORY;

    public GenerateQueryForAllGamesUseCase(ICreateGroupChatRoomRepository repository) {
        this.REPOSITORY = repository;
    }

    public Query execute() {
        return REPOSITORY.getAllGamesQuery();
    }

    @NonNull
    @Contract(value = "_ -> new", pure = true)
    public static Query invoke() {
        return new GenerateQueryForAllGamesUseCase(new CreateGroupChatRoomRepository()).execute();
    }
}
