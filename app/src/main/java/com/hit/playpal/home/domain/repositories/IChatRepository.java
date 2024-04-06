package com.hit.playpal.home.domain.repositories;

import com.hit.playpal.entities.chats.ChatRoom;
import com.hit.playpal.entities.chats.GroupChatRoom;
import com.hit.playpal.entities.chats.OneToOneChatRoom;
import com.hit.playpal.entities.chats.enums.ChatRoomType;

import java.util.List;

public interface IChatRepository {
    GroupChatRoom createGroupChat(String iGroupName, String iGroupPicture, String iGameName, String iGamePicture, String iOwnerUsername);
    List<OneToOneChatRoom> getOneToOneChatsByUsername(String iUserName, int iPage);
    List<GroupChatRoom> getGroupChatsByName(String iChatName, int iPage);
    List<ChatRoom> getChatsByType(ChatRoomType iChatRoomType, int iPage);
    List<ChatRoom> getChats(int iPage);
}
