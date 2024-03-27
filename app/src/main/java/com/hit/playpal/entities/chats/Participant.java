package com.hit.playpal.entities.chats;

import com.hit.playpal.entities.chats.enums.UserChatRole;

public class Participant {
    private String mUserUid;
    private String mDisplayName;
    private String mProfilePicture;
    private UserChatRole mUserChatRole;

    public Participant() {
    }

    public Participant(String iUserUid, String iDisplayName, String iProfilePicture, UserChatRole iUserChatRole) {
        this.mUserUid = iUserUid;
        this.mDisplayName = iDisplayName;
        this.mProfilePicture = iProfilePicture;
        this.mUserChatRole = iUserChatRole;
    }

    public String getUserUid() {
        return mUserUid;
    }

    public void setUserUid(String iUserUid) {
        mUserUid = iUserUid;
    }

    public String getDisplayName() {
        return mDisplayName;
    }

    public void setDisplayName(String iDisplayName) {
        mDisplayName = iDisplayName;
    }

    public String getProfilePicture() {
        return mProfilePicture;
    }

    public void setProfilePicture(String iProfilePicture) {
        mProfilePicture = iProfilePicture;
    }

    public UserChatRole getUserChatRole() {
        return mUserChatRole;
    }

    public void setUserChatRole(UserChatRole iUserChatRole) {
        mUserChatRole = iUserChatRole;
    }
}
