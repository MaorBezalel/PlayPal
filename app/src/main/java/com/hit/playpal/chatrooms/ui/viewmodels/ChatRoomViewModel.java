package com.hit.playpal.chatrooms.ui.viewmodels;

import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelKt;
import androidx.lifecycle.ViewModelProvider;
import androidx.paging.Pager;
import androidx.paging.PagingData;
import androidx.lifecycle.FlowLiveDataConversions;
import androidx.paging.PagingLiveData;

import com.google.firebase.firestore.DocumentReference;
import com.hit.playpal.chatrooms.data.repositories.ChatRoomRepository;
import com.hit.playpal.chatrooms.domain.listeners.INewMessageEventListener;
import com.hit.playpal.chatrooms.domain.listeners.INewMessageRegistrationListener;
import com.hit.playpal.chatrooms.domain.usecases.chatbody.FetchLatestMessageUseCase;
import com.hit.playpal.chatrooms.domain.usecases.chatbody.FetchMessagesUseCase;
import com.hit.playpal.chatrooms.domain.usecases.chatbody.SendMessageUseCase;
import com.hit.playpal.chatrooms.domain.usecases.chatbody.UpdateChatRoomLastMessageUseCase;
import com.hit.playpal.entities.chats.ChatRoom;
import com.hit.playpal.entities.chats.Message;
import com.hit.playpal.entities.users.User;
import com.hit.playpal.utils.UseCaseResult;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

import kotlinx.coroutines.CoroutineScope;
import kotlinx.coroutines.flow.Flow;

public class ChatRoomViewModel extends ViewModel {
    private static final String TAG = "ChatRoomViewModel";

    private final User USER;
    public User getUser() {
        return USER;
    }

    private final MutableLiveData<ChatRoom> CHAT_ROOM_LIVE_DATA;
    public LiveData<ChatRoom> getChatRoomLiveData() {
        return CHAT_ROOM_LIVE_DATA;
    }

    private final INewMessageRegistrationListener NEW_MESSAGE_LISTENER;

    public void removeNewMessageListener() {
        NEW_MESSAGE_LISTENER.remove();
    }

    private final MutableLiveData<Void> ON_MESSAGE_SENT = new MutableLiveData<>();
    public LiveData<Void> onMessageSent() {
        return ON_MESSAGE_SENT;
    }

    public ChatRoomViewModel() {
        USER = null;
        CHAT_ROOM_LIVE_DATA = new MutableLiveData<>();
        NEW_MESSAGE_LISTENER = null;
    }
    public ChatRoomViewModel(@NonNull User iUser, @NonNull ChatRoom iChatRoom) {
        USER = iUser;
        CHAT_ROOM_LIVE_DATA = new MutableLiveData<>(iChatRoom);
        NEW_MESSAGE_LISTENER = fetchLatestMessage();
    }

    public LiveData<PagingData<Message>> fetchMessages(int iPageSize) {
        FetchMessagesUseCase useCase = new FetchMessagesUseCase(new ChatRoomRepository());
        String chatRoomId = CHAT_ROOM_LIVE_DATA.getValue().getId();
        Flow<PagingData<Message>> messages = useCase.execute(chatRoomId, iPageSize);

        return FlowLiveDataConversions.asLiveData(messages);
    }

    public LiveData<PagingData<Message>> fetchMessages2(int iPageSize) {
        FetchMessagesUseCase useCase = new FetchMessagesUseCase(new ChatRoomRepository());
        String chatRoomId = CHAT_ROOM_LIVE_DATA.getValue().getId();
        CoroutineScope viewModelScope = ViewModelKt.getViewModelScope(this);
        Pager<DocumentReference, Message> pager = useCase.execute2(chatRoomId, iPageSize);

        return PagingLiveData.cachedIn(PagingLiveData.getLiveData(pager), viewModelScope);
    }

    public void sendMessage(Message iMessage) {
        SendMessageUseCase sendMessageUseCase = new SendMessageUseCase(new ChatRoomRepository());
        UpdateChatRoomLastMessageUseCase updateChatRoomLastMessageUseCase = new UpdateChatRoomLastMessageUseCase(new ChatRoomRepository());
        AtomicReference<String> chatRoomId = new AtomicReference<>(CHAT_ROOM_LIVE_DATA.getValue().getId());

        sendMessageUseCase.execute(chatRoomId.get(), iMessage)
                .thenCompose(result -> {
                    if (result.isSuccessful()) {
                        return updateChatRoomLastMessageUseCase.execute(chatRoomId.get(), iMessage);
                    } else {
                        return CompletableFuture.completedFuture(UseCaseResult.forFailure(result.getFailure()));
                    }
                })
                .thenAccept(result -> {
                    if (result.isSuccessful()) {
                        ON_MESSAGE_SENT.setValue(null); // Tell the UI that the message was sent successfully
                    } else {
                        Log.e(TAG, "Couldn't update the last message in the chat room collection during `sendMessage` runtime");
                        Toast.makeText(null, "Couldn't send the message", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private INewMessageRegistrationListener fetchLatestMessage() {
        FetchLatestMessageUseCase useCase = new FetchLatestMessageUseCase(new ChatRoomRepository());
        String chatRoomId = CHAT_ROOM_LIVE_DATA.getValue().getId();

        return useCase.execute(chatRoomId, new INewMessageEventListener() {
            @Override
            public void onFetched(ChatRoom iChatRoom) {
                CHAT_ROOM_LIVE_DATA.postValue(iChatRoom);
            }

            @Override
            public void onError(Exception iException) {
                // TODO: handle error

                // For now...
                Log.e(TAG, "onError: " + iException.getMessage());
                Toast.makeText(null, "An error occurred while fetching the latest message", Toast.LENGTH_SHORT).show();
            }
        });
    }

    public static class Factory implements ViewModelProvider.Factory {
        private final User USER;
        private final ChatRoom CHAT_ROOM;

        public Factory(@NonNull User iUser, @NonNull ChatRoom iChatRoom) {
            USER = iUser;
            CHAT_ROOM = iChatRoom;
        }

        @NonNull
        @Override
        public <T extends ViewModel> T create(@NonNull Class<T> iModelClass) {
            if (iModelClass.isAssignableFrom(ChatRoomViewModel.class)) {
                return (T) new ChatRoomViewModel(USER, CHAT_ROOM);
            }

            throw new IllegalArgumentException(TAG+ ": Unknown ViewModel class");
        }
    }
}
