package com.hit.playpal.settings.data.datasources;

import android.net.Uri;
import com.google.android.gms.tasks.Task;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.Objects;

public class FireBaseStorageDataSource {
    private final FirebaseStorage storage = FirebaseStorage.getInstance();
    private final StorageReference storageReference = storage.getReference();

    public Task<Uri> uploadProfilePicture(Uri imageUri, String userId) {
        StorageReference userProfilePictureRef = storageReference.child("profile_picture/" + userId);
        return userProfilePictureRef.putFile(imageUri).continueWithTask(task -> {
            if (!task.isSuccessful()) {
                throw Objects.requireNonNull(task.getException());
            }
            return userProfilePictureRef.getDownloadUrl();
        });
    }
}
