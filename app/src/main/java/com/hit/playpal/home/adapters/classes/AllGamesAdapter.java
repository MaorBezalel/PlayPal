package com.hit.playpal.home.adapters.classes;

import androidx.lifecycle.LifecycleOwner;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.hit.playpal.entities.games.Game;
import com.hit.playpal.home.adapters.interfaces.IBindableGame;
import com.hit.playpal.home.adapters.interfaces.IGameAdapter;

public class AllGamesAdapter extends GameAdapter<Game>{

    public AllGamesAdapter(IGameAdapter iGameAdapter, LifecycleOwner iOwner) {
        super(iGameAdapter, new IBindableGame<Game>() {
            @Override
            public String getTitle(Game item) {
                return item.getGameName();
            }

            @Override
            public float getRating(Game item) {
                return item.getRating();
            }

            @Override
            public String getBackgroundImage(Game item) {
                return item.getBackgroundImage();
            }

            @Override
            public String getId(Game item) {
                return item.getGameId();
            }
        }, iOwner, Game.class, FirebaseFirestore.getInstance().collection("games"));

        mGamePrefixPath = "";
    }
}
