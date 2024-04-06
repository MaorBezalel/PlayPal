package com.hit.playpal.game.data.repositories;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hit.playpal.entities.games.Game;
import com.hit.playpal.entities.users.User;
import com.hit.playpal.game.data.datasources.FavGameDataSource;
import com.hit.playpal.game.data.datasources.GameDataSource;
import com.hit.playpal.game.data.utils.exceptions.DatabaseErrorException;
import com.hit.playpal.game.domain.repositories.IGameRepository;
import com.hit.playpal.game.data.utils.exceptions.GameNotFoundException;

public class GameRepository implements IGameRepository {
    private final GameDataSource gameDataSource = new GameDataSource();
    private final FavGameDataSource favGameDataSource = new FavGameDataSource();


    private static GameRepository sGameRepositorySingleton = new GameRepository();
    public static GameRepository getGameRepository() {
        if(sGameRepositorySingleton == null)
        {
            sGameRepositorySingleton = new GameRepository();
        }

        return sGameRepositorySingleton;
    }

    private GameRepository() {}


    @Override
    public Task<Game> getGameInfo(String iGameName) {
        return gameDataSource.getGame(iGameName).continueWith(task -> {
            if (task.isSuccessful()) {

                DocumentSnapshot document = task.getResult();

                if (document.exists())
                {
                    return document.toObject(Game.class);
                }
                else
                {
                    throw new GameNotFoundException();
                }
            }
            else
            {
                throw new DatabaseErrorException();
            }
        });
    }

    @Override
    public Task<Void> updateGameToFavorites(Game iGame, boolean iNewStatus, User iCurrentlyLoggedUser) {
        if(iNewStatus)
        {
            return favGameDataSource.addGameToFavorites(iGame, iCurrentlyLoggedUser);
        }
        else
        {
            return favGameDataSource.getGameFavoriteStatus(iGame.getGameName(), iCurrentlyLoggedUser).continueWithTask(task -> {
                if(task.isSuccessful())
                {
                    QuerySnapshot querySnapshot = task.getResult();
                    if(querySnapshot.isEmpty())
                    {
                        return Tasks.forException(new GameNotFoundException());
                    }
                    else
                    {
                        return favGameDataSource.removeGameFromFavorites(querySnapshot.getDocuments().get(0).getId()).continueWithTask(task1 -> {
                            if(task1.isSuccessful())
                            {
                                return Tasks.forResult(null);
                            }
                            else
                            {
                                return Tasks.forException(new DatabaseErrorException());
                            }
                        });
                    }
                }
                else
                {
                    return Tasks.forException(new DatabaseErrorException());
                }
            });
        }
    }

    @Override
    public Task<Boolean> getGameFavoriteStatus(String iGameName, User iCurrentlyLoggedUser) {
        return favGameDataSource.getGameFavoriteStatus(iGameName, iCurrentlyLoggedUser).continueWith(task -> {
            if(task.isSuccessful())
            {
                return !task.getResult().isEmpty();
            }
            else
            {
                throw new DatabaseErrorException();
            }
        });
    }



}
