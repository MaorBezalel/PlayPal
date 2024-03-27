package com.hit.playpal.home.domain.repositories;

import com.hit.playpal.entities.chats.GroupChat;
import com.hit.playpal.entities.games.Game;
import com.hit.playpal.entities.users.User;

import java.util.List;

public interface IMyProfileRepository {
    List<User> getFriendsOfUser(int iPage);
    List<User> getFriendsOfUserByDisplayName(String iSearchedDisplayName, int iPage);
    List<GroupChat> getGroupChatsOfUser(int iPage);
    List<GroupChat> getGroupChatsOfUserByGroupChatName(String iSearchedGroupChatName, int iPage);
    List<Game> getFavoriteGamesOfUser(int iPage);
    List<Game> getFavoriteGamesOfUserByGameName(String iSearchedFavoriteGameName, int iPage);
}
