package com.hit.playpal.paginatedsearch.users.utils;

public interface IBindableUser<T> {
    String getUserId(T iItem);
    String getUserImage(T iItem);
    String getUsername(T iItem);
}
