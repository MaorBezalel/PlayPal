package com.hit.playpal.paginatedsearch.rooms.utils;

import com.hit.playpal.entities.chats.ChatRoom;

public interface IBindableRoom<T> {
    String getRoomName(T item);

    String getRoomId(T item);

    String getRoomImage(T item);
    String getGameImage(T item);
    int getMembersCount(T item);

    ChatRoom getChatRoom(T item);
}
