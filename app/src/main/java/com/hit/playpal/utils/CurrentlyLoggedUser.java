package com.hit.playpal.utils;

import com.hit.playpal.entities.users.User;

public final class CurrentlyLoggedUser {
    private static User sCurrentlyLoggedUser = null;

    private CurrentlyLoggedUser() {}

    public static User getCurrentlyLoggedUser() {
        return sCurrentlyLoggedUser;
    }

    public static void setCurrentlyLoggedUser(User iCurrentlyLoggedUser) {
        sCurrentlyLoggedUser = iCurrentlyLoggedUser;
    }
}
