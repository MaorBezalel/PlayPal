package com.hit.playpal.home.domain.repositories;

import games.Game;
import home.domain.util.GameFilterOptions;

import java.util.List;

public interface IGameRepository {
    List<Game> getGamesByAlphabeticOrder(String iOrderType, int iPage);
    List<Game> getGamesByFilter(GameFilterOptions iFilteringOptions, int iPage);
}

