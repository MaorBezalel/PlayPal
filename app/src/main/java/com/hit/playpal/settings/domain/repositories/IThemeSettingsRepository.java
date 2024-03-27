package com.hit.playpal.settings.domain.repositories;

import users.enums.ThemePolicy;

public interface IThemeSettingsRepository {
    ThemePolicy getThemeSettingsForThisAccount();
    void updateThemeSettingsForThisAccount(ThemePolicy iNewThemePolicy);
}
