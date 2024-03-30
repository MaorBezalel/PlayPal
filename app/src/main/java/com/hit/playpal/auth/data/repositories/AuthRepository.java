package com.hit.playpal.auth.data.repositories;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hit.playpal.auth.data.datasources.FirebaseAuthDataSource;
import com.hit.playpal.auth.data.datasources.FirebaseFirestoreDataSource;
import com.hit.playpal.auth.domain.repositories.IAuthRepository;
import com.hit.playpal.auth.domain.utils.exceptions.EmailAlreadyTakenException;
import com.hit.playpal.auth.domain.utils.exceptions.InternalErrorException;
import com.hit.playpal.auth.domain.utils.exceptions.InvalidDetailsException;
import com.hit.playpal.entities.users.Settings;
import com.hit.playpal.entities.users.User;
import com.hit.playpal.entities.users.UserPrivate;

public class AuthRepository implements IAuthRepository {
    private final FirebaseAuthDataSource AUTH = new FirebaseAuthDataSource();
    private final FirebaseFirestoreDataSource DB = new FirebaseFirestoreDataSource();

    private static void handleDataSourceFailure(Exception e) {
        if (e instanceof FirebaseAuthException) {
            handleFirebaseAuthFailure((FirebaseAuthException) e);
        } else {
            throw new InternalErrorException();
        }
    }

    private static void handleFirebaseAuthFailure(@NonNull FirebaseAuthException e) {
        if (e.getErrorCode().equals("ERROR_EMAIL_ALREADY_IN_USE")) {
            throw new EmailAlreadyTakenException();
        } else if (e.getErrorCode().equals("ERROR_WRONG_PASSWORD") || e.getErrorCode().equals("ERROR_USER_NOT_FOUND")) {
            throw new InvalidDetailsException();
        } else {
            throw new InternalErrorException();
        }
    }

    @Override
    public Task<AuthResult> loginWithEmail(String iEmail, String iPassword) {
        return AUTH.loginWithEmail(iEmail, iPassword);
    }

    @Override
    public Task<AuthResult> createUser(String iEmail, String iPassword) {
        return AUTH.createUser(iEmail, iPassword).addOnFailureListener(AuthRepository::handleDataSourceFailure);
    }

    @Override
    public Task<Void> storeUserPublicData(User iUserPublic) {
        return DB.storeUserPublicData(iUserPublic);
    }

    @Override
    public Task<Void> storeUserPrivateData(String iUid, UserPrivate iUserPrivate) {
        return DB.storeUserPrivateData(iUid, iUserPrivate);
    }

    @Override
    public Task<Void> storeUserDefaultSettingsData(String iUid, Settings iSettings) {
        return DB.storeUserDefaultSettingsData(iUid, iSettings);
    }

    @Override
    public void forgotPassword(String iEmail) {

    }

    @Override
    public boolean isUserAuthenticated() {
        return false;
    }

    @Override
    public Task<DocumentSnapshot> getUserByUid(String iUid) {
        return DB.getUserByUid(iUid);
    }

    @Override
    public Task<DocumentSnapshot> getUserPrivateByUid(String iUid) {
        return DB.getUserPrivateByUid(iUid);
    }

    @Override
    public Task<QuerySnapshot> getUserByUsername(String iUsername) {
        return DB.getUserByUsername(iUsername);
    }

    @Override
    public Task<QuerySnapshot> getUserByEmail(String iEmail) {
        return DB.getUserByEmail(iEmail);
    }
}
