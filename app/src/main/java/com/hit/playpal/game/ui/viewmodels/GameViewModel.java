package com.hit.playpal.game.ui.viewmodels;

import android.util.Log;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hit.playpal.entities.games.Game;
import com.hit.playpal.entities.users.User;
import com.hit.playpal.game.domain.usecases.GetFavoriteGameStatusUseCase;
import com.hit.playpal.game.domain.usecases.UpdateFavoriteGameStatusUseCase;
import com.hit.playpal.game.domain.usecases.GetGameInfoUseCase;
import com.hit.playpal.utils.SingleLiveEvent;

import java.util.concurrent.atomic.AtomicReference;

public class GameViewModel extends ViewModel {
    private MutableLiveData<Game> mGame;
    private MutableLiveData<Boolean> mIsInFavorites;


    private MutableLiveData<String> mLoadGameDetailsSuccess;
    private MutableLiveData<String> mLoadGameDetailsError;

    private MutableLiveData<String> mUpdateGameFavoriteStatusSuccess;
    private MutableLiveData<String> mUpdateGameFavoriteStatusError;

    public GameViewModel()
    {
        mGame = new MutableLiveData<>();
        mLoadGameDetailsSuccess = new MutableLiveData<>();
        mLoadGameDetailsError = new MutableLiveData<>();
        mUpdateGameFavoriteStatusSuccess = new MutableLiveData<>();
        mUpdateGameFavoriteStatusError = new MutableLiveData<>();
        mIsInFavorites = new MutableLiveData<>();
    }

    public void loadGameAndUserGameFavoriteStatus(String iGameId) {
        GetGameInfoUseCase getGameInfoUseCase = new GetGameInfoUseCase();
        GetFavoriteGameStatusUseCase getFavoriteGameStatusUseCase = new GetFavoriteGameStatusUseCase();

        AtomicReference<Game> currentGame = new AtomicReference<>();
        AtomicReference<Boolean> isInFavorites = new AtomicReference<>();


        getGameInfoUseCase.execute(iGameId)

                .thenCompose(game -> {
                    currentGame.set(game);
                    return getFavoriteGameStatusUseCase.execute(game.getGameName());
                })

                .thenAccept(isFavorite -> {
                    isInFavorites.set(isFavorite);})

                .whenComplete((result, throwable) -> {
                    if (throwable != null)
                    {
                        Log.e("GameViewModel", "Error loading game details", throwable);
                        mLoadGameDetailsError.setValue(throwable.getMessage());
                    }
                    else
                    {
                        mGame.setValue(currentGame.get());
                        mIsInFavorites.setValue(isInFavorites.get());
                        mLoadGameDetailsSuccess.setValue("Game details loaded successfully");
                    }
                });
    }

    public void updateGameFavoriteStatus() {

        if(mGame.getValue() == null || mIsInFavorites.getValue() == null || mGame.getValue().getGameName() == null) return;

        System.out.println(mIsInFavorites.getValue());

        UpdateFavoriteGameStatusUseCase updateFavoriteGamesStatus = new UpdateFavoriteGameStatusUseCase();

        updateFavoriteGamesStatus
                .execute(mGame.getValue().getGameName(), mGame.getValue().getBackgroundImage(), mGame.getValue().getRating(), !mIsInFavorites.getValue())

                .thenAccept(none -> {
                    mIsInFavorites.setValue(!mIsInFavorites.getValue());
                    mUpdateGameFavoriteStatusSuccess.setValue("Updated To: " + mIsInFavorites.getValue().toString());
                })
                .exceptionally(throwable -> {
                    mUpdateGameFavoriteStatusError.setValue(throwable.getMessage());
                    return null;
                });
    }

    public MutableLiveData<Game> getGame() {
        return mGame;
    }
    public MutableLiveData<Boolean> getIsInFavorites() {
        return mIsInFavorites;
    }


    public MutableLiveData<String> getLoadGameDetailsSuccess() {
        return mLoadGameDetailsSuccess;
    }
    public MutableLiveData<String> getLoadGameDetailsError() {
        return mLoadGameDetailsError;
    }


    public MutableLiveData<String> getUpdateFavoriteStatusSuccess() {
        return mUpdateGameFavoriteStatusSuccess;
    }
    public MutableLiveData<String> getUpdateFavoriteStatusError() {
        return mUpdateGameFavoriteStatusError;
    }
}
