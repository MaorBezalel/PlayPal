package com.hit.playpal.settings.domain.usecases.account;

import com.hit.playpal.settings.domain.repositories.IAccountSettingsRepository;

public class HandleLogoutUseCase {
    private IAccountSettingsRepository mAccountSettingsRepository;

    public void execute() {
        mAccountSettingsRepository.logoutThisAccount();
    }
}
