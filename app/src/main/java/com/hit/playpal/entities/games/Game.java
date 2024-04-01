package com.hit.playpal.entities.games;

import com.google.firebase.firestore.PropertyName;
import com.hit.playpal.entities.games.enums.Genre;
import com.hit.playpal.entities.games.enums.Platform;

import java.util.Date;
import java.util.List;

public class Game {

    @PropertyName("game_id")  private String mGameId;
    @PropertyName("game_id") public String getGameId() { return mGameId; }
    @PropertyName("game_id") public void setGameId(String iGameId) { mGameId = iGameId; }

    @PropertyName("game_name") private String mGameName;
    @PropertyName("game_name") public String getGameName() { return mGameName; }
    @PropertyName("game_name") public void setGameName(String iGameName) { mGameName = iGameName; }

    @PropertyName("background_image") private String mBackgroundImage;
    @PropertyName("background_image") public String getBackgroundImage() { return mBackgroundImage; }
    @PropertyName("background_image") public void setBackgroundImage(String iBackgroundImage) { mBackgroundImage = iBackgroundImage; }

    @PropertyName("genres") private List<Genre> mGenres;
    @PropertyName("genres") public List<Genre> getGenres() { return mGenres; }
    @PropertyName("genres") public void setGenres(List<Genre> iGenres) { mGenres = iGenres; }

    @PropertyName("platforms") private List<Platform> mPlatforms;
    @PropertyName("platforms") public List<Platform> getPlatforms() { return mPlatforms; }
    @PropertyName("platforms") public void setPlatforms(List<Platform> iPlatforms) { mPlatforms = iPlatforms; }

    @PropertyName("rating") private float mRating;
    @PropertyName("rating") public float getRating() { return mRating; }
    @PropertyName("rating") public void setRating(float iRating) { mRating = iRating; }

    @PropertyName("release_date") private Date mReleaseDate;
    @PropertyName("release_date") public Date getReleaseDate() { return mReleaseDate; }
    @PropertyName("release_date") public void setReleaseDate(Date iReleaseDate) { mReleaseDate = iReleaseDate; }

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
