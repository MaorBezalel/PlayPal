package com.hit.playpal.auth.domain.repositories;


import com.hit.playpal.entities.users.User;

public interface IAuthRepository {
    User loginWithEmail(String iEmail, String iPassword);
    User loginWithUsername(String iUsername, String iPassword); // ---> loginWithEmail
    User createUser(String iEmail, String iUsername, String iDisplayName, String iPassword);
    void forgotPassword(String iEmail);
}
