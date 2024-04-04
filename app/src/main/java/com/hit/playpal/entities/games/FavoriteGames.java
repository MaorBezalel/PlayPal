package com.hit.playpal.entities.games;

import com.google.firebase.firestore.PropertyName;
import com.hit.playpal.entities.users.User;



public class FavoriteGames {
    @PropertyName("game_name") private String mGameName;
    @PropertyName("game_name") public String getGameName() { return mGameName; }
    @PropertyName("game_name") public void setGameName(String iGameName) { mGameName = iGameName; }

    @PropertyName("user") private User mUser;
    @PropertyName("user") public User getUser() { return mUser; }
    @PropertyName("user") public void setUser(User iUser) { mUser = iUser; }

    @PropertyName("game_image") private String mGameImage;
    @PropertyName("game_image") public String getGameImage() { return mGameImage; }
    @PropertyName("game_image") public void setGameImage(String iGameImage) { mGameImage = iGameImage; }

    @PropertyName("game_rating") private float mGameRating;
    @PropertyName("game_rating") public float getGameRating() { return mGameRating; }
    @PropertyName("game_rating") public void setGameRating(float iGameRating) { mGameRating = iGameRating; }

    public FavoriteGames(){}
    public FavoriteGames(String iGameName, User iUser){
        mGameName = iGameName;
        mUser = iUser;
    }

    public FavoriteGames(String iGameName, String iGameImage, float iGameRating, User iUser){
        mGameName = iGameName;
        mUser = iUser;
        mGameImage = iGameImage;
        mGameRating = iGameRating;
    }
}
