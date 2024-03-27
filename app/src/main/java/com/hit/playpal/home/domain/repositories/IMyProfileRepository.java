package com.hit.playpal.home.domain.repositories;

import chats.GroupChat;
import games.Game;
import users.User;

import java.util.List;

public interface IMyProfileRepository {
    List<User> getFriendsOfUser(int iPage);
    List<User> getFriendsOfUserByDisplayName(String iSearchedDisplayName, int iPage);
    List<GroupChat> getGroupChatsOfUser(int iPage);
    List<GroupChat> getGroupChatsOfUserByGroupChatName(String iSearchedGroupChatName, int iPage);
    List<Game> getFavoriteGamesOfUser(int iPage);
    List<Game> getFavoriteGamesOfUserByGameName(String iSearchedFavoriteGameName, int iPage);
}
