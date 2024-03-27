package com.hit.playpal.settings.domain.usecases.account;

import com.hit.playpal.settings.domain.repositories.IAccountSettingsRepository;

public class UpdateDisplayNameUseCase {
    private IAccountSettingsRepository mAccountSettingsRepository;

    public void execute(String iNewDisplayName) {
        mAccountSettingsRepository.updateTheDisplayNameOfThisAccount(iNewDisplayName);
    }
}
