package com.hit.playpal.settings.domain.usecases.theme;

import com.hit.playpal.settings.domain.repositories.IThemeSettingsRepository;
import users.enums.ThemePolicy;

public class GetThemeSettingsUseCase {
    private IThemeSettingsRepository mThemeSettingsRepository;

    public ThemePolicy execute() {
        return mThemeSettingsRepository.getThemeSettingsForThisAccount();
    }
}
