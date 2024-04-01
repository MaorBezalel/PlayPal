package com.hit.playpal.game.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hit.playpal.entities.games.Game;
import com.hit.playpal.game.domain.usecases.GetFavoriteGameStatusUseCase;
import com.hit.playpal.game.domain.usecases.UpdateFavoriteGameStatusUseCase;
import com.hit.playpal.game.domain.usecases.GetGameInfoUseCase;
import com.hit.playpal.home.adapters.GameAdapter;

public class GameViewModel extends ViewModel {
    private MutableLiveData<Game> mGame;
    private MutableLiveData<Boolean> isInFavorites;


    private MutableLiveData<String> mGameDetailsErrorMessage;
    private MutableLiveData<String> mAddToFavoritesErrorMessage;

    public GameViewModel()
    {
        mGame = new MutableLiveData<>();
        mGameDetailsErrorMessage = new MutableLiveData<>();
        mAddToFavoritesErrorMessage = new MutableLiveData<>();
        isInFavorites = new MutableLiveData<>();
    }

    public void fetchGameDetails(String iGameId)
    {
        GetGameInfoUseCase getGameInfoUseCase = new GetGameInfoUseCase();
        getGameInfoUseCase.execute(
                iGameId,
                onSuccessReturnedGame -> {
                    mGame.setValue(onSuccessReturnedGame);
                    fetchGameFavoriteStatus(iGameId);},
                onFailure -> mGameDetailsErrorMessage.setValue(onFailure.getMessage())
        );
    }

    public void addGameToFavorites(String iGameId)
    {
        UpdateFavoriteGameStatusUseCase updateFavoriteGameStatusUseCase = new UpdateFavoriteGameStatusUseCase();
        updateFavoriteGameStatusUseCase.execute(
                 iGameId,
                 onSuccess -> isInFavorites.setValue(true),
                 onFailure -> mAddToFavoritesErrorMessage.setValue(onFailure.getMessage())
        );
    }

    private void fetchGameFavoriteStatus(String iGameId)
    {
        GetFavoriteGameStatusUseCase getFavoriteGameStatusUseCase = new GetFavoriteGameStatusUseCase();
        getFavoriteGameStatusUseCase.execute(
                iGameId,
                onSuccess -> isInFavorites.setValue(onSuccess),
                onFailure -> mGameDetailsErrorMessage.setValue(onFailure.getMessage())
        );
    }

    public MutableLiveData<Game> getGame() {
        return mGame;
    }

    public MutableLiveData<String> getGameDetailsErrorMessage() {
        return mGameDetailsErrorMessage;
    }

    public MutableLiveData<Boolean> getIsInFavorites() {
        return isInFavorites;
    }

    public MutableLiveData<String> getAddToFavoritesErrorMessage() {
        return mAddToFavoritesErrorMessage;
    }
}
