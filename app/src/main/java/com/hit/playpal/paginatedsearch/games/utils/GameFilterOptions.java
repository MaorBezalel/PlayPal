package com.hit.playpal.paginatedsearch.games.utils;

import com.hit.playpal.entities.games.enums.Genre;
import com.hit.playpal.entities.games.enums.Platform;

import java.util.List;
import java.util.Objects;

public class GameFilterOptions {
    private String mGameName;
    private List<Genre> mGenre;
    private List<Platform> mPlatform;

    public GameFilterOptions() { }

    public GameFilterOptions(String iGameName, Genre iGenre, Platform iPlatform)
    {

    }

    public String getGameName() {
        return mGameName;
    }

    public void setGameName(String mGameName) {
        this.mGameName = mGameName;
    }

    public List<Genre> getGenre() {
        return mGenre;
    }

    public void setGenre(List<Genre> mGenre) {
        this.mGenre = mGenre;
    }

    public List<Platform> getPlatform() {
        return mPlatform;
    }

    public void setPlatform(List<Platform> mPlatform) {
        this.mPlatform = mPlatform;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        GameFilterOptions other = (GameFilterOptions) o;


        return Objects.equals(mGameName, other.mGameName) &&
                Objects.deepEquals(mGenre, other.mGenre) &&
                Objects.deepEquals(mPlatform, other.mPlatform);
    }


    @Override
    public int hashCode() {
        return Objects.hash(mGameName, mGenre, mPlatform);
    }
}
