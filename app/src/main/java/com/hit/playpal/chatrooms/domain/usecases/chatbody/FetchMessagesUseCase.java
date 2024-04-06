package com.hit.playpal.chatrooms.domain.usecases.chatbody;

import androidx.paging.Pager;
import androidx.paging.PagingData;

import com.google.firebase.firestore.DocumentReference;
import com.hit.playpal.chatrooms.domain.repositories.IChatRoomRepository;
import com.hit.playpal.entities.chats.Message;

import kotlinx.coroutines.flow.Flow;

public class FetchMessagesUseCase {
    private final IChatRoomRepository REPOSITORY;

    public FetchMessagesUseCase(IChatRoomRepository iRepository) {
        REPOSITORY = iRepository;
    }

    public Flow<PagingData<Message>> execute(String iChatRoomId, int iPageSize) {
        return REPOSITORY.fetchMessages(iChatRoomId, iPageSize);
    }

    public Pager<DocumentReference, Message> execute2(String iChatRoomId, int iPageSize) {
        return REPOSITORY.getPagerToFetchMessages(iChatRoomId, iPageSize);
    }
}
