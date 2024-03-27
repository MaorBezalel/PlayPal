package com.hit.playpal.settings.domain.repositories;

import com.hit.playpal.entities.users.enums.ThemePolicy;

public interface IThemeSettingsRepository {
    ThemePolicy getThemeSettingsForThisAccount();
    void updateThemeSettingsForThisAccount(ThemePolicy iNewThemePolicy);
}
