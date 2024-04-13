package com.hit.playpal.chatrooms.domain.utils.exceptions;

public class MessagesNotFoundException extends Exception{
    public MessagesNotFoundException() {
        super("Messages not found in the chat room.");
    }
}
