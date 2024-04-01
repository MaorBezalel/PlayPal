package com.hit.playpal.game.domain.usecases;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.hit.playpal.game.data.repositories.GameRepository;

public class UpdateFavoriteGameStatusUseCase {
    private static GameRepository sGameRepository = new GameRepository();

    public void execute(String iGameId, OnSuccessListener<Void> iOnSuccessListener, OnFailureListener iOnFailureListener)
    {
        sGameRepository.addGameToFavorites(iGameId).
                addOnSuccessListener(iOnSuccessListener).
                addOnFailureListener(iOnFailureListener);
    }
}
