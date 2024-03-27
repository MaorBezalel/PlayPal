package com.hit.playpal.settings.domain.repositories;

import com.hit.playpal.entities.users.enums.MessagesPolicy;

public interface IMessagesSettingsRepository {
    MessagesPolicy getMessagesSettingsForThisAccount();
    void updateMessagesSettingsForThisAccount(MessagesPolicy iNewMessagePolicy);
}
