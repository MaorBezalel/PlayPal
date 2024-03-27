package com.hit.playpal.entities.games;

import com.hit.playpal.entities.users.User;



public class FavoriteGames {
    private String mGameName;
    public String getGameName() { return mGameName; }
    public void setGameName(String iGameName) { mGameName = iGameName; }

    private User mUser;
    public User getUser() { return mUser; }
    public void setUser(User iUser) { mUser = iUser; }

    public FavoriteGames(){}
    public FavoriteGames(String iGameName, User iUser){
        mGameName = iGameName;
        mUser = iUser;
    }
}
