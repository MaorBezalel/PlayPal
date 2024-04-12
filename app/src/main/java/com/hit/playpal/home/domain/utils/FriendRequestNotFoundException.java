package com.hit.playpal.home.domain.utils;

public class FriendRequestNotFoundException extends Exception{
    public FriendRequestNotFoundException() {
        super("Friend request not found!");
    }
}
