package com.hit.playpal.data.models;

import com.google.firebase.firestore.PropertyName;

public class Messenger {
    @PropertyName("end_uid")
    private String mEndUid;

    @PropertyName("end_username")
    private String mEndUsername;

    @PropertyName("end_profile_image_url")
    private String mEndProfileImageUrl;

    public Messenger() {}

    public Messenger(String iEndUid, String iEndUsername, String iEndProfileImageUrl) {
        mEndUid = iEndUid;
        mEndUsername = iEndUsername;
        mEndProfileImageUrl = iEndProfileImageUrl;
    }

    @PropertyName("end_uid") public String getEndUid() {
        return mEndUid;
    }
    @PropertyName("end_uid") public void setEndUid(String iEndUid) {
        mEndUid = iEndUid;
    }

    @PropertyName("end_username") public String getEndUsername() {
        return mEndUsername;
    }
    @PropertyName("end_username") public void setEndUsername(String iEndUsername) {
        mEndUsername = iEndUsername;
    }

    @PropertyName("end_profile_image_url") public String getEndProfileImageUrl() {
        return mEndProfileImageUrl;
    }
    @PropertyName("end_profile_image_url") public void setEndProfileImageUrl(String iEndProfileImageUrl) {
        mEndProfileImageUrl = iEndProfileImageUrl;
    }
}
