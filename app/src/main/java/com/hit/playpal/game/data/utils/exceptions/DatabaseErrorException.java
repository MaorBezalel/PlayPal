package com.hit.playpal.game.data.utils.exceptions;

public class DatabaseErrorException extends Exception {
    public DatabaseErrorException() {
        super("Error from database");
    }
}
