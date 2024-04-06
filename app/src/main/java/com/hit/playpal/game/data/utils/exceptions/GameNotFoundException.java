package com.hit.playpal.game.data.utils.exceptions;

public class GameNotFoundException extends Exception {
    public GameNotFoundException() {
        super("Game not found");
    }
}
