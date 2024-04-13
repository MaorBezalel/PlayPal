package com.hit.playpal.chatrooms.domain.usecases.chatbody;

import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hit.playpal.chatrooms.domain.repositories.IChatRoomRepository;
import com.hit.playpal.chatrooms.domain.utils.exceptions.MessagesNotFoundException;
import com.hit.playpal.entities.chats.Message;
import com.hit.playpal.utils.Out;

import java.util.List;
import java.util.concurrent.CompletableFuture;

public class FetchMessagesUseCase {
    private final IChatRoomRepository REPOSITORY;

    public FetchMessagesUseCase(IChatRoomRepository iRepository) {
        REPOSITORY = iRepository;
    }

    public CompletableFuture<List<Message>> execute(String iChatRoomId, long iPageSize, DocumentSnapshot iAfterDocument, Out<DocumentSnapshot> oNewLatestDocument)
    {
        CompletableFuture<List<Message>> future = new CompletableFuture<>();

        REPOSITORY.getMessagesInPage(iChatRoomId, iPageSize, iAfterDocument).addOnCompleteListener(task -> {
            if (task.isSuccessful())
            {
                QuerySnapshot querySnapshot = task.getResult();

                if(querySnapshot != null && !querySnapshot.isEmpty())
                {
                    oNewLatestDocument.set(querySnapshot.getDocuments().get(querySnapshot.size() - 1));

                    future.complete(querySnapshot.toObjects(Message.class));
                }
                else
                {
                    future.completeExceptionally(new MessagesNotFoundException());
                }
            }
            else
            {
                future.completeExceptionally(task.getException());
            }
        });

        return future;
    }
}
