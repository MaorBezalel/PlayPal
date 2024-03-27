package com.hit.playpal.settings.domain.usecases.account;

import com.hit.playpal.settings.domain.repositories.IAccountSettingsRepository;

public class UpdatePasswordUseCase {
    private IAccountSettingsRepository mAccountSettingsRepository;

    public void execute(String iNewPassword) {
        mAccountSettingsRepository.updateThePasswordOfThisAccount(iNewPassword);
    }
}
