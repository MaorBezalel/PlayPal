package com.hit.playpal.entities.users;

import com.google.firebase.firestore.PropertyName;

public class User {
    @PropertyName("uid") private String mUid;
    @PropertyName("uid") public String getUid() { return mUid; }
    @PropertyName("uid") public void setUid(String iUid) { mUid = iUid; }

    @PropertyName("user_name") private String mUsername;
    @PropertyName("user_name") public String getUsername() { return mUsername; }
    @PropertyName("user_name") public void setUsername(String iUsername) { mUsername = iUsername; }

    @PropertyName("display_name") private String mDisplayName;
    @PropertyName("display_name") public String getDisplayName() { return mDisplayName; }
    @PropertyName("display_name") public void setDisplayName(String iDisplayName) { mDisplayName = iDisplayName; }

    @PropertyName("profile_picture") private String mProfilePicture;
    @PropertyName("profile_picture") public String getProfilePicture() { return mProfilePicture; }
    @PropertyName("profile_picture") public void setProfilePicture(String iProfilePicture) { mProfilePicture = iProfilePicture; }

    @PropertyName("about_me") private String mAboutMe;
    @PropertyName("about_me") public String getAboutMe() { return mAboutMe; }
    @PropertyName("about_me") public void setAboutMe(String iAboutMe) { mAboutMe = iAboutMe; }

    public User() { }
    public User(String iUid, String iUsername, String iDisplayName, String iProfilePicture, String iAboutMe) {
        mUid = iUid;
        mUsername = iUsername;
        mDisplayName = iDisplayName;
        mProfilePicture = iProfilePicture;
        mAboutMe = iAboutMe;
    }
}
