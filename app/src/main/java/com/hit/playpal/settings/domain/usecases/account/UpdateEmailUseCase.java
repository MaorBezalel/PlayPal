package com.hit.playpal.settings.domain.usecases.account;

import com.hit.playpal.settings.domain.repositories.IAccountSettingsRepository;

public class UpdateEmailUseCase {
    private IAccountSettingsRepository mAccountSettingsRepository;

    public void execute(String iNewEmail) {
        mAccountSettingsRepository.updateTheEmailOfThisAccount(iNewEmail);
    }
}
