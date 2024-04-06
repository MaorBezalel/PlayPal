package com.hit.playpal.chatrooms.data.listeners;

import com.google.firebase.firestore.ListenerRegistration;
import com.hit.playpal.chatrooms.domain.listeners.INewMessageRegistrationListener;

public class FirebaseFirestoreNewMessageRegistrationListener implements INewMessageRegistrationListener {
    private final ListenerRegistration LISTENER;

    public FirebaseFirestoreNewMessageRegistrationListener(ListenerRegistration iListener) {
        LISTENER = iListener;
    }

    @Override
    public void remove() {
        LISTENER.remove();
    }
}
