package com.hit.playpal.home.domain.usecases.chats;

import android.net.Uri;

import com.hit.playpal.home.data.repositories.CreateGroupChatRoomRepository;
import com.hit.playpal.home.domain.enums.CreateGroupChatRoomFailure;
import com.hit.playpal.home.domain.repositories.ICreateGroupChatRoomRepository;
import com.hit.playpal.utils.UseCaseResult;

import java.util.concurrent.CompletableFuture;

public class UploadGroupChatRoomProfileImageUseCase {
    private final ICreateGroupChatRoomRepository REPOSITORY;

    public UploadGroupChatRoomProfileImageUseCase(ICreateGroupChatRoomRepository iRepository) {
        REPOSITORY = iRepository;
    }

    public CompletableFuture<UseCaseResult<Uri, CreateGroupChatRoomFailure>> execute(
            String iChatRoomId,
             Uri iImageUri
    ) {
        CompletableFuture<UseCaseResult<Uri, CreateGroupChatRoomFailure>> future = new CompletableFuture<>();

        REPOSITORY.uploadGroupChatRoomProfileImage(iChatRoomId, iImageUri)
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        future.complete(UseCaseResult.forSuccess(task.getResult()));
                    } else {
                        future.complete(UseCaseResult.forFailure(CreateGroupChatRoomFailure.GROUP_CHAT_ROOM_PROFILE_IMAGE_URI_UPLOAD_FAILED));
                    }
                });

        return future;
    }

    public static CompletableFuture<UseCaseResult<Uri, CreateGroupChatRoomFailure>> invoke(
            String iChatRoomId,
            Uri iImageUri
    ) {
        return new UploadGroupChatRoomProfileImageUseCase(new CreateGroupChatRoomRepository()).execute(iChatRoomId, iImageUri);
    }
}
