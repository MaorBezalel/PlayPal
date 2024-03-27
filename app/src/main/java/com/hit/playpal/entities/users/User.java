package com.hit.playpal.entities.users;

public class User {
    private String mUid;
    public String getUid() { return mUid; }
    public void setUid(String iUid) { mUid = iUid; }

    private String mUsername;
    public String getUsername() { return mUsername; }
    public void setUsername(String iUsername) { mUsername = iUsername; }

    private String mDisplayName;
    public String getDisplayName() { return mDisplayName; }
    public void setDisplayName(String iDisplayName) { mDisplayName = iDisplayName; }

    private String mProfilePicture;
    public String getProfilePicture() { return mProfilePicture; }
    public void setProfilePicture(String iProfilePicture) { mProfilePicture = iProfilePicture; }

    private String mAboutMe;
    public String getAboutMe() { return mAboutMe; }
    public void setAboutMe(String iAboutMe) { mAboutMe = iAboutMe; }

    public User() { }
    public User(String iUid, String iUsername, String iDisplayName, String iProfilePicture, String iAboutMe) {
        mUid = iUid;
        mUsername = iUsername;
        mDisplayName = iDisplayName;
        mProfilePicture = iProfilePicture;
        mAboutMe = iAboutMe;
    }
}
