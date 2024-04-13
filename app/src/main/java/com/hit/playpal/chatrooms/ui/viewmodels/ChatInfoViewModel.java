package com.hit.playpal.chatrooms.ui.viewmodels;

import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

import com.hit.playpal.chatrooms.data.repositories.ChatRoomRepository;
import com.hit.playpal.chatrooms.domain.usecases.chatinfopage.GetMembersOfGroupChatUseCase;
import com.hit.playpal.entities.chats.group.GroupProfile;

import java.util.List;

public class ChatInfoViewModel  extends ViewModel
{
    private MutableLiveData<List<GroupProfile.Participant>> mMembersList;
    private MutableLiveData<String> mGetMemberListSuccess;
    private MutableLiveData<String> mGetMemberListError;

    public ChatInfoViewModel()
    {
        mMembersList = new MutableLiveData<>();
        mGetMemberListSuccess = new MutableLiveData<>();
        mGetMemberListError = new MutableLiveData<>();
    }

    public void getMembersListOfGroupChat(String iRoomId)
    {
        GetMembersOfGroupChatUseCase getMembersOfGroupChatUseCase = new GetMembersOfGroupChatUseCase(new ChatRoomRepository());

        getMembersOfGroupChatUseCase.execute(iRoomId)
                .whenComplete((result, throwable) -> {
                    if (throwable != null)
                    {
                        mGetMemberListError.postValue(throwable.getMessage());
                    }
                    else
                    {
                        mMembersList.postValue(result);
                        mGetMemberListSuccess.postValue("Members list fetched successfully");
                    }
                });
    }

    public MutableLiveData<List<GroupProfile.Participant>> getMembersList()
    {
        return mMembersList;
    }

    public MutableLiveData<String> getGetMemberListSuccess() {
        return mGetMemberListSuccess;
    }

    public MutableLiveData<String> getGetMemberListError() {
        return mGetMemberListError;
    }


}
