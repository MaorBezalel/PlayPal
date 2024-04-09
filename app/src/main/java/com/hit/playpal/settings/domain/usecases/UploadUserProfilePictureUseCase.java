package com.hit.playpal.settings.domain.usecases;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.hit.playpal.settings.data.repositories.UpdateUserProfileRepository;

public class UploadUserProfilePictureUseCase {
    private final UpdateUserProfileRepository mUpdateUserProfileRepository;

    public UploadUserProfilePictureUseCase() {
        this.mUpdateUserProfileRepository = new UpdateUserProfileRepository();
    }

    public Task<Uri> execute(Uri selectedImageUri, String userId) {
        return mUpdateUserProfileRepository.uploadProfilePicture(selectedImageUri, userId);
    }
}
