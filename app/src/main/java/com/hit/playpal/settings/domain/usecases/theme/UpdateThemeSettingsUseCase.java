package com.hit.playpal.settings.domain.usecases.theme;

import com.hit.playpal.settings.domain.repositories.IThemeSettingsRepository;
import users.enums.ThemePolicy;

public class UpdateThemeSettingsUseCase {
    private IThemeSettingsRepository mThemeSettingsRepository;

    public void execute(ThemePolicy iNewThemePolicy) {
        mThemeSettingsRepository.updateThemeSettingsForThisAccount(iNewThemePolicy);
    }
}
