package com.hit.playpal.home.ui.viewmodels;

import android.net.Uri;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.lifecycle.ViewModel;

import com.google.firebase.firestore.Query;
import com.hit.playpal.entities.chats.group.GroupChatRoom;
import com.hit.playpal.entities.chats.group.GroupProfile;
import com.hit.playpal.entities.games.Game;
import com.hit.playpal.entities.users.User;
import com.hit.playpal.home.domain.usecases.chats.CreateGroupChatProfileUseCase;
import com.hit.playpal.home.domain.usecases.chats.CreateGroupChatRoomUseCase;
import com.hit.playpal.home.domain.usecases.chats.StoreGroupChatRoomProfileImageUseCase;
import com.hit.playpal.home.domain.usecases.chats.UploadGroupChatRoomProfileImageUseCase;
import com.hit.playpal.home.domain.usecases.users.GenerateQueryForAllUsersUseCase;
import com.hit.playpal.home.domain.usecases.games.GenerateQueryForAllGamesUseCase;
import com.hit.playpal.utils.CurrentlyLoggedUser;
import com.hit.playpal.utils.UseCaseResult;

import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.atomic.AtomicReference;

public class CreateGroupChatRoomViewModel extends ViewModel {
    private static final String TAG = "CreateGroupChatRoomViewModel";
    private final String THIS_USER_ID = CurrentlyLoggedUser.get().getUid();

    private GroupChatRoom mGroupChatRoom;
    public GroupChatRoom getGroupChatRoom() {
        return mGroupChatRoom;
    }

    private GroupProfile mGroupProfile;
    public GroupProfile getGroupProfile() {
        return mGroupProfile;
    }

    private Uri mGroupPictureUri;
    public Uri getGroupPictureUri() {
        return mGroupPictureUri;
    }

    public CreateGroupChatRoomViewModel() {
        mGroupChatRoom = new GroupChatRoom();
        mGroupProfile = new GroupProfile();
    }

    public void setGroupChatRoomDetails(Uri iGroupPictureUri, String iGroupName, String iGroupDescription) {
        mGroupChatRoom.setName(iGroupName);
        mGroupChatRoom.setDescription(iGroupDescription);
        mGroupProfile.setDescription(iGroupDescription);
        mGroupPictureUri = iGroupPictureUri;
        mGroupChatRoom.setProfilePicture("");
    }
    public void setGroupChatRoomGame(@NonNull Game iGame) {
        mGroupChatRoom.setGame(new GroupChatRoom.Game(iGame.getGameId(), iGame.getGameName(), iGame.getBackgroundImage()));
    }
    public void setGroupChatRoomInitialMembers(User iOwner, Set<User> initialMembers) {
        mGroupChatRoom.setInitialMembers(iOwner, initialMembers);
        mGroupProfile.convertUsersToParticipants(iOwner, initialMembers);
    }

    public Query generateQueryForAllGames() {
        return GenerateQueryForAllGamesUseCase.invoke();
    }

    public Query generateQueryForAllUsers() {
        return GenerateQueryForAllUsersUseCase.invoke(THIS_USER_ID);
    }

    public void createGroupChatRoom(Runnable iOnSuccess, Runnable iOnFailure) {
        if (mGroupPictureUri == null) {
            createGroupChatRoomWithoutProfilePicture(iOnSuccess, iOnFailure);
        } else {
            createGroupChatRoomWithProfilePicture(iOnSuccess, iOnFailure);
        }
    }

    private void createGroupChatRoomWithoutProfilePicture(Runnable iOnSuccess, Runnable iOnFailure) {
        CreateGroupChatRoomUseCase.invoke(mGroupChatRoom)
                // if the group chat room is created successfully, create the group chat profile; otherwise, return a failed result
                .thenCompose(createGroupChatRoomResult -> {
                    if (createGroupChatRoomResult.isSuccessful()) {
                        Log.i(TAG, "createGroupChatRoomWithoutProfilePicture: Group chat room created successfully");
                        return CreateGroupChatProfileUseCase.invoke(createGroupChatRoomResult.getResult(), mGroupProfile);
                    } else {
                        Log.e(TAG, "createGroupChatRoomWithoutProfilePicture: Failed to create group chat room");
                        return CompletableFuture.completedFuture(UseCaseResult.forFailure(createGroupChatRoomResult.getFailure()));
                    }
                })

                // if the group chat profile is created successfully, invoke the onSuccess callback; otherwise, invoke the onFailure callback
                .thenAccept(createGroupProfileResult -> {
                    if (createGroupProfileResult.isSuccessful()) {
                        Log.i(TAG, "createGroupChatRoomWithoutProfilePicture: Group chat profile created successfully");
                        iOnSuccess.run();
                    } else {
                        Log.e(TAG, "createGroupChatRoomWithoutProfilePicture: Failed to create group chat profile");
                        iOnFailure.run();
                    }
                });
    }

    private void createGroupChatRoomWithProfilePicture(Runnable iOnSuccess ,Runnable iOnFailure) {
        AtomicReference<String> groupChatRoomId = new AtomicReference<>();

        CreateGroupChatRoomUseCase.invoke(mGroupChatRoom)
                // if the group chat room is created successfully, create the group chat profile; otherwise, return a failed result
                .thenCompose(createGroupChatRoomResult -> {
                    if (createGroupChatRoomResult.isSuccessful()) {
                        Log.i(TAG, "createGroupChatRoomWithProfilePicture: Group chat room created successfully");
                        groupChatRoomId.set(createGroupChatRoomResult.getResult());
                        return CreateGroupChatProfileUseCase.invoke(groupChatRoomId.get(), mGroupProfile);
                    } else {
                        Log.e(TAG, "createGroupChatRoomWithProfilePicture: Failed to create group chat room");
                        return CompletableFuture.completedFuture(UseCaseResult.forFailure(createGroupChatRoomResult.getFailure()));
                    }
                })

                // if the group chat profile is created successfully, upload the profile picture URI to the Firebase Storage; otherwise, return a failed result
                .thenCompose(createGroupProfileResult -> {
                    if (createGroupProfileResult.isSuccessful()) {
                        Log.i(TAG, "createGroupChatRoomWithProfilePicture: Group chat profile created successfully");
                        return UploadGroupChatRoomProfileImageUseCase.invoke(groupChatRoomId.get(), mGroupPictureUri);
                    } else {
                        Log.e(TAG, "createGroupChatRoomWithProfilePicture: Failed to create group chat profile");
                        return CompletableFuture.completedFuture(UseCaseResult.forFailure(createGroupProfileResult.getFailure()));
                    }
                })

                // if the profile picture is uploaded successfully, store the profile picture URI in the Firestore database; otherwise, return a failed result
                .thenCompose(uploadGroupChatRoomProfileImageResult -> {
                    if (uploadGroupChatRoomProfileImageResult.isSuccessful()) {
                        Log.i(TAG, "createGroupChatRoomWithProfilePicture: Group chat room profile picture uploaded successfully");
                        String profilePictureUrl = uploadGroupChatRoomProfileImageResult.getResult().toString();
                        return StoreGroupChatRoomProfileImageUseCase.invoke(groupChatRoomId.get(), profilePictureUrl);
                    } else {
                        Log.e(TAG, "createGroupChatRoomWithProfilePicture: Failed to upload group chat room profile picture");
                        return CompletableFuture.completedFuture(UseCaseResult.forFailure(uploadGroupChatRoomProfileImageResult.getFailure()));
                    }
                })

                // if the profile picture URI is stored successfully, invoke the onSuccess callback; otherwise, invoke the onFailure callback
                .thenAccept(storeGroupChatRoomProfileImageResult -> {
                    if (storeGroupChatRoomProfileImageResult.isSuccessful()) {
                        Log.i(TAG, "createGroupChatRoomWithProfilePicture: Group chat room profile picture stored successfully");
                        iOnSuccess.run();
                    } else {
                        Log.e(TAG, "createGroupChatRoomWithProfilePicture: Failed to store group chat room profile picture");
                        iOnFailure.run();
                    }
                });
    }
}
