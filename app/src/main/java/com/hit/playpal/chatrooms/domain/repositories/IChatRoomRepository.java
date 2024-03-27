package com.hit.playpal.chatrooms.domain.repositories;

import chats.Chat;
import chats.enums.AppearancePolicy;
import chats.enums.ContentType;
import chats.enums.JoiningPolicy;
import chats.enums.UserChatRole;
import users.User;

import java.util.Date;
import java.util.HashMap;
import java.util.List;

public interface IChatRoomRepository {
    Chat getChatRoomInfo(String iChatRoomId);

    void getAllMessages(String iChatRoomId, int iPage);
    void sendMessage(String iChatRoomId, String iMessageBody, ContentType iContentType, Date iSentAt);
    void deleteMessage(String iChatRoomId, String iMessageId);

    List<User> getMembersOfGroupChatRoom(String iChatRoomId, int iPage);
    void sendGroupInvitationTo(String iChatRoomId, String iUsernameToInvite);
    void kickMemberFromGroupChatRoom(String iChatRoomId, String iUsernameToRemove);

    void sendRequestToOwnerToJoinGroupChatRoom(String iChatRoomId, String iUsernameOfTheOwner);

    void findSpecificTextMessage(String iChatRoomId, String iTextMessage, int iPage);
    void getAllMediaMessages(String iChatRoomId, int iPage);

    void updateRoleOfMember(String iChatRoomId, String iUsername, UserChatRole iRole); // including co-owner (require member to be moderator!)

    HashMap<JoiningPolicy, Boolean> getChatGroupJoiningPolicy(String iChatRoomId);
    void updateChatGroupJoiningPolicy(String iChatRoomId, HashMap<JoiningPolicy, Boolean> iJoiningPolicy);

    HashMap<AppearancePolicy, Boolean> getChatGroupAppearancePolicy(String iChatRoomId);
    void updateChatGroupAppearancePolicy(String iChatRoomId, HashMap<AppearancePolicy, Boolean> iAppearancePolicy);

}
