package com.hit.playpal.entities.chats;

public class GroupDetails {
    private String mGroupPicture;
    private String mGroupName;

    public GroupDetails() {
    }

    public GroupDetails(String iGroupPicture, String iGroupName) {
        this.mGroupPicture = iGroupPicture;
        this.mGroupName = iGroupName;
    }

    public String getGroupPicture() {
        return mGroupPicture;
    }

    public void setGroupPicture(String iGroupPicture) {
        this.mGroupPicture = iGroupPicture;
    }

    public String getGroupName() {
        return mGroupName;
    }

    public void setGroupName(String iGroupName) {
        this.mGroupName = iGroupName;
    }
}
