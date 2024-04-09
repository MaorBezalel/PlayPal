package com.hit.playpal.settings.data.repositories;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;
import com.hit.playpal.settings.data.datasources.FireBaseStorageDataSource;
import com.hit.playpal.settings.data.datasources.SettingsFireBaseFireStoreDataSource;
import com.hit.playpal.settings.domain.repositories.IUpdateUserProfileRepository;

public class UpdateUserProfileRepository implements IUpdateUserProfileRepository {
    private final SettingsFireBaseFireStoreDataSource DB = new SettingsFireBaseFireStoreDataSource();
    private final FireBaseStorageDataSource storage = new FireBaseStorageDataSource();

    @Override
    public Task<Uri> uploadProfilePicture(Uri imageUri, String userId) {
        return storage.uploadProfilePicture(imageUri, userId);
    }

    @Override
    public Task<Void> updateUserProfile(String iUserId, String iUsername, String iDisplayName, String iAboutMe, String iProfilePicture) {
        return DB.updateUserProfile(iUserId, iUsername, iDisplayName, iAboutMe, iProfilePicture);
    }

    @Override
    public Task<Void> updateUserProfileWithoutPicture(String iUserId, String iUsername, String iDisplayName, String iAboutMe) {
        return DB.updateUserProfileWithoutPicture(iUserId, iUsername, iDisplayName, iAboutMe);
    }
    @Override
    public Task<QuerySnapshot> getUserByUsername(String iUsername) {
        return DB.getUserByUsername(iUsername);
    }
}
