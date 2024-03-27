package com.hit.playpal.home.domain.repositories;

import com.hit.playpal.entities.chats.Chat;
import com.hit.playpal.entities.chats.GroupChat;
import com.hit.playpal.entities.chats.OneToOneChat;
import com.hit.playpal.entities.chats.enums.ChatType;

import java.util.List;

public interface IChatRepository {
    GroupChat createGroupChat(String iGroupName, String iGroupPicture, String iGameName, String iGamePicture, String iOwnerUsername);
    List<OneToOneChat> getOneToOneChatsByUsername(String iUserName, int iPage);
    List<GroupChat> getGroupChatsByName(String iChatName, int iPage);
    List<Chat> getChatsByType(ChatType iChatType, int iPage);
    List<Chat> getChats(int iPage);
}
