package com.hit.playpal.game.data.repositories;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hit.playpal.entities.games.Game;
import com.hit.playpal.game.data.datasources.FavGameDataSource;
import com.hit.playpal.game.data.datasources.GameDataSource;
import com.hit.playpal.game.domain.repositories.IGameRepository;
import com.hit.playpal.game.exceptions.GameNotFoundException;

import java.util.List;

public class GameRepository implements IGameRepository {
    private static GameDataSource sGameDataSource = new GameDataSource();
    private static FavGameDataSource sFavGameDataSource = new FavGameDataSource();

    @Override
    public Task<Game> getGameInfo(String iGameName) {
        return sGameDataSource.getGame(iGameName).continueWith(task -> {
            if (task.isSuccessful()) {

                DocumentSnapshot document = task.getResult();

                if (document.exists())
                {
                    return document.toObject(Game.class);
                }
                else
                {
                    throw new GameNotFoundException("Game not found");
                }
            }
            else
            {
                throw new Exception("Failed to get game info");
            }
        });
    }

    @Override
    public Task<Void> addGameToFavorites(String iGameId) {
        // TODO: add userId from AndroidViewModel implementation
        return sFavGameDataSource.addGameToFavorites(iGameId).continueWith(task -> {
            if (task.isSuccessful())
            {
                return null;
            }
            else
            {
                throw new Exception("Failed to add game to favorites");
            }
        });
    }

    @Override
    public Task<Boolean> getGameFavoriteStatus(String iGameName) {
        return sFavGameDataSource.getGameFavoriteStatus(iGameName).continueWith(task -> {
            if(task.isSuccessful())
            {
                QuerySnapshot querySnapshot = task.getResult();

                return !querySnapshot.isEmpty();
            }
            else
            {
                throw new Exception("Failed to get game favorite status");
            }
        });
    }



}
