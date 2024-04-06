package com.hit.playpal.chatrooms.domain.listeners;

import com.hit.playpal.entities.chats.ChatRoom;

public interface INewMessageEventListener {
    void onFetched(ChatRoom iChatRoom);
    void onError(Exception iException);
}
