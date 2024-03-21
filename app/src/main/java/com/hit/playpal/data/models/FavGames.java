package com.hit.playpal.data.models;

import com.google.firebase.firestore.PropertyName;

public class FavGames {
    @PropertyName("game_name")
    private String mGameName;

    @PropertyName("username")
    private String mUsername;

    public FavGames() {}

    public FavGames(
            String iGameName,
            String iUsername
    ) {
        mGameName = iGameName;
        mUsername = iUsername;
    }

    @PropertyName("game_name") public String getGameName() {
        return mGameName;
    }
    @PropertyName("game_name") public void setGameName(String iGameName) {
        mGameName = iGameName;
    }

    @PropertyName("username") public String getUsername() {
        return mUsername;
    }
    @PropertyName("username") public void setUsername(String iUsername) {
        mUsername = iUsername;
    }
}
