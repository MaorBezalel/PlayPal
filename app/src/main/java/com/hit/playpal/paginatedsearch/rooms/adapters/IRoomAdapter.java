package com.hit.playpal.paginatedsearch.rooms.adapters;

import com.hit.playpal.entities.chats.ChatRoom;

public interface IRoomAdapter<T> {
    void onRoomClick(ChatRoom iChatRoom);
}
