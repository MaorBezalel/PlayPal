package com.hit.playpal.chatrooms.data.datasources;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.hit.playpal.chatrooms.domain.listeners.INewMessageEventListener;
import com.hit.playpal.entities.chats.GroupProfile;
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
    public Task<DocumentSnapshot> getGroupChatProfile(String iChatRoomId) {
        return DB
                .collection("chat_rooms")
                .document(iChatRoomId)
                .collection("group_profile")
                .document("data")
                .get();
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


    public Task<QuerySnapshot> loadMessages(String iChatRoomId, long iLimit, DocumentSnapshot iAfterThisMessageRef) {
        return (iAfterThisMessageRef == null)
                ? DB
                    .collection("chat_rooms")
                    .document(iChatRoomId)
                    .collection("messages")
                    .orderBy("sent_at", Query.Direction.DESCENDING)
                    .limit(iLimit)
                    .get()
                : DB
                    .collection("chat_rooms")
                    .document(iChatRoomId)
                    .collection("messages")
                    .orderBy("sent_at", Query.Direction.DESCENDING)
                    .startAfter(iAfterThisMessageRef)
                    .limit(iLimit)
                    .get();
    }

    public Task<Void> updateGroupChatRoomWithNewMember(String iChatRoomId, String iUserId) {
        return DB
                .collection("chat_rooms")
                .document(iChatRoomId)
                .update("members_uid", FieldValue.arrayUnion(iUserId));
    }

    public Task<Void> updateGroupProfileWithNewParticipant(String iChatRoomId, GroupProfile.Participant iParticipant) {
        return DB
                .collection("chat_rooms")
                .document(iChatRoomId)
                .collection("group_profile")
                .document("data")
                .update("participants", FieldValue.arrayUnion(iParticipant));
    }
}
