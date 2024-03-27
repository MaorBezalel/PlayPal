package com.hit.playpal.settings.domain.usecases.account;

import com.hit.playpal.settings.domain.repositories.IAccountSettingsRepository;

public class AddToTheBlockedListUseCase {
    IAccountSettingsRepository mAccountSettingsRepository;

    public void execute(String iUsername) {
        mAccountSettingsRepository.AddUserToTheBlockedListOfThisAccount(iUsername);
    }
}
