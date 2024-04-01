package com.hit.playpal.entities.users;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.PropertyName;
import com.hit.playpal.entities.users.enums.MessagesPolicy;
import com.hit.playpal.entities.users.enums.NotificationPolicy;
import com.hit.playpal.entities.users.enums.ThemePolicy;

import org.jetbrains.annotations.Contract;

import java.util.HashMap;

public class Settings {

    @PropertyName("message_policy") private MessagesPolicy mMessagePolicy;
    @PropertyName("message_policy") public MessagesPolicy getMessagePolicy() { return mMessagePolicy; }
    @PropertyName("message_policy") public void setMessagePolicy(MessagesPolicy iMessagePolicy) { mMessagePolicy = iMessagePolicy; }

    @PropertyName("theme_policy") private ThemePolicy mThemePolicy;
    @PropertyName("theme_policy") public ThemePolicy getThemePolicy() { return mThemePolicy; }
    @PropertyName("theme_policy") public void setThemePolicy(ThemePolicy iThemePolicy) { mThemePolicy = iThemePolicy; }


    @PropertyName("notification_policy") private HashMap<String, Boolean> mNotificationPolicies;
    @PropertyName("notification_policy") public HashMap<String, Boolean> getNotificationPolicy() { return mNotificationPolicies; }
    @PropertyName("notification_policy") public void setNotificationPolicy(HashMap<String, Boolean> iNotificationPolicies) { mNotificationPolicies = iNotificationPolicies; }

    public Settings() { }
    public Settings(MessagesPolicy iMessagePolicy, ThemePolicy iThemePolicy, HashMap<String, Boolean> iNotificationPolicies) {
        mMessagePolicy = iMessagePolicy;
        mThemePolicy = iThemePolicy;
        mNotificationPolicies = iNotificationPolicies;
    }

    @NonNull
    @Contract(" -> new")
    public static Settings getDefaultSettings() {
        return new Settings(MessagesPolicy.EVERYONE, ThemePolicy.SYSTEM, new HashMap<String, Boolean>() {{
            put(NotificationPolicy.FRIENDSHIP_ACCEPTED.name(), true);
            put(NotificationPolicy.GROUP_INVITATION.name(), true);
            put(NotificationPolicy.RECENT_MESSAGE_IN_GROUP.name(), true);
            put(NotificationPolicy.RECENT_MESSAGE_IN_PRIVATE_CHAT.name(), true);
            put(NotificationPolicy.KICKED_OUT_OF_GROUP.name(), true);
        }});
    }
}
