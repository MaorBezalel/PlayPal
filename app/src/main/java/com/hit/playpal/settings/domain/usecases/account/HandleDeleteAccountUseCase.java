package com.hit.playpal.settings.domain.usecases.account;

import com.hit.playpal.settings.domain.repositories.IAccountSettingsRepository;

public class HandleDeleteAccountUseCase {
    public IAccountSettingsRepository mAccountSettingsRepository;

    public void execute() {
        mAccountSettingsRepository.deleteThisAccount();
    }
}
