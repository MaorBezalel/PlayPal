package com.hit.playpal.profile.domain.usecases;

import android.util.Log;


import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.hit.playpal.profile.data.repositories.ProfileRepository;

import java.util.HashMap;
import java.util.Map;

public class AddPendingFriendUseCase {
    private final ProfileRepository mProfileRepository;
    private final GetProfileAccountInfoUseCase mGetProfileAccountInfoUseCase;

    private final SendFriendRequestUseCase mSendFriendRequestUseCase;

    public AddPendingFriendUseCase() {
        this.mProfileRepository = new ProfileRepository();
        this.mGetProfileAccountInfoUseCase = new GetProfileAccountInfoUseCase();
        this.mSendFriendRequestUseCase = new SendFriendRequestUseCase(mProfileRepository);
    }

    public Task<Void> execute(String currentUser,String Uid, Map<String, Object> otherUserData) {
        return mGetProfileAccountInfoUseCase.execute(currentUser).continueWithTask(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    String displayName = document.getString("display_name");
                    String profilePicture = document.getString("profile_picture");
                    String userName = document.getString("username");

                    Map<String, Object> currentUserData = new HashMap<>();
                    currentUserData.put("display_name", displayName);
                    currentUserData.put("profile_picture", profilePicture);
                    currentUserData.put("uid", currentUser);
                    currentUserData.put("username", userName);


                    Task<Void> task0 = mSendFriendRequestUseCase.sendFriendRequest(Uid, currentUser, displayName, profilePicture);
                    Task<Void> task1 = mProfileRepository.addPendingFriend(Uid, currentUserData);
                    Task<Void> task2 = mProfileRepository.addPendingFriend(currentUser, otherUserData);
                    return Tasks.whenAllSuccess(task0,task1, task2).continueWith(taskInner -> null);
                } else {
                    Log.d("AddFriendUseCase", "No such user");
                    return Tasks.forException(new Exception("No such user"));
                }
            } else {
                Log.d("AddFriendUseCase", "Failed to get user profile");
                return Tasks.forException(new Exception("Failed to get user profile"));
            }
        });
    }
}