package com.hit.playpal.data.models;

import com.google.firebase.firestore.PropertyName;

public class UserPrivateSettings {
    @PropertyName("password")
    private String mPassword;
    @PropertyName("email")
    private String mEmail;

    public UserPrivateSettings() {}

    public UserPrivateSettings(String iPassword, String iEmail) {
        mPassword = iPassword;
        mEmail = iEmail;
    }

    @PropertyName("password") public String getPassword() {
        return mPassword;
    }
    @PropertyName("password") public void setPassword(String iPassword) {
        mPassword = iPassword;
    }

    @PropertyName("email") public String getEmail() {
        return mEmail;
    }
    @PropertyName("email") public void setEmail(String iEmail) {
        mEmail = iEmail;
    }
}
