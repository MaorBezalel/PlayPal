package com.hit.playpal.data.models;

import com.google.firebase.firestore.PropertyName;

public class UserPreferencesSettings {
    @PropertyName("is_dark_mode")
    private boolean mIsDarkMode;
    @PropertyName("can_only_be_messaged_by_friends")
    private boolean mCanOnlyBeMessagedByFriends;

    public UserPreferencesSettings() {}

    public UserPreferencesSettings(boolean iIsDarkMode, boolean iCanOnlyBeMessagedByFriends) {
        mIsDarkMode = iIsDarkMode;
        mCanOnlyBeMessagedByFriends = iCanOnlyBeMessagedByFriends;
    }

    @PropertyName("is_dark_mode") public boolean getIsDarkMode() {
        return mIsDarkMode;
    }
    @PropertyName("is_dark_mode") public void setIsDarkMode(boolean iIsDarkMode) {
        mIsDarkMode = iIsDarkMode;
    }

    @PropertyName("can_only_be_messaged_by_friends") public boolean getCanOnlyBeMessagedByFriends() {
        return mCanOnlyBeMessagedByFriends;
    }
    @PropertyName("can_only_be_messaged_by_friends") public void setCanOnlyBeMessagedByFriends(boolean iCanOnlyBeMessagedByFriends) {
        mCanOnlyBeMessagedByFriends = iCanOnlyBeMessagedByFriends;
    }
}
