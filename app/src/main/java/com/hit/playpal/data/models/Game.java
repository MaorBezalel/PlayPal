package com.hit.playpal.data.models;

import com.google.firebase.firestore.PropertyName;
import com.hit.playpal.data.enums.Genre;
import com.hit.playpal.data.enums.Platform;

import java.util.Date;
import java.util.List;

public class Game {
    @PropertyName("game_id")
    private String mGameId;

    @PropertyName("game_name")
    private String mGameName;

    @PropertyName("release_date")
    private Date mReleaseDate;

    @PropertyName("background_image")
    private String mBackgroundImage;

    @PropertyName("rating")
    private float mRating;

    @PropertyName("platforms")
    private List<Platform> mPlatforms;

    @PropertyName("genres")
    private List<Genre> mGenres;

    public Game() {}

    public Game(
            String iGameId,
            String iGameName,
            Date iReleaseDate,
            String iBackgroundImage,
            float iRating,
            List<Platform> iPlatforms,
            List<Genre> iGenres
    ) {
        mGameId = iGameId;
        mGameName = iGameName;
        mReleaseDate = iReleaseDate;
        mBackgroundImage = iBackgroundImage;
        mRating = iRating;
        mPlatforms = iPlatforms;
        mGenres = iGenres;
    }

    @PropertyName("game_id") public String getGameId() {
        return mGameId;
    }
    @PropertyName("game_id") public void setGameId(String iGameId) {
        mGameId = iGameId;
    }

    @PropertyName("game_name") public String getGameName() {
        return mGameName;
    }
    @PropertyName("game_name") public void setGameName(String iGameName) {
        mGameName = iGameName;
    }

    @PropertyName("release_date") public Date getReleaseDate() {
        return mReleaseDate;
    }
    @PropertyName("release_date") public void setReleaseDate(Date iReleaseDate) {
        mReleaseDate = iReleaseDate;
    }

    @PropertyName("background_image") public String getBackgroundImage() {
        return mBackgroundImage;
    }
    @PropertyName("background_image") public void setBackgroundImage(String iBackgroundImage) {
        mBackgroundImage = iBackgroundImage;
    }

    @PropertyName("rating") public float getRating() {
        return mRating;
    }
    @PropertyName("rating") public void setRating(float iRating) {
        mRating = iRating;
    }

    @PropertyName("platforms") public List<Platform> getPlatforms() {
        return mPlatforms;
    }
    @PropertyName("platforms") public void setPlatforms(List<Platform> iPlatforms) {
        mPlatforms = iPlatforms;
    }

    @PropertyName("genres") public List<Genre> getGenres() {
        return mGenres;
    }
    @PropertyName("genres") public void setGenres(List<Genre> iGenres) {
        mGenres = iGenres;
    }
}
