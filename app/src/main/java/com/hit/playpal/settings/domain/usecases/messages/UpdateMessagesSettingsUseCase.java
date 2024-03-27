package com.hit.playpal.settings.domain.usecases.messages;

import com.hit.playpal.settings.domain.repositories.IMessagesSettingsRepository;
import users.enums.MessagesPolicy;

public class UpdateMessagesSettingsUseCase {
    private IMessagesSettingsRepository mMessagesSettingsRepository;

    public void execute(MessagesPolicy iNewMessagePolicy) {
        mMessagesSettingsRepository.updateMessagesSettingsForThisAccount(iNewMessagePolicy);
    }
}
