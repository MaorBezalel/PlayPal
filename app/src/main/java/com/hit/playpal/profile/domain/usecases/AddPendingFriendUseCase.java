package com.hit.playpal.profile.domain.usecases;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
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

    public void execute(String currentUser,String Uid, Map<String, Object> otherUserData) {
        mGetProfileAccountInfoUseCase.execute(currentUser).addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document != null && document.exists()) {
                        String displayName = document.getString("display_name");
                        String profilePicture = document.getString("profile_picture");

                        Map<String, Object> currentUserData = new HashMap<>();
                        currentUserData.put("display_name", displayName);
                        currentUserData.put("profile_picture", profilePicture);
                        currentUserData.put("uid", currentUser);

                        mProfileRepository.addPendingFriend(Uid, currentUserData);
                        mProfileRepository.addPendingFriend(currentUser, otherUserData);

                    }
                } else {
                    Log.d("AddFriendUseCase", "No such user");
                }
            }
        });
    }
}