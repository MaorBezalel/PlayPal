package com.hit.playpal.game.domain.usecases;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.hit.playpal.entities.games.Game;
import com.hit.playpal.game.data.repositories.GameRepository;

import org.checkerframework.checker.units.qual.C;

import java.util.concurrent.CompletableFuture;

public class GetGameInfoUseCase {
    private final GameRepository gameRepository = GameRepository.getGameRepository();
    public CompletableFuture<Game> execute(String iGameId)
    {
        CompletableFuture<Game> future = new CompletableFuture<>();

        gameRepository.getGameInfo(iGameId).addOnCompleteListener(task -> {
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
