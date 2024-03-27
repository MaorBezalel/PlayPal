package com.hit.playpal.entities.chats;

import com.hit.playpal.entities.games.Game;

import java.util.HashMap;

public class GroupChat extends Chat {

    public GroupDetails mGroupDetails;
    public Game mGameDetails;

    public GroupChat() {
    }

    public GroupChat(GroupDetails iGroupDetails, Game iGameDetails) {
        this.mGroupDetails = iGroupDetails;
        this.mGameDetails = iGameDetails;
    }

    public GroupDetails getGroupDetails() {
        return mGroupDetails;
    }

    public void setGroupDetails(GroupDetails iGroupDetails) {
        this.mGroupDetails = iGroupDetails;
    }

    public Game getGameDetails() {
        return mGameDetails;
    }

    public void setGameDetails(Game iGameDetails) {
        this.mGameDetails = iGameDetails;
    }



}
