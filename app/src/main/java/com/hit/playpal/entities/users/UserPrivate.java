package com.hit.playpal.entities.users;

import com.google.firebase.firestore.PropertyName;

/**
 * Entity class that represents a user's private information.
 */
public class UserPrivate {
    @PropertyName("email") private String mEmail;
    @PropertyName("email") public String getEmail() { return mEmail; }
    @PropertyName("email") public void setEmail(String iEmail) { mEmail = iEmail; }

    @PropertyName("password") private String mPassword;
    @PropertyName("password") public String getPassword() { return mPassword; }
    @PropertyName("password") public void setPassword(String iPassword) { mPassword = iPassword; }

    public UserPrivate() { }
    public UserPrivate(String iEmail, String iPassword) {
        mEmail = iEmail;
        mPassword = iPassword;
    }
}
