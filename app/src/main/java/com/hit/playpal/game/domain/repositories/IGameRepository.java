package com.hit.playpal.game.domain.repositories;

import com.google.android.gms.tasks.Task;
import com.hit.playpal.entities.games.Game;
import com.hit.playpal.entities.users.User;
import com.hit.playpal.entities.chats.GroupChat;

import java.util.Date;
import java.util.List;

public interface IGameRepository {
    Task<Game> getGameInfo(String iGameId);
    Task<Void> updateGameToFavorites(Game iGame, boolean iNewStatus, User iCurrentlyLoggedUser);
    Task<Boolean> getGameFavoriteStatus(String iGameName, User iCurrentlyLoggedUser);
}
