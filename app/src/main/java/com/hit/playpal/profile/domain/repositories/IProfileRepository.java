package com.hit.playpal.profile.domain.repositories;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hit.playpal.entities.chats.GroupChat;
import com.hit.playpal.entities.games.Game;
import com.hit.playpal.entities.users.User;

import java.util.List;

public interface IProfileRepository {
/*    User getProfileAccountInfo(String iUsername); // display name, profile picture, about me, username
    List<User> getFriendsOfUser(String iUsername, int iPage);
    List<User> getFriendsOfUserByDisplayName(String iUsername, String iSearchedDisplayName, int iPage);
    List<GroupChat> getGroupChatsOfUser(String iUsername, int iPage);
    List<GroupChat> getGroupChatsOfUserByGroupChatName(String iUsername, String iSearchedGroupChatName, int iPage);
    List<Game> getFavoriteGamesOfUser(String iUsername, int iPage);
    List<Game> getFavoriteGamesOfUserByGameName(String iUsername, String iSearchedFavoriteGameName, int iPage);*/

    Task<DocumentSnapshot> getUserByUid(String iUid);
    Task<DocumentSnapshot> getUserPrivateByUid(String iUid);

    Task<QuerySnapshot>getRoomsByParticipantUid(String uid, DocumentSnapshot lastVisible, int limit);

    Task<QuerySnapshot> getUserFriendsByDisplayName(String iUid, DocumentSnapshot lastVisible, int limit);
}
