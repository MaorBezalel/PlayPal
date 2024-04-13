package com.hit.playpal.chatrooms.domain.utils.exceptions;

public class RoomNotFoundException extends Exception {
    public RoomNotFoundException() {
        super("Room not found");
    }
}
