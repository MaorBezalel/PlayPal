package com.hit.playpal.chatrooms.data.repositories;

import androidx.lifecycle.ViewModelKt;
import androidx.paging.CachedPagingDataKt;
import androidx.paging.Pager;
import androidx.paging.PagingConfig;
import androidx.paging.PagingData;
import androidx.paging.PagingLiveData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hit.playpal.chatrooms.data.datasources.FirebaseFirestoreDataSource;
import com.hit.playpal.chatrooms.domain.listeners.INewMessageEventListener;
import com.hit.playpal.chatrooms.domain.listeners.INewMessageRegistrationListener;
import com.hit.playpal.chatrooms.domain.repositories.IChatRoomRepository;
import com.hit.playpal.entities.chats.Message;
import com.hit.playpal.entities.chats.enums.ContentType;

import java.util.Date;

import kotlinx.coroutines.flow.Flow;

public class ChatRoomRepository implements IChatRoomRepository {
    private final FirebaseFirestoreDataSource DB = new FirebaseFirestoreDataSource();

    @Override
    public INewMessageRegistrationListener listenForTheLatestMessage(String iChatRoomId, INewMessageEventListener iEventListener) {
        return DB.listenForTheLatestMessage(iChatRoomId, iEventListener);
    }

    @Override
    public Task<DocumentReference> sendMessage(String iChatRoomId, Message iMessage) {
        return DB.writeMessage(iChatRoomId, iMessage);
    }

    @Override
    public Task<Void> updateLastMessage(String iChatRoomId, Message iMessage) {
        return DB.updateLastMessage(iChatRoomId, iMessage);
    }

    @Override
    public Task<DocumentSnapshot> getChatRoom(String iChatRoomId) {
        return DB.getGroupChatProfile(iChatRoomId);
    }

    @Override
    public Task<QuerySnapshot> getMessagesInPage(String iChatRoomId, long iPageSize, DocumentSnapshot iAfterThisMessageRef) {
        return DB.loadMessages(iChatRoomId, iPageSize, iAfterThisMessageRef);
    }
}
