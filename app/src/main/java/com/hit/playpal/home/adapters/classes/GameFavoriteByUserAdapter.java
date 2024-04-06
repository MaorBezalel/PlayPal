package com.hit.playpal.home.adapters.classes;

import androidx.lifecycle.LifecycleOwner;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.hit.playpal.entities.games.FavoriteGames;
import com.hit.playpal.home.adapters.interfaces.IBindableGame;
import com.hit.playpal.home.adapters.interfaces.IGameAdapter;

public class GameFavoriteByUserAdapter extends GameAdapter<FavoriteGames> {

    public GameFavoriteByUserAdapter(IGameAdapter iGameAdapter, LifecycleOwner iOwner, String userUid) {
        super(iGameAdapter, new IBindableGame<FavoriteGames>() {
            @Override
            public String getTitle(FavoriteGames item) {
                return item.getGame().getGameName();
            }

            @Override
            public float getRating(FavoriteGames item) {
                return item.getGame().getRating();
            }

            @Override
            public String getBackgroundImage(FavoriteGames item) {
                return item.getGame().getBackgroundImage();
            }

            @Override
            public String getId(FavoriteGames item) {
                return item.getGame().getGameId();
            }
        }, iOwner, FavoriteGames.class, FirebaseFirestore.getInstance().collection("fav_games").whereEqualTo("user.uid", userUid));

        mGamePrefixPath = "game.";
    }
}
