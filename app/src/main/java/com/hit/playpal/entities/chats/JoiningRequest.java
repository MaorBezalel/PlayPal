package com.hit.playpal.entities.chats;

import com.hit.playpal.entities.users.User;

import java.util.Date;

public class JoiningRequest {
    private User mRequestUser;
    private Date mSentAt;

    public JoiningRequest() {}

    public JoiningRequest(User iRequestUser, Date iSentAt) {
        mRequestUser = iRequestUser;
        mSentAt = iSentAt;
    }

    public User getRequestUser() {
        return mRequestUser;
    }

    public void setRequestUser(User iRequestUser) {
        this.mRequestUser = iRequestUser;
    }

    public Date getSentAt() {
        return mSentAt;
    }

    public void setSentAt(Date iSentAt) {
        this.mSentAt = iSentAt;
    }
}
