package com.hit.playpal.game.domain.usecases;

import com.hit.playpal.game.data.repositories.GameRepository;
import com.hit.playpal.utils.CurrentlyLoggedUser;

import java.util.concurrent.CompletableFuture;

public class GetFavoriteGameStatusUseCase {
    private final GameRepository gameRepository = GameRepository.getGameRepository();

    public CompletableFuture<Boolean> execute(String iGameName)
    {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        gameRepository.getGameFavoriteStatus(iGameName, CurrentlyLoggedUser.get()).
                addOnCompleteListener(task -> {
            if (task.isSuccessful())
            {
                future.complete(task.getResult());
            }
            else
            {
                future.completeExceptionally(task.getException());
            }
        });

        return future;
    }
}
