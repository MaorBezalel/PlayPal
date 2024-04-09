package com.hit.playpal.settings.domain.repositories;

import android.net.Uri;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.QuerySnapshot;

public interface IUpdateUserProfileRepository {
    Task<Void> updateUserProfile(String iUserId, String iUsername, String iDisplayName, String iAboutMe, String iProfilePicture);

    Task<Uri> uploadProfilePicture(Uri imageUri, String userId);

    Task<Void> updateUserProfileWithoutPicture(String iCurrentUser, String iUserName, String iDisplayName, String iAboutMe);

    Task<QuerySnapshot> getUserByUsername(String iUsername);
}
