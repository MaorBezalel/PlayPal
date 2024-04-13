package com.hit.playpal.auth.data.datasources;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hit.playpal.entities.settings.Settings;
import com.hit.playpal.entities.users.User;
import com.hit.playpal.entities.users.UserPrivate;

public class FirebaseFirestoreDataSource {
    private final FirebaseFirestore DB = FirebaseFirestore.getInstance();

    public Task<DocumentSnapshot> getUserByUid(String iUid) {
        return DB.collection("users").document(iUid).get();
    }

    public Task<DocumentSnapshot> getUserPrivateByUid(String iUid) {
        return DB.collection("users").document(iUid).collection("private").document("data").get();
    }

    public Task<QuerySnapshot> getUserByUsername(String iUsername) {
        return DB.collection("users").whereEqualTo("username", iUsername).get();
    }

    public Task<QuerySnapshot> getUserByEmail(String iEmail) {
        return DB.collection("users").whereEqualTo("email", iEmail).get();
    }

    public Task<Void> storeUserPublicData(@NonNull User iUser) {
        return DB.collection("users").document(iUser.getUid()).set(iUser);
    }

    public Task<Void> storeUserPrivateData(String iUid, UserPrivate iUserPrivate) {
        return DB.collection("users").document(iUid).collection("private").document("data").set(iUserPrivate);
    }

    public Task<Void> storeUserDefaultSettingsData(String iUid, Settings iSettings) {
        return DB.collection("users").document(iUid).collection("settings").document("data").set(iSettings);
    }

    public Task<QuerySnapshot> getUserPrivateByEmail(String iEmail) {
        return DB.collection("users").whereEqualTo("email", iEmail).get();
    }
}
