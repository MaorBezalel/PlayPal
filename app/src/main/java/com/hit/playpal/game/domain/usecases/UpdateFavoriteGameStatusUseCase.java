package com.hit.playpal.game.domain.usecases;

import com.hit.playpal.entities.games.Game;
import com.hit.playpal.game.data.repositories.GameRepository;
import com.hit.playpal.utils.CurrentlyLoggedUser;

import java.util.concurrent.CompletableFuture;

public class UpdateFavoriteGameStatusUseCase {
    private final GameRepository gameRepository = GameRepository.getGameRepository();

    public CompletableFuture<Void> execute(Game iGame, boolean iNewStatus)
    {
        CompletableFuture<Void> future = new CompletableFuture<>();

        gameRepository.updateGameToFavorites(iGame , iNewStatus, CurrentlyLoggedUser.get()).
                addOnCompleteListener(task -> {
            if (task.isSuccessful())
            {
                future.complete(null);
            }
            else
            {
                future.completeExceptionally(task.getException());
            }
        });

        return future;
    }
}
