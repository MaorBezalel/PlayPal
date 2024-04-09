package com.hit.playpal.chatrooms.domain.repositories;

import androidx.paging.Pager;
import androidx.paging.PagingData;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.QuerySnapshot;
import com.hit.playpal.chatrooms.domain.listeners.INewMessageEventListener;
import com.hit.playpal.chatrooms.domain.listeners.INewMessageRegistrationListener;
import com.hit.playpal.entities.chats.ChatRoom;
import com.hit.playpal.entities.chats.Message;
import com.hit.playpal.entities.chats.enums.AppearancePolicy;
import com.hit.playpal.entities.chats.enums.ContentType;
import com.hit.playpal.entities.chats.enums.JoiningPolicy;
import com.hit.playpal.entities.chats.enums.UserChatRole;
import com.hit.playpal.entities.users.User;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

import kotlinx.coroutines.flow.Flow;

public interface IChatRoomRepository {
//    ChatRoom getChatRoomInfo(String iChatRoomId);

    INewMessageRegistrationListener listenForTheLatestMessage(String iChatRoomId, INewMessageEventListener iEventListener);
    Flow<PagingData<Message>> fetchMessages(String iChatRoomId, int iPageSize);
    Pager<DocumentReference, Message> getPagerToFetchMessages(String iChatRoomId, int iPageSize);
    Task<DocumentReference> sendMessage(String iChatRoomId, Message iMessage);
    Task<Void> updateLastMessage(String iChatRoomId, Message iMessage);
    Task<Void> deleteMessage(String iChatRoomId, String iMessageId);
    Task<DocumentSnapshot> getChatRoom(String iChatRoomId);

    Task<QuerySnapshot> getMessagesInPage(String iChatRoomId, long iPageSize, DocumentSnapshot iAfterThisMessageRef);

//    List<User> getMembersOfGroupChatRoom(String iChatRoomId, int iPage);
//    void sendGroupInvitationTo(String iChatRoomId, String iUsernameToInvite);
//    void kickMemberFromGroupChatRoom(String iChatRoomId, String iUsernameToRemove);
//
//    void sendRequestToOwnerToJoinGroupChatRoom(String iChatRoomId, String iUsernameOfTheOwner);
//
//    void findSpecificTextMessage(String iChatRoomId, String iTextMessage, int iPage);
//    void getAllMediaMessages(String iChatRoomId, int iPage);
//
//    void updateRoleOfMember(String iChatRoomId, String iUsername, UserChatRole iRole); // including co-owner (require member to be moderator!)
//
//    HashMap<JoiningPolicy, Boolean> getChatGroupJoiningPolicy(String iChatRoomId);
//    void updateChatGroupJoiningPolicy(String iChatRoomId, HashMap<JoiningPolicy, Boolean> iJoiningPolicy);
//
//    HashMap<AppearancePolicy, Boolean> getChatGroupAppearancePolicy(String iChatRoomId);
//    void updateChatGroupAppearancePolicy(String iChatRoomId, HashMap<AppearancePolicy, Boolean> iAppearancePolicy);

}
