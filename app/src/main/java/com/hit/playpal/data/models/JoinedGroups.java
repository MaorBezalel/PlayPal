package com.hit.playpal.data.models;

import com.google.firebase.firestore.PropertyName;

public class JoinedGroups {
    @PropertyName("group_id")
    private String mGroupId;

    @PropertyName("group_name")
    private String mGroupName;

    @PropertyName("group_image_url")
    private String mGroupImageUrl;

    public JoinedGroups() {}

    public JoinedGroups(
            String iGroupId,
            String iGroupName,
            String iGroupImageUrl
    ) {
        mGroupId = iGroupId;
        mGroupName = iGroupName;
        mGroupImageUrl = iGroupImageUrl;
    }

    @PropertyName("group_id") public String getGroupId() {
        return mGroupId;
    }
    @PropertyName("group_id") public void setGroupId(String iGroupId) {
        mGroupId = iGroupId;
    }

    @PropertyName("group_name") public String getGroupName() {
        return mGroupName;
    }
    @PropertyName("group_name") public void setGroupName(String iGroupName) {
        mGroupName = iGroupName;
    }

    @PropertyName("group_image_url") public String getGroupImageUrl() {
        return mGroupImageUrl;
    }
    @PropertyName("group_image_url") public void setGroupImageUrl(String iGroupImageUrl) {
        mGroupImageUrl = iGroupImageUrl;
    }
}
