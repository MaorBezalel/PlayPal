package com.hit.playpal.settings.data.datasources;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class SettingsFireBaseFireStoreDataSource {
    private final FirebaseFirestore DB = FirebaseFirestore.getInstance();

    public Task<QuerySnapshot> getUserByUsername(String iUsername) {
        return DB.collection("users").whereEqualTo("username", iUsername).get();
    }

    public Task<Void> updateUserProfile(String iUserId, String iUsername, String iDisplayName, String iAboutMe, String iProfilePicture) {
        Map<String, Object> user = new HashMap<>();
        user.put("username", iUsername);
        user.put("display_name", iDisplayName);
        user.put("about_me", iAboutMe);
        user.put("profile_picture", iProfilePicture);
        return DB.collection("users").document(iUserId).update(user);
    }

    public Task<Void> updateUserProfileWithoutPicture(String iUserId, String iUsername, String iDisplayName, String iAboutMe) {
        Map<String, Object> user = new HashMap<>();
        user.put("username", iUsername);
        user.put("display_name", iDisplayName);
        user.put("about_me", iAboutMe);
        return DB.collection("users").document(iUserId).update(user);
    }
}

