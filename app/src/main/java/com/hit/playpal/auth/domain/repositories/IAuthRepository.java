package com.hit.playpal.auth.domain.repositories;


import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hit.playpal.entities.users.Settings;
import com.hit.playpal.entities.users.User;
import com.hit.playpal.entities.users.UserPrivate;

public interface IAuthRepository {
    Task<AuthResult> loginWithEmail(String iEmail, String iPassword);

    Task<AuthResult> createUser(String iEmail, String iPassword);
    Task<Void> storeUserPublicData(User iUserPublic);
    Task<Void> storeUserPrivateData(String iUid, UserPrivate iUserPrivate);
    Task<Void> storeUserDefaultSettingsData(String iUid, Settings iSettings);

    void forgotPassword(String iEmail);
    boolean isUserAuthenticated();

    Task<DocumentSnapshot> getUserByUid(String iUid);
    Task<DocumentSnapshot> getUserPrivateByUid(String iUid);
    Task<QuerySnapshot> getUserByUsername(String iUsername);
    Task<QuerySnapshot> getUserByEmail(String iEmail);
}
