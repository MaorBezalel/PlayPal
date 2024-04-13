package com.hit.playpal.entities.users;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.PropertyName;

import org.jetbrains.annotations.Contract;

/**
 * Entity class that represents a user.
 */
public class User implements Parcelable {
    @PropertyName("uid") private String mUid; // TODO: change to @DocumentId ???
    @PropertyName("uid") public String getUid() { return mUid; }
    @PropertyName("uid") public void setUid(String iUid) { mUid = iUid; }

    @PropertyName("username") private String mUsername;
    @PropertyName("username") public String getUsername() { return mUsername; }
    @PropertyName("username") public void setUsername(String iUsername) { mUsername = iUsername; }

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
    public User(String iUsername, String iDisplayName) {
        mUid = null;
        mUsername = iUsername;
        mDisplayName = iDisplayName;
        mProfilePicture = "";
        mAboutMe = "";
    }
    public User(String iUid, String iUsername, String iDisplayName) {
        mUid = iUid;
        mUsername = iUsername;
        mDisplayName = iDisplayName;
        mProfilePicture = "";
        mAboutMe = "";
    }
   public User(String iUid, String iUsername, String iDisplayName, String iProfilePicture, String iAboutMe) {
        mUid = iUid;
        mUsername = iUsername;
        mDisplayName = iDisplayName;
        mProfilePicture = iProfilePicture;
        mAboutMe = iAboutMe;
    }

    protected User(@NonNull Parcel in) {
        mUid = in.readString();
        mUsername = in.readString();
        mDisplayName = in.readString();
        mProfilePicture = in.readString();
        mAboutMe = in.readString();
    }

    public static final Creator<User> CREATOR = new Creator<User>() {
        @NonNull
        @Contract("_ -> new")
        @Override
        public User createFromParcel(Parcel in) {
            return new User(in);
        }

        @NonNull
        @Contract(value = "_ -> new", pure = true)
        @Override
        public User[] newArray(int size) {
            return new User[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(@NonNull Parcel dest, int flags) {
        dest.writeString(mUid);
        dest.writeString(mUsername);
        dest.writeString(mDisplayName);
        dest.writeString(mProfilePicture);
        dest.writeString(mAboutMe);
    }
}
