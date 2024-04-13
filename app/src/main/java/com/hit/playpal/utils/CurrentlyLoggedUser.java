package com.hit.playpal.utils;

import com.hit.playpal.entities.users.User;

/**
 * This utility class is used to manage the state of the currently logged in user.
 * It follows the Singleton pattern to ensure that only one instance of the user is active at a time.
 */
public final class CurrentlyLoggedUser {
    /**
     * The single instance of the User class that represents the currently logged in user.
     */
    private static User sInstance = null;

    /**
     * Private constructor to prevent instantiation of this utility class.
     */
    private CurrentlyLoggedUser() {} // static class

    /**
     * Retrieves the instance of the currently logged in user.
     *
     * @return The currently logged in user, or null if no user is logged in.
     */
    public static User get() {
        return sInstance;
    }

    /**
     * Sets the instance of the currently logged in user.
     *
     * @param iCurrentlyLoggedUser The User object representing the user that has just logged in.
     */
    public static void set(User iCurrentlyLoggedUser) {
        sInstance = iCurrentlyLoggedUser;
    }
}
