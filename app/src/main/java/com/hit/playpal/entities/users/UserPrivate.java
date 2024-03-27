package com.hit.playpal.entities.users;

public class UserPrivate {
    private String mEmail;
    public String getEmail() { return mEmail; }
    public void setEmail(String iEmail) { mEmail = iEmail; }

    private String mPassword;
    public String getPassword() { return mPassword; }
    public void setPassword(String iPassword) { mPassword = iPassword; }

    public UserPrivate() { }
    public UserPrivate(String iEmail, String iPassword) {
        mEmail = iEmail;
        mPassword = iPassword;
    }
}
