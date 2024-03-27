package com.hit.playpal.home.domain.repositories;

import com.hit.playpal.home.domain.util.GameFilterOptions;

import com.hit.playpal.entities.games.Game;

import java.util.List;

public interface IGameRepository {
    List<Game> getGamesByAlphabeticOrder(String iOrderType, int iPage);
    List<Game> getGamesByFilter(GameFilterOptions iFilteringOptions, int iPage);
}

