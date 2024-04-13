package com.hit.playpal.home.domain.usecases.chats;

import android.util.Log;

import com.hit.playpal.entities.chats.group.GroupProfile;
import com.hit.playpal.home.data.repositories.CreateGroupChatRoomRepository;
import com.hit.playpal.home.domain.enums.CreateGroupChatRoomFailure;
import com.hit.playpal.home.domain.repositories.ICreateGroupChatRoomRepository;
import com.hit.playpal.utils.UseCaseResult;

import java.util.concurrent.CompletableFuture;

public class CreateGroupChatProfileUseCase {
    private final ICreateGroupChatRoomRepository REPOSITORY;

    public CreateGroupChatProfileUseCase(ICreateGroupChatRoomRepository iRepository) {
        REPOSITORY = iRepository;
    }

    public CompletableFuture<UseCaseResult<Void, CreateGroupChatRoomFailure>> execute(
            String iChatRoomId,
            GroupProfile iGroupProfile
    ) {
        CompletableFuture<UseCaseResult<Void, CreateGroupChatRoomFailure>> future = new CompletableFuture<>();

        REPOSITORY.createGroupProfile(iChatRoomId, iGroupProfile).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i("CreateGroupChatProfileUseCase", "Group chat room profile created successfully");
                future.complete(UseCaseResult.forSuccess(null));
            } else {
                Log.e("CreateGroupChatProfileUseCase", "Group chat room profile creation failed", task.getException());
                future.complete(UseCaseResult.forFailure(CreateGroupChatRoomFailure.GROUP_CHAT_ROOM_PROFILE_CREATION_FAILED));
            }
        });

        return future;
    }

    public static CompletableFuture<UseCaseResult<Void, CreateGroupChatRoomFailure>> invoke(
            String iChatRoomId,
            GroupProfile iGroupProfile
    ) {
        return new CreateGroupChatProfileUseCase(new CreateGroupChatRoomRepository()).execute(iChatRoomId, iGroupProfile);
    }
}
