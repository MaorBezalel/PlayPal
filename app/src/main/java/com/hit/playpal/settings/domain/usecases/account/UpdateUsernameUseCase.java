package com.hit.playpal.settings.domain.usecases.account;

import com.hit.playpal.settings.domain.repositories.IAccountSettingsRepository;

public class UpdateUsernameUseCase {
    private IAccountSettingsRepository mAccountSettingsRepository;

    public void execute(String iNewUsername) {
        mAccountSettingsRepository.updateTheUsernameOfThisAccount(iNewUsername);
    }
}
