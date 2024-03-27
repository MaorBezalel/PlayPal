package com.hit.playpal.home.domain.repositories;

import chats.Chat;
import chats.GroupChat;
import chats.OneToOneChat;
import chats.enums.ChatType;

import javax.swing.*;
import java.util.List;

public interface IChatRepository {
    GroupChat createGroupChat(String iGroupName, String iGroupPicture, String iGameName, String iGamePicture, String iOwnerUsername);
    List<OneToOneChat> getOneToOneChatsByUsername(String iUserName, int iPage);
    List<GroupChat> getGroupChatsByName(String iChatName, int iPage);
    List<Chat> getChatsByType(ChatType iChatType, int iPage);
    List<Chat> getChats(int iPage);
}
