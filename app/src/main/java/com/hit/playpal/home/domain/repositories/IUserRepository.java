package com.hit.playpal.home.domain.repositories;

import com.hit.playpal.entities.users.User;

import java.util.List;

public interface IUserRepository {
    List<User> getUsers(int iPage);
    List<User> getUserByDisplayName(String iDisplayName, int iPage);
    List<User> getUsersByFavGameName(String iFavGameName, int iPage);
    List<User> getUsersByGroupChatName(String iGroupChatName, int iPage);
}
