package com.hit.playpal.chatrooms.domain.utils.exceptions;

public class MessagesNotFound extends Exception{
    public MessagesNotFound() {
        super("Messages not found in the chat room.");
    }
}
