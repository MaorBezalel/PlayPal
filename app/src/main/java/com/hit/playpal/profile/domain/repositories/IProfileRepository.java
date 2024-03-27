package com.hit.playpal.profile.domain.repositories;

import com.hit.playpal.entities.chats.GroupChat;
import com.hit.playpal.entities.games.Game;
import com.hit.playpal.entities.users.User;

import java.util.List;

public interface IProfileRepository {
    User getProfileAccountInfo(String iUsername); // display name, profile picture, about me, username
    List<User> getFriendsOfUser(String iUsername, int iPage);
    List<User> getFriendsOfUserByDisplayName(String iUsername, String iSearchedDisplayName, int iPage);
    List<GroupChat> getGroupChatsOfUser(String iUsername, int iPage);
    List<GroupChat> getGroupChatsOfUserByGroupChatName(String iUsername, String iSearchedGroupChatName, int iPage);
    List<Game> getFavoriteGamesOfUser(String iUsername, int iPage);
    List<Game> getFavoriteGamesOfUserByGameName(String iUsername, String iSearchedFavoriteGameName, int iPage);
}
