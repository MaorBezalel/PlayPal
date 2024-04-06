package com.hit.playpal.chatrooms.data.datasources;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.ListenerRegistration;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hit.playpal.chatrooms.domain.listeners.INewMessageEventListener;
import com.hit.playpal.entities.chats.Message;
import com.hit.playpal.chatrooms.data.listeners.FirebaseFirestoreNewMessageRegistrationListener;
import com.hit.playpal.chatrooms.data.listeners.FirebaseFirestoreNewMessageEventListener;


public class FirebaseFirestoreDataSource {
    private final FirebaseFirestore DB = FirebaseFirestore.getInstance();

    public FirebaseFirestoreNewMessageRegistrationListener listenForTheLatestMessage(String iChatRoomId, INewMessageEventListener iEventListener) {
        return new FirebaseFirestoreNewMessageRegistrationListener(
                DB
                    .collection("chat_rooms")
                    .document(iChatRoomId)
                    .addSnapshotListener(new FirebaseFirestoreNewMessageEventListener(iEventListener))
        );
    }
    public Task<QuerySnapshot> fetchMessages(String iChatRoomId, long iLimit, DocumentReference iAfterThisMessageRef) {
        return generateQueryForGettingMessages(iChatRoomId, iLimit, iAfterThisMessageRef).get();
    }
    @NonNull
    public Query generateQueryForGettingMessages(String iChatRoomId, long iLimit, DocumentReference iAfterThisMessageRef) {
        Query baseQuery = DB
                .collection("chat_rooms")
                .document(iChatRoomId)
                .collection("messages")
                .orderBy("sent_at", Query.Direction.DESCENDING)
                .limit(iLimit);

        Log.d("FirebaseFirestoreDataSource", "`generateQueryForGettingMessages` has been called with iChatRoomId = " + iChatRoomId + ", iLimit = " + iLimit + ", iAfterThisMessageRef = " + iAfterThisMessageRef);

        return iAfterThisMessageRef == null
                ? baseQuery
                : baseQuery.startAfter(iAfterThisMessageRef);
    }

    public Task<DocumentReference> writeMessage(String iChatRoomId, Message iMessage) {
        return DB
                .collection("chat_rooms")
                .document(iChatRoomId)
                .collection("messages")
                .add(iMessage);
    }

    public Task<Void> updateLastMessage(String iChatRoomId, Message iMessage) {
        return DB
                .collection("chat_rooms")
                .document(iChatRoomId)
                .update("last_message", iMessage);
    }

}
