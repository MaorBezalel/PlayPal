package com.hit.playpal.home.domain.usecases.chats;

import android.util.Log;

import com.hit.playpal.entities.chats.GroupChatRoom;
import com.hit.playpal.entities.chats.GroupProfile;
import com.hit.playpal.home.data.repositories.CreateGroupChatRoomRepository;
import com.hit.playpal.home.domain.enums.CreateGroupChatRoomFailure;
import com.hit.playpal.home.domain.repositories.ICreateGroupChatRoomRepository;
import com.hit.playpal.utils.UseCaseResult;

import java.util.concurrent.CompletableFuture;

public class CreateGroupChatRoomUseCase {
    private final ICreateGroupChatRoomRepository REPOSITORY;

    public CreateGroupChatRoomUseCase(ICreateGroupChatRoomRepository iRepository) {
        REPOSITORY = iRepository;
    }

    public CompletableFuture<UseCaseResult<String, CreateGroupChatRoomFailure>> execute(GroupChatRoom iGroupChatRoom) {
        CompletableFuture<UseCaseResult<String, CreateGroupChatRoomFailure>> future = new CompletableFuture<>();

        REPOSITORY.createGroupChatRoom(iGroupChatRoom).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                Log.i("CreateGroupChatRoomUseCase", "Group chat room created successfully");
                future.complete(UseCaseResult.forSuccess(task.getResult().getId()));
            } else {
                Log.e("CreateGroupChatRoomUseCase", "Group chat room creation failed", task.getException());
                future.complete(UseCaseResult.forFailure(CreateGroupChatRoomFailure.GROUP_CHAT_ROOM_CREATION_FAILED));
            }
        });

        return future;
    }

    public static CompletableFuture<UseCaseResult<String, CreateGroupChatRoomFailure>> invoke(GroupChatRoom iGroupChatRoom) {
        return new CreateGroupChatRoomUseCase(new CreateGroupChatRoomRepository()).execute(iGroupChatRoom);
    }
}
