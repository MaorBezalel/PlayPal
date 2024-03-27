package com.hit.playpal.home.domain.util;

import com.hit.playpal.entities.games.enums.Genre;
import com.hit.playpal.entities.games.enums.Platform;

public class GameFilterOptions {
    private String mGameName;
    private Genre mGenre;
    private Platform mPlatform;
    private boolean mExactMatching; // searching for exact game name or contains game name

    public GameFilterOptions() { }

    public GameFilterOptions(String iGameName, Genre iGenre, Platform iPlatform, boolean iExactMatching)
    {

    }
}
