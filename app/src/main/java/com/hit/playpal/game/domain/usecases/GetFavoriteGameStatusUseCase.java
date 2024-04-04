package com.hit.playpal.game.domain.usecases;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.hit.playpal.entities.users.User;
import com.hit.playpal.game.data.repositories.GameRepository;
import com.hit.playpal.game.data.utils.exceptions.DatabaseErrorException;
import com.hit.playpal.utils.CurrentlyLoggedUser;
import com.hit.playpal.utils.UseCaseResult;

import java.util.concurrent.CompletableFuture;

public class GetFavoriteGameStatusUseCase {
    private final GameRepository gameRepository = GameRepository.getGameRepository();

    public CompletableFuture<Boolean> execute(String iGameName)
    {
        CompletableFuture<Boolean> future = new CompletableFuture<>();

        gameRepository.getGameFavoriteStatus(iGameName, CurrentlyLoggedUser.getCurrentlyLoggedUser()).
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
