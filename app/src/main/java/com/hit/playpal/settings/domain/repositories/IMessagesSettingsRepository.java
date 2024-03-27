package com.hit.playpal.settings.domain.repositories;

import users.enums.MessagesPolicy;

public interface IMessagesSettingsRepository {
    MessagesPolicy getMessagesSettingsForThisAccount();
    void updateMessagesSettingsForThisAccount(MessagesPolicy iNewMessagePolicy);
}
