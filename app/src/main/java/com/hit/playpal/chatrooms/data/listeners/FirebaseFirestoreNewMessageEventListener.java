package com.hit.playpal.chatrooms.data.listeners;

import androidx.annotation.Nullable;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.hit.playpal.chatrooms.domain.listeners.INewMessageEventListener;
import com.hit.playpal.entities.chats.group.GroupChatRoom;
import com.hit.playpal.entities.chats.o2o.OneToOneChatRoom;
import com.hit.playpal.entities.chats.enums.ChatRoomType;

public class FirebaseFirestoreNewMessageEventListener implements EventListener<DocumentSnapshot> {
    private final String TAG = "FirebaseFirestoreNewMessageEventListener";
    private final INewMessageEventListener LISTENER;

    public FirebaseFirestoreNewMessageEventListener(INewMessageEventListener iListener) {
        LISTENER = iListener;
    }

    @Override
    public void onEvent(@Nullable DocumentSnapshot iValue, @Nullable FirebaseFirestoreException iError) {
        if (iError != null) {
            LISTENER.onError(iError);
            return;
        }

        if (iValue != null && iValue.exists())
        {
                ChatRoomType type = ChatRoomType.valueOf(iValue.get("type").toString());

                switch (type) {
                    case ONE_TO_ONE:
                        LISTENER.onFetched(iValue.toObject(OneToOneChatRoom.class));
                        break;
                    case GROUP:
                        LISTENER.onFetched(iValue.toObject(GroupChatRoom.class));
                        break;
                    default:
                        throw new IllegalArgumentException(TAG + ": Unsupported chat room type: " + type);
                }
        }
    }
}
