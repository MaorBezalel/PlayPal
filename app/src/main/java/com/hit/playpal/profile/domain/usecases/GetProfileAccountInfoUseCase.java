package com.hit.playpal.profile.domain.usecases;

import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.hit.playpal.profile.data.repositories.ProfileRepository;

public class GetProfileAccountInfoUseCase {
    private final ProfileRepository mProfileRepository;

    public GetProfileAccountInfoUseCase() {
        this.mProfileRepository = new ProfileRepository();
    }

    public Task<DocumentSnapshot> execute(String iUid) {
        Task<DocumentSnapshot> task = mProfileRepository.getUserByUid(iUid);
        task.addOnFailureListener(e -> {
            // Handle the error here
            Log.e("ProfileAccountInfoUseCase", "Error getting user data", e);
        });
        return task;
    }

}