package com.hit.playpal.settings.domain.usecases.account;

import com.hit.playpal.settings.domain.repositories.IAccountSettingsRepository;
import com.hit.playpal.entities.users.User;

import java.util.List;

public class GetBlockedUsersUseCase {
    IAccountSettingsRepository mAccountSettingsRepository;

    public List<User> execute(int iPage) {
        return mAccountSettingsRepository.getTheBlockedUsersOfThisAccount(iPage);
    }
}
