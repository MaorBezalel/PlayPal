package com.hit.playpal.profile.ui.viewmodels;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hit.playpal.entities.chats.o2o.OneToOneChatRoom;
import com.hit.playpal.entities.messages.Message;
import com.hit.playpal.entities.relationships.OneToOneChatRelationship;
import com.hit.playpal.entities.users.User;
import com.hit.playpal.profile.domain.usecases.CreateAndGetNewOneToOneChatRoomUseCase;
import com.hit.playpal.profile.domain.usecases.CreateNewOneToOneChatRelationshipUseCase;
import com.hit.playpal.profile.domain.usecases.GetTheExistingOneToOneChatRoomUseCase;
import com.hit.playpal.profile.domain.usecases.TryToGetOneToOneChatRelationshipUseCase;

import java.util.ArrayList;
import java.util.HashMap;

public class ProfileViewModel extends ViewModel {
    private final MutableLiveData<OneToOneChatRoom> mOnSuccessfulOpenChat = new MutableLiveData<>();
    public MutableLiveData<OneToOneChatRoom> getOnSuccessfulOpenChat() {
        return mOnSuccessfulOpenChat;
    }

    private final MutableLiveData<Throwable> mOnFailedOpenChat = new MutableLiveData<>();
    public MutableLiveData<Throwable> getOnFailedOpenChat() {
        return mOnFailedOpenChat;
    }

    public void getOneToOneChatRoom(@NonNull User thisUser, @NonNull User otherUser) {
        TryToGetOneToOneChatRelationshipUseCase.invoke(thisUser.getUid(), otherUser.getUid())
                .whenComplete((result1, throwable1) -> {
                    if (throwable1 == null && result1 != null) {
                        GetTheExistingOneToOneChatRoomUseCase.invoke(result1)
                                .whenComplete((result2, throwable2) -> {
                                    if (throwable2 == null && result2 != null) {
                                        mOnSuccessfulOpenChat.postValue(result2); // Signal the ui that the chat room is ready
                                    } else {
                                        mOnFailedOpenChat.postValue(throwable2); // Signal the ui that the chat room is not ready
                                    }
                                });
                    } else if (throwable1 == null && result1 == null) {
                        OneToOneChatRoom newOneToOneChatRoom = createEmptyOneToOneChatRoom(thisUser, otherUser);

                        CreateAndGetNewOneToOneChatRoomUseCase.invoke(newOneToOneChatRoom)
                                .whenComplete((result2, throwable2) -> {
                                    if (throwable2 == null) {
                                        OneToOneChatRelationship newOneToOneChatRelationship = createNewOneToOneChatRelationship(thisUser, otherUser, result2);
                                        newOneToOneChatRoom.setId(result2);

                                        CreateNewOneToOneChatRelationshipUseCase.invoke(newOneToOneChatRelationship)
                                                .whenComplete((result3, throwable3) -> {
                                                    if (throwable3 == null) {
                                                        mOnSuccessfulOpenChat.postValue(newOneToOneChatRoom); // Signal the ui that the chat room is ready
                                                    } else {
                                                        mOnFailedOpenChat.postValue(throwable3); // Signal the ui that the chat room is not ready
                                                    }
                                                });
                                    }
                                });
                    }
                });
    }

    @NonNull
    private OneToOneChatRoom createEmptyOneToOneChatRoom(@NonNull User thisUser, @NonNull User otherUser) {
        HashMap<String, OneToOneChatRoom.OtherMemberData> otherMemberData = new HashMap<String, OneToOneChatRoom.OtherMemberData>() {{
            put(thisUser.getUid(), new OneToOneChatRoom.OtherMemberData(otherUser.getDisplayName(), otherUser.getProfilePicture()));
            put(otherUser.getUid(), new OneToOneChatRoom.OtherMemberData(thisUser.getDisplayName(), thisUser.getProfilePicture()));
        }};
        ArrayList<String> membersUid = new ArrayList<String>() {{
            add(thisUser.getUid());
            add(otherUser.getUid());
        }};
        Message lastMessage = new Message();

        return new OneToOneChatRoom(
                null,
                membersUid,
                lastMessage,
                otherMemberData
        );
    }

    private OneToOneChatRelationship createNewOneToOneChatRelationship(@NonNull User thisUser, @NonNull User otherUser, @NonNull String chatRoomId) {
        return new OneToOneChatRelationship(
                OneToOneChatRelationship.joinUids(thisUser.getUid(), otherUser.getUid()),
                chatRoomId
        );
    }
}
