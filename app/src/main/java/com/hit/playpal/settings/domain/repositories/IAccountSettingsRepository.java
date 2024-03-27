package com.hit.playpal.settings.domain.repositories;

import users.User;

import java.util.List;

public interface IAccountSettingsRepository {
    String getTheEmailOfThisAccount();
    void updateTheEmailOfThisAccount(String iNewEmail);

    String getTheUsernameOfThisAccount();
    void updateTheUsernameOfThisAccount(String iNewUsername);

    String getTheDisplayNameOfThisAccount();
    void updateTheDisplayNameOfThisAccount(String iNewDisplayName);

    String getThePasswordOfThisAccount();
    void updateThePasswordOfThisAccount(String iNewPassword);

    List<User> getTheBlockedUsersOfThisAccount(int iPage);
    void AddUserToTheBlockedListOfThisAccount(String iUsername);
    void removeUserFromTheBlockedListOfThisAccount(String iUsername);

    void logoutThisAccount();
    void deleteThisAccount();
}
