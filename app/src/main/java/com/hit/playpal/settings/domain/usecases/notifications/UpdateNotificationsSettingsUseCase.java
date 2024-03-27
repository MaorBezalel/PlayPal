package com.hit.playpal.settings.domain.usecases.notifications;

import com.hit.playpal.settings.domain.repositories.INotificationsSettingsRepository;

import com.hit.playpal.entities.users.enums.NotificationPolicy;

import java.util.HashMap;

public class UpdateNotificationsSettingsUseCase {
    private INotificationsSettingsRepository mNotificationsSettingsRepository;

    public void execute(HashMap<NotificationPolicy, Boolean> iNewNotificationPolicy) {
        mNotificationsSettingsRepository.updateNotificationsSettingsForThisAccount(iNewNotificationPolicy);
    }
}
