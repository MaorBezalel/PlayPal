package com.hit.playpal.chatrooms.data.pagingsources;

import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.paging.PagingSource;
import androidx.paging.PagingState;

import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.QuerySnapshot;
import com.hit.playpal.chatrooms.data.datasources.FirebaseFirestoreDataSource;
import com.hit.playpal.entities.chats.Message;

import java.util.List;

import kotlin.coroutines.Continuation;

public class MessagePagingSource extends PagingSource<DocumentReference, Message> {
    private final FirebaseFirestoreDataSource DB;
    private final String CHAT_ROOM_ID;
    private final int PAGE_SIZE;

    public MessagePagingSource(FirebaseFirestoreDataSource iDB, String iChatRoomId, int iPageSize) {
        DB = iDB;
        CHAT_ROOM_ID = iChatRoomId;
        PAGE_SIZE = iPageSize;
    }

    @Nullable
    @Override
    public DocumentReference getRefreshKey(@NonNull PagingState<DocumentReference, Message> iPagingState) {
//        // The anchor position is the first visible position in the list
//        Integer anchorPosition = iPagingState.getAnchorPosition();
//        if (anchorPosition == null) {
//            return null;
//        }
//
//        // Get the closest page to the anchor position
//        LoadResult.Page<DocumentReference, Message> closestPage = iPagingState.closestPageToPosition(anchorPosition);
//        if (closestPage == null) {
//            return null;
//        } else {
//            // Return the key for the previous page
//            return closestPage.getPrevKey();
//        }

        return null;
    }

    @Nullable
    @Override
    public Object load(@NonNull LoadParams<DocumentReference> iLoadParams, @NonNull Continuation<? super LoadResult<DocumentReference, Message>> iContinuation) {
        DocumentReference afterThisMessageRef = iLoadParams.getKey();

        try {
            // Fetch messages from Firestore
            QuerySnapshot querySnapshot = Tasks.await(DB.fetchMessages(CHAT_ROOM_ID, PAGE_SIZE, afterThisMessageRef));

            // Convert the Firestore QuerySnapshot to a list of Message objects
            List<Message> messages = querySnapshot.toObjects(Message.class);

            // Get the reference to the document of the last message of the current page
            DocumentReference lastMessageRef = querySnapshot.getDocuments().get(querySnapshot.size() - 1).getReference();

            Log.d("MessagePagingSource", "load: messages.size() = " + messages.size() + ", lastMessageRef = " + lastMessageRef.getPath());

            // Return the list of messages and the reference to the document of the last message (for the next page)
            return new LoadResult.Page<>(
                    messages,
                    null,
                    lastMessageRef
            );
        } catch (Exception e) {
            return new LoadResult.Error<>(e);
        }
    }
}

