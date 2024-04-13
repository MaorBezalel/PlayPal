package com.hit.playpal.profile.domain.usecases;

import com.hit.playpal.entities.relationships.OneToOneChatRelationship;
import com.hit.playpal.profile.data.repositories.ProfileRepository;
import com.hit.playpal.profile.domain.repositories.IProfileRepository;

import java.util.concurrent.CompletableFuture;

public class TryToGetOneToOneChatRelationshipUseCase {
    private final IProfileRepository mProfileRepository;

    public TryToGetOneToOneChatRelationshipUseCase(IProfileRepository iProfileRepository) {
        mProfileRepository = iProfileRepository;
    }

    public CompletableFuture<String> execute(String iUid1, String iUid2) {
        CompletableFuture<String> future = new CompletableFuture<>();

        mProfileRepository.tryToGetOneToOneChatRelationship(iUid1, iUid2).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                if (!task.getResult().isEmpty()) {
                    String chatRoomId = task.getResult().getDocuments().get(0).getString("chat_room_id");
                    future.complete(chatRoomId);
                } else {
                    future.complete(null);
                }
            } else {
                future.completeExceptionally(task.getException());
            }
        });

        return future;
    }

    public static CompletableFuture<String> invoke(String iUid1, String iUid2) {
        TryToGetOneToOneChatRelationshipUseCase useCase = new TryToGetOneToOneChatRelationshipUseCase(new ProfileRepository());
        return useCase.execute(iUid1, iUid2);
    }

//    public Task<OneToOneChatRoom> execute(User iUid1, User iUid2) {
//        return mProfileRepository.tryToGetOneToOneChatRelationship(iUid1.getUid(), iUid2.getUid()).continueWithTask(task -> {
//            if (task.isSuccessful()) {
//                if (!task.getResult().isEmpty()) {
//                    String chatRoomId = task.getResult().getDocuments().get(0).getString("chat_room_id");
//                    return mProfileRepository.getTheExistingOneToOneChatRoom(chatRoomId).continueWith(task1 -> task1.getResult().toObject(OneToOneChatRoom.class));
//                } else {
//                    HashMap<String, OneToOneChatRoom.OtherMemberData> otherMemberData = new HashMap<String, OneToOneChatRoom.OtherMemberData>() {{
//                        put(iUid1.getUid(), new OneToOneChatRoom.OtherMemberData(iUid2.getDisplayName(), iUid2.getProfilePicture()));
//                        put(iUid2.getUid(), new OneToOneChatRoom.OtherMemberData(iUid1.getDisplayName(), iUid1.getProfilePicture()));
//                    }};
//                    ArrayList<String> membersUid = new ArrayList<String>() {{
//                        add(iUid1.getUid());
//                        add(iUid2.getUid());
//                    }};
//                    Message lastMessage = new Message();
//
//                    OneToOneChatRoom oneToOneChatRoom = new OneToOneChatRoom(
//                            null,
//                            membersUid,
//                            lastMessage,
//                            otherMemberData
//                    );
//
//                    return mProfileRepository.createAndGetNewOneToOneChatRoom(oneToOneChatRoom).continueWith(task1 -> {
//                        oneToOneChatRoom.setId(task1.getResult().getId());
//                        OneToOneChatRelationship oneToOneChatRelationship = new OneToOneChatRelationship(
//                                OneToOneChatRelationship.joinUids(iUid1.getUid(), iUid2.getUid()),
//                                oneToOneChatRoom.getId()
//                        );
//
//                        return mProfileRepository.createNewOneToOneChatRelationship(oneToOneChatRelationship).);
//                    });
//                }
//            } else {
//                throw task.getException();
//            }
//        });
//    }
}
