package com.hit.playpal.paginatedsearch.rooms.utils;

public interface IBindableRoom<T> {
    String getRoomName(T item);

    String getRoomId(T item);

    String getRoomImage(T item);
    String getGameImage(T item);
    int getMembersCount(T item);
}
