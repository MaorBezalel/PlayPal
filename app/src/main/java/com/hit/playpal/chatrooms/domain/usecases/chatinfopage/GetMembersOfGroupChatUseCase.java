package com.hit.playpal.chatrooms.domain.usecases.chatinfopage;

import com.google.firebase.firestore.DocumentSnapshot;
import com.hit.playpal.chatrooms.domain.repositories.IChatRoomRepository;
import com.hit.playpal.chatrooms.domain.utils.exceptions.RoomNotFound;
import com.hit.playpal.entities.chats.GroupChatRoom;
import com.hit.playpal.entities.users.User;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class GetMembersOfGroupChatUseCase {
    private final IChatRoomRepository REPOSITORY;

    public GetMembersOfGroupChatUseCase(IChatRoomRepository iRepository) {
        REPOSITORY = iRepository;
    }

    public CompletableFuture<List<User>> execute(String iChatRoomId) {
            return null;
    }
}
