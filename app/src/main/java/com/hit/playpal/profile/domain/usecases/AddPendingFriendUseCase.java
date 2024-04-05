package com.hit.playpal.profile.domain.usecases;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.hit.playpal.profile.data.repositories.ProfileRepository;

import java.util.HashMap;
import java.util.Map;

public class AddPendingFriendUseCase {
    private ProfileRepository mProfileRepository;
    private GetProfileAccountInfoUseCase mGetProfileAccountInfoUseCase;

    public AddPendingFriendUseCase() {
        this.mProfileRepository = new ProfileRepository();
        this.mGetProfileAccountInfoUseCase = new GetProfileAccountInfoUseCase();
    }

    public Task<Void> execute(String currentUser,String Uid, Map<String, Object> otherUserData) {
        return mGetProfileAccountInfoUseCase.execute(currentUser).continueWithTask(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document != null && document.exists()) {
                    String displayName = document.getString("display_name");
                    String profilePicture = document.getString("profile_picture");

                    Map<String, Object> currentUserData = new HashMap<>();
                    currentUserData.put("display_name", displayName);
                    currentUserData.put("profile_picture", profilePicture);
                    currentUserData.put("uid", currentUser);

                    Task<Void> task1 = mProfileRepository.addPendingFriend(Uid, currentUserData);
                    Task<Void> task2 = mProfileRepository.addPendingFriend(currentUser, otherUserData);
                    return Tasks.whenAllSuccess(task1, task2).continueWith(taskInner -> null);
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