package com.hit.playpal.entities.chats;

import com.google.firebase.firestore.PropertyName;
import com.hit.playpal.entities.chats.enums.UserChatRole;

public class Participant {
    @PropertyName("uid") private String mUserUid;
    @PropertyName("display_name") private String mDisplayName;
    @PropertyName("profile_image") private String mProfilePicture;
    @PropertyName("chat_role") private UserChatRole mUserChatRole;

    public Participant() {
    }

    public Participant(String iUserUid, String iDisplayName, String iProfilePicture, UserChatRole iUserChatRole) {
        this.mUserUid = iUserUid;
        this.mDisplayName = iDisplayName;
        this.mProfilePicture = iProfilePicture;
        this.mUserChatRole = iUserChatRole;
    }

    @PropertyName("uid")  public String getUserUid() {
        return mUserUid;
    }

    @PropertyName("uid")  public void setUserUid(String iUserUid) {
        mUserUid = iUserUid;
    }

    @PropertyName("display_name") public String getDisplayName() {
        return mDisplayName;
    }

    @PropertyName("display_name") public void setDisplayName(String iDisplayName) {
        mDisplayName = iDisplayName;
    }

    @PropertyName("user_profile") public String getProfilePicture() {
        return mProfilePicture;
    }

    @PropertyName("user_profile") public void setProfilePicture(String iProfilePicture) {
        mProfilePicture = iProfilePicture;
    }

    @PropertyName("chat_role")  public UserChatRole getUserChatRole() {
        return mUserChatRole;
    }

    @PropertyName("chat_role")  public void setUserChatRole(UserChatRole iUserChatRole) {
        mUserChatRole = iUserChatRole;
    }
}
