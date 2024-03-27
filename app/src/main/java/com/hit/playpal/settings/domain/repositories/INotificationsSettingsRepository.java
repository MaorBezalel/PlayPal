package com.hit.playpal.settings.domain.repositories;

import users.enums.NotificationPolicy;

import java.util.HashMap;

public interface INotificationsSettingsRepository {
    HashMap<NotificationPolicy, Boolean> getNotificationsSettingsForThisAccount();
    void updateNotificationsSettingsForThisAccount(HashMap<NotificationPolicy, Boolean> iNewNotificationPolicy);
}
