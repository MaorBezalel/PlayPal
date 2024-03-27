package com.hit.playpal.entities.games;

import com.hit.playpal.entities.games.enums.Genre;
import com.hit.playpal.entities.games.enums.Platform;

import java.util.Date;
import java.util.List;

public class Game {
    private String mGameId;
    public String getGameId() { return mGameId; }
    public void setGameId(String iGameId) { mGameId = iGameId; }

    private String mGameName;
    public String getGameName() { return mGameName; }
    public void setGameName(String iGameName) { mGameName = iGameName; }

    private String mBackgroundImage;
    public String getBackgroundImage() { return mBackgroundImage; }
    public void setBackgroundImage(String iBackgroundImage) { mBackgroundImage = iBackgroundImage; }

    private List<Genre> mGenres;
    public List<Genre> getGenres() { return mGenres; }
    public void setGenres(List<Genre> iGenres) { mGenres = iGenres; }

    private List<Platform> mPlatforms;
    public List<Platform> getPlatforms() { return mPlatforms; }
    public void setPlatforms(List<Platform> iPlatforms) { mPlatforms = iPlatforms; }

    private float mRating;
    public float getRating() { return mRating; }
    public void setRating(float iRating) { mRating = iRating; }

    private Date mReleaseDate;
    public Date getReleaseDate() { return mReleaseDate; }
    public void setReleaseDate(Date iReleaseDate) { mReleaseDate = iReleaseDate; }

    public Game() { }
    public Game(
            String iGameId,
            String iGameName,
            String iBackgroundImage,
            List<Genre> iGenres,
            List<Platform> iPlatforms,
            float iRating,
            Date iReleaseDate)  {
        mGameId = iGameId;
        mGameName = iGameName;
        mBackgroundImage = iBackgroundImage;
        mGenres = iGenres;
        mPlatforms = iPlatforms;
        mRating = iRating;
        mReleaseDate = iReleaseDate;
    }
}
