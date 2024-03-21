package com.hit.playpal.data.models;

import com.google.firebase.firestore.PropertyName;

public class User {
    @PropertyName("uid")
    private String mUid;

    @PropertyName("username")
    private String mUsername;

    @PropertyName("profile_image_url")
    private String mProfileImageUrl;

    public User() {}

    public User(
            String iUid,
            String iUsername,
            String iProfileImageUrl
    ) {
        mUid = iUid;
        mUsername = iUsername;
        mProfileImageUrl = iProfileImageUrl;
    }

    @PropertyName("uid") public String getUid() {
        return mUid;
    }
    @PropertyName("uid") public void setUid(String iUid) {
        mUid = iUid;
    }

    @PropertyName("username") public String getUsername() {
        return mUsername;
    }
    @PropertyName("username") public void setUsername(String iUsername) {
        mUsername = iUsername;
    }

    @PropertyName("profile_image_url") public String getProfileImageUrl() {
        return mProfileImageUrl;
    }
    @PropertyName("profile_image_url") public void setProfileImageUrl(String iProfileImageUrl) {
        mProfileImageUrl = iProfileImageUrl;
    }
}
