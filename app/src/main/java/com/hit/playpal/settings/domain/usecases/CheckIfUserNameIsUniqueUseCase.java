package com.hit.playpal.settings.domain.usecases;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.QuerySnapshot;
import com.hit.playpal.settings.data.repositories.UpdateUserProfileRepository;
import com.hit.playpal.settings.ui.utils.SettingsValidations;
import com.hit.playpal.utils.Out;


public class CheckIfUserNameIsUniqueUseCase {
    private final UpdateUserProfileRepository mUpdateUserProfileRepository;

    public CheckIfUserNameIsUniqueUseCase() {
        this.mUpdateUserProfileRepository = new UpdateUserProfileRepository();
    }

    public Task<Boolean> isUsernameValidAndUnique(String iUsername) {
        Out<String> invalidationReason = Out.of(String.class);
        if (!SettingsValidations.isUsernameValid(iUsername, invalidationReason)) {
            return Tasks.forException(new Exception(invalidationReason.get()));
        }

        return mUpdateUserProfileRepository.getUserByUsername(iUsername)
                .continueWith(task -> {
                    if (task.isSuccessful()) {
                        QuerySnapshot querySnapshot = task.getResult();
                        if (querySnapshot.isEmpty()) {
                            // The username is not taken
                            return true;
                        } else {
                            // The username is taken
                            throw new Exception("Username is already taken.");
                        }
                    } else {
                        // Handle the error
                        throw new Exception("Failed to check username uniqueness.", task.getException());
                    }
                });
    }
}

