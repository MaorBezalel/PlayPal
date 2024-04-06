package com.hit.playpal.game.domain.repositories;

import com.google.android.gms.tasks.Task;
import com.hit.playpal.entities.games.Game;

public interface IGameRepository {
    Task<Game> getGameInfo(String iGameId);
    Task<Void> updateGameToFavorites(String iGameName, boolean iNewStatus, User iCurrentlyLoggedUser);
    Task<Boolean> getGameFavoriteStatus(String iGameName, User iCurrentlyLoggedUser);
}
