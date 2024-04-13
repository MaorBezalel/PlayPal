package com.hit.playpal.chatrooms.domain.repositories;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import com.hit.playpal.chatrooms.domain.listeners.INewMessageEventListener;
import com.hit.playpal.chatrooms.domain.listeners.INewMessageRegistrationListener;
import com.hit.playpal.entities.chats.Message;
import com.hit.playpal.entities.users.User;

public interface IChatRoomRepository {
    INewMessageRegistrationListener listenForTheLatestMessage(String iChatRoomId, INewMessageEventListener iEventListener);
    Task<DocumentReference> sendMessage(String iChatRoomId, Message iMessage);
    Task<Void> updateLastMessage(String iChatRoomId, Message iMessage);
    Task<DocumentSnapshot> getChatRoom(String iChatRoomId);
    Task<QuerySnapshot> getMessagesInPage(String iChatRoomId, long iPageSize, DocumentSnapshot iAfterThisMessageRef);

    Task<Void> addNewMemberToGroupChatRoom(String iChatRoomId, User iUser);
}
