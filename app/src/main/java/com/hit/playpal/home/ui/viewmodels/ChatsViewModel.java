package com.hit.playpal.home.ui.viewmodels;

import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.Query;
import com.hit.playpal.entities.chats.enums.ChatRoomType;
import com.hit.playpal.home.domain.usecases.chats.GenerateQueryForAllChatRoomsUseCase;
import com.hit.playpal.home.domain.usecases.chats.GenerateQueryForSpecificChatRoomsUseCase;
import com.hit.playpal.utils.CurrentlyLoggedUser;

public class ChatsViewModel extends ViewModel {
    private static final String TAG = "ChatsViewModel";
    private final String THIS_USER_ID = CurrentlyLoggedUser.get().getUid();

    public Query generateQueryForAllChatRooms() {
        return GenerateQueryForAllChatRoomsUseCase.invoke(THIS_USER_ID);
    }

    public Query generateQueryForSpecificChatRooms(ChatRoomType iChatRoomType) {
        return GenerateQueryForSpecificChatRoomsUseCase.invoke(THIS_USER_ID, iChatRoomType);
    }
}
