package com.hit.playpal.settings.domain.usecases.account;

import com.hit.playpal.settings.domain.repositories.IAccountSettingsRepository;

public class GetEmailUseCase {
    private IAccountSettingsRepository mAccountSettingsRepository;

    public String execute() {
        return mAccountSettingsRepository.getTheEmailOfThisAccount();
    }
}
