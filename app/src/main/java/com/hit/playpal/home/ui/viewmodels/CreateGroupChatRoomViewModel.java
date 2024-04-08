package com.hit.playpal.home.ui.viewmodels;

import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.Query;
import com.hit.playpal.entities.chats.GroupChatRoom;
import com.hit.playpal.home.domain.usecases.GenerateQueryForAllUsersUseCase;
import com.hit.playpal.home.domain.usecases.games.GenerateQueryForAllGamesUseCase;
import com.hit.playpal.utils.CurrentlyLoggedUser;

public class CreateGroupChatRoomViewModel extends ViewModel {
    private static final String TAG = "CreateGroupChatRoomViewModel";
    private final String THIS_USER_ID = CurrentlyLoggedUser.getCurrentlyLoggedUser().getUid();
    public final GroupChatRoom GROUP_CHAT_ROOM_TO_CREATE = new GroupChatRoom();

    public Query generateQueryForAllGames() {
        return GenerateQueryForAllGamesUseCase.invoke();
    }

    public Query generateQueryForAllUsers() {
        return GenerateQueryForAllUsersUseCase.invoke(THIS_USER_ID);
    }
}
