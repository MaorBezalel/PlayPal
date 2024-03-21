package com.hit.playpal.data.models;

import com.google.firebase.firestore.PropertyName;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

public class UserPublicProfile {
    @PropertyName("about_me")
    private String mAboutMe;

    @PropertyName("joined_groups")
    private List<JoinedGroups> mJoinedGroups;

    public UserPublicProfile() { }

    public UserPublicProfile(
            String iAboutMe,
            List<JoinedGroups> iJoinedGroups
    ) {
        mAboutMe = iAboutMe;
        mJoinedGroups = iJoinedGroups;
    }

    @PropertyName("about_me") public String getAboutMe() {
        return mAboutMe;
    }
    @PropertyName("about_me") public void setAboutMe(String iAboutMe) {
        mAboutMe = iAboutMe;
    }

    @PropertyName("joined_groups") public List<JoinedGroups> getJoinedGroups() {
        return mJoinedGroups;
    }
    @PropertyName("joined_groups") public void setJoinedGroups(List<JoinedGroups> iJoinedGroups) {
        mJoinedGroups = iJoinedGroups;
    }
}
