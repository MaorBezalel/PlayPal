package com.hit.playpal.settings.domain.usecases.theme;

import com.hit.playpal.settings.domain.repositories.IThemeSettingsRepository;
import com.hit.playpal.entities.users.enums.ThemePolicy;

public class UpdateThemeSettingsUseCase {
    private IThemeSettingsRepository mThemeSettingsRepository;

    public void execute(ThemePolicy iNewThemePolicy) {
        mThemeSettingsRepository.updateThemeSettingsForThisAccount(iNewThemePolicy);
    }
}
