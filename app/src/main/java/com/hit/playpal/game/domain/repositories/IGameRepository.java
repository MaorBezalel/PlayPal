package com.hit.playpal.game.domain.repositories;

import com.hit.playpal.entities.games.Game;
import com.hit.playpal.entities.users.User;
import com.hit.playpal.entities.chats.GroupChat;

import java.util.Date;
import java.util.List;

public interface IGameRepository {
    //Game getGameInfo(String iGameId, String iGameName, String iGameImage, List<String> iGenres, List<String> iPlatforms, float iRating, Date iReleaseDate);
    Game getGameInfo(String iGameName);
    List<User> favoriteByUsers(String iUsername, String iUserImage, int iPage);
    List<GroupChat> groupChatsOfGame(String iGroupName, int iPage);


}
