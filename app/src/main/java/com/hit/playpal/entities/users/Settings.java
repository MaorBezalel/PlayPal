package com.hit.playpal.entities.users;

import com.hit.playpal.entities.users.enums.MessagesPolicy;
import com.hit.playpal.entities.users.enums.NotificationPolicy;
import com.hit.playpal.entities.users.enums.ThemePolicy;

import java.util.HashMap;

public class Settings {
    private MessagesPolicy mMessagePolicy;
    public MessagesPolicy getMessagePolicy() { return mMessagePolicy; }
    public void setMessagePolicy(MessagesPolicy iMessagePolicy) { mMessagePolicy = iMessagePolicy; }

    private ThemePolicy mThemePolicy;
    public ThemePolicy getThemePolicy() { return mThemePolicy; }
    public void setThemePolicy(ThemePolicy iThemePolicy) { mThemePolicy = iThemePolicy; }

    private HashMap<NotificationPolicy, Boolean> mNotificationPolicies;
    public HashMap<NotificationPolicy, Boolean> getNotificationPolicy() { return mNotificationPolicies; }
    public void setNotificationPolicy(HashMap<NotificationPolicy, Boolean> iNotificationPolicies) { mNotificationPolicies = iNotificationPolicies; }

    public Settings() { }
    public Settings(MessagesPolicy iMessagePolicy, HashMap<NotificationPolicy, Boolean> iNotificationPolicies) {
        mMessagePolicy = iMessagePolicy;
        mNotificationPolicies = iNotificationPolicies;
    }
}
