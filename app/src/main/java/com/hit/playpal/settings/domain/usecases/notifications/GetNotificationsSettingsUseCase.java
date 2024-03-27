package com.hit.playpal.settings.domain.usecases.notifications;

import com.hit.playpal.settings.domain.repositories.INotificationsSettingsRepository;

import com.hit.playpal.entities.users.enums.NotificationPolicy;

import java.util.HashMap;

public class GetNotificationsSettingsUseCase {
    private INotificationsSettingsRepository mNotificationsSettingsRepository;

    public HashMap<NotificationPolicy, Boolean> execute() {
        return mNotificationsSettingsRepository.getNotificationsSettingsForThisAccount();
    }
}
