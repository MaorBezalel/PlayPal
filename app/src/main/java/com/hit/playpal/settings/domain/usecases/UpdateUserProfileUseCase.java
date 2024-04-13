package com.hit.playpal.settings.domain.usecases;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.hit.playpal.entities.users.User;
import com.hit.playpal.settings.data.repositories.UpdateUserProfileRepository;
import com.hit.playpal.utils.CurrentlyLoggedUser;

public class UpdateUserProfileUseCase {
    private final UpdateUserProfileRepository mUpdateUserProfileRepository;
    private final UploadUserProfilePictureUseCase mUploadUserProfilePictureUseCase;

    public UpdateUserProfileUseCase() {
        this.mUpdateUserProfileRepository = new UpdateUserProfileRepository();
        this.mUploadUserProfilePictureUseCase = new UploadUserProfilePictureUseCase();
    }

    public Task<Void> execute(String currentUser, String userName, String displayName, String aboutMe, Uri selectedImageUri) {
        if (selectedImageUri == null) {
            // If selectedImageUri is null, update the user profile without changing the profile picture URL

            CurrentlyLoggedUser.set(new User(currentUser, userName, displayName, CurrentlyLoggedUser.get().getProfilePicture(), aboutMe));

            return mUpdateUserProfileRepository.updateUserProfileWithoutPicture(currentUser, userName, displayName, aboutMe);
        } else {
            // If selectedImageUri is not null, upload the profile picture and update the user profile
            Task<Uri> uploadTask = mUploadUserProfilePictureUseCase.execute(selectedImageUri, currentUser);

            return uploadTask.continueWithTask(task -> {
                if (task.isSuccessful()) {
                    Uri downloadUri = task.getResult();

                    CurrentlyLoggedUser.set(new User(currentUser, userName, displayName, downloadUri.toString(), aboutMe));

                    return mUpdateUserProfileRepository.updateUserProfile(currentUser, userName, displayName, aboutMe, downloadUri.toString());
                } else {
                    throw new RuntimeException("Failed to upload profile picture", task.getException());
                }
            });
        }
    }
}