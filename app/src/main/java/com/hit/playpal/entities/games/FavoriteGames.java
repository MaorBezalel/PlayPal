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

    public FavoriteGames(){}
    public FavoriteGames(String iGameName, User iUser){
        mGameName = iGameName;
        mUser = iUser;
    }
}
