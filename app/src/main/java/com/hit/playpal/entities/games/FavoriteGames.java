package com.hit.playpal.entities.games;

import com.google.firebase.firestore.PropertyName;
import com.hit.playpal.entities.users.User;



public class FavoriteGames {

    @PropertyName("user") private User mUser;
    @PropertyName("user") public User getUser() { return mUser; }
    @PropertyName("user") public void setUser(User iUser) { mUser = iUser; }

    @PropertyName("game") private Game mGame;
    @PropertyName("game") public Game getGame() { return mGame; }
    @PropertyName("game") public void setGame(Game iGame) { mGame = iGame; }


    public FavoriteGames(){}
    public FavoriteGames(String iGameName, String iGameImage, String iGameId, float iGameRating, User iUser){
        mGame = new Game(iGameName, iGameImage, iGameId, iGameRating);
        mUser = iUser;
    }

}
