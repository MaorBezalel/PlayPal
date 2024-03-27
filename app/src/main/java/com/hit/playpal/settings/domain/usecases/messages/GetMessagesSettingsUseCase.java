package com.hit.playpal.settings.domain.usecases.messages;

import com.hit.playpal.settings.domain.repositories.IMessagesSettingsRepository;
import com.hit.playpal.entities.users.enums.MessagesPolicy;

public class GetMessagesSettingsUseCase {
    private IMessagesSettingsRepository mMessagesSettingsRepository;

    public MessagesPolicy execute() {
        return mMessagesSettingsRepository.getMessagesSettingsForThisAccount();
    }
}
