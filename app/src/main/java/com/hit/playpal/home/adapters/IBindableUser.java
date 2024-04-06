package com.hit.playpal.home.adapters;

public interface IBindableUser<T> {
    String getUserId(T iItem);
    String getUserImage(T iItem);
    String getDisplayName(T iItem);
}
