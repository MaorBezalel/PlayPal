package com.hit.playpal.chatrooms.domain.usecases.chatinfopage;

import com.google.firebase.firestore.DocumentSnapshot;
import com.hit.playpal.chatrooms.domain.repositories.IChatRoomRepository;
import com.hit.playpal.chatrooms.domain.utils.exceptions.RoomNotFoundException;
import com.hit.playpal.entities.chats.GroupProfile;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GetMembersOfGroupChatUseCase {
    private final IChatRoomRepository REPOSITORY;

    public GetMembersOfGroupChatUseCase(IChatRoomRepository iRepository) {
        REPOSITORY = iRepository;
    }

    public CompletableFuture<List<GroupProfile.Participant>> execute(String iChatRoomId) {
            CompletableFuture<List<GroupProfile.Participant>> future = new CompletableFuture<>();

            REPOSITORY.getChatRoom(iChatRoomId)
                    .addOnCompleteListener(task -> {
                        if (task.isSuccessful())
                        {
                            DocumentSnapshot document = task.getResult();
                            if (document.exists())
                            {
                                GroupProfile groupProfile = document.toObject(GroupProfile.class);

                                if (groupProfile != null)
                                {
                                    future.complete(groupProfile.getParticipants());
                                }
                                else
                                {
                                    future.completeExceptionally(new RoomNotFoundException());
                                }
                            }
                            else
                            {
                                future.completeExceptionally(new RoomNotFoundException());
                            }
                        }
                        else
                        {
                            future.completeExceptionally(task.getException());
                        }
                    });
            return future;
    }
}
