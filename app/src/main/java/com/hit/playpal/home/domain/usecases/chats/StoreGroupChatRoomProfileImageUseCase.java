package com.hit.playpal.home.domain.usecases.chats;

import com.hit.playpal.home.data.repositories.CreateGroupChatRoomRepository;
import com.hit.playpal.home.domain.enums.CreateGroupChatRoomFailure;
import com.hit.playpal.home.domain.repositories.ICreateGroupChatRoomRepository;
import com.hit.playpal.utils.UseCaseResult;

import java.util.concurrent.CompletableFuture;

public class StoreGroupChatRoomProfileImageUseCase {
    private final ICreateGroupChatRoomRepository REPOSITORY;

    public StoreGroupChatRoomProfileImageUseCase(ICreateGroupChatRoomRepository iRepository) {
        REPOSITORY = iRepository;
    }

    public CompletableFuture<UseCaseResult<Void, CreateGroupChatRoomFailure>> execute(String iChatRoomId, String iImageUri) {
        CompletableFuture<UseCaseResult<Void, CreateGroupChatRoomFailure>> future = new CompletableFuture<>();

        REPOSITORY.storeGroupChatRoomProfileImage(iChatRoomId, iImageUri)
            .addOnCompleteListener(task -> {
                if (task.isSuccessful()) {
                    future.complete(UseCaseResult.forSuccessWithoutResult());
                } else {
                    future.complete(UseCaseResult.forFailure(CreateGroupChatRoomFailure.GROUP_CHAT_ROOM_PROFILE_IMAGE_STORE_IN_DB_FAILED));
                }
            });

        return future;
    }

    public static CompletableFuture<UseCaseResult<Void, CreateGroupChatRoomFailure>> invoke(String iChatRoomId, String iImageUri) {
        return new StoreGroupChatRoomProfileImageUseCase(new CreateGroupChatRoomRepository()).execute(iChatRoomId, iImageUri);
    }
}
