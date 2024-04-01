package com.hit.playpal.game.domain.usecases;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.hit.playpal.game.data.repositories.GameRepository;

public class GetFavoriteGameStatusUseCase {
    private static GameRepository sGameRepository = new GameRepository();

    public void execute(String iGameName, OnSuccessListener<Boolean> iOnSuccessListener, OnFailureListener iOnFailureListener)
    {
        sGameRepository.getGameFavoriteStatus(iGameName)
                .addOnSuccessListener(iOnSuccessListener)
                .addOnFailureListener(iOnFailureListener);
    }
}
