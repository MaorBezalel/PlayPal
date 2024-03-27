package com.hit.playpal.entities.chats;

import com.hit.playpal.entities.users.User;

import java.util.HashMap;
import java.util.List;

public class OneToOneChat extends Chat {

    private List<User> mUserDetails;

    public OneToOneChat() {
    }

    public OneToOneChat(List<User> iUserDetails) {
        this.mUserDetails = iUserDetails;
    }

    public List<User> getProfilePictures() {
        return mUserDetails;
    }

    public void setProfilePictures(List<User> iUserDetails) {
        this.mUserDetails = iUserDetails;
    }
}
