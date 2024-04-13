package com.hit.playpal.auth.data.repositories;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuthException;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hit.playpal.auth.data.datasources.FirebaseAuthDataSource;
import com.hit.playpal.auth.data.datasources.FirebaseFirestoreDataSource;
import com.hit.playpal.auth.domain.repositories.IAuthRepository;
import com.hit.playpal.auth.domain.utils.exceptions.DisabledAccountException;
import com.hit.playpal.auth.domain.utils.exceptions.EmailAlreadyTakenException;
import com.hit.playpal.auth.domain.utils.exceptions.InternalErrorException;
import com.hit.playpal.auth.domain.utils.exceptions.InvalidDetailsException;
import com.hit.playpal.entities.settings.Settings;
import com.hit.playpal.entities.users.User;
import com.hit.playpal.entities.users.UserPrivate;

public class AuthRepository implements IAuthRepository {
    private final FirebaseAuthDataSource AUTH = new FirebaseAuthDataSource();
    private final FirebaseFirestoreDataSource DB = new FirebaseFirestoreDataSource();

    @NonNull
    private Task<AuthResult> interpretFirebaseAuthTaskBeforeSendingItBackToTheDomain(@NonNull Task<AuthResult> iTask) {
        Task<AuthResult> interpretedTask = null;

        if (iTask.isSuccessful()) {
            interpretedTask = iTask;
        } else if (!(iTask.getException() instanceof FirebaseAuthException)) {
            interpretedTask = Tasks.forException(new InternalErrorException());
        } else {
            String errorCode = ((FirebaseAuthException) iTask.getException()).getErrorCode();

            switch (errorCode) {
                case "ERROR_EMAIL_ALREADY_IN_USE":
                    interpretedTask = Tasks.forException(new EmailAlreadyTakenException());
                    break;
                case "ERROR_WRONG_PASSWORD":
                case "ERROR_USER_NOT_FOUND":
                case "ERROR_INVALID_CREDENTIAL":
                    interpretedTask = Tasks.forException(new InvalidDetailsException());
                    break;
                case "ERROR_USER_DISABLED":
                    interpretedTask = Tasks.forException(new DisabledAccountException());
                    break;
                default:
                    interpretedTask = Tasks.forException(new InternalErrorException());
                    break;
            }
        }

        return interpretedTask;
    }

    @Override
    public Task<AuthResult> loginWithEmail(String iEmail, String iPassword) {
        return AUTH.loginWithEmail(iEmail, iPassword).continueWithTask(this::interpretFirebaseAuthTaskBeforeSendingItBackToTheDomain);
    }

    @Override
    public Task<AuthResult> createUser(String iEmail, String iPassword) {
        return AUTH.createUser(iEmail, iPassword).continueWithTask(this::interpretFirebaseAuthTaskBeforeSendingItBackToTheDomain);
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
    public Task<Void> resetPassword(String iEmail) {
        return AUTH.resetPassword(iEmail).continueWithTask(task -> {
            if (task.isSuccessful()) {
                Log.d("AuthRepository", "Password reset email sent successfully");
                return task;
            } else {
                if (task.getException() instanceof FirebaseAuthException) {
                    String errorCode = ((FirebaseAuthException) task.getException()).getErrorCode();

                    if (errorCode.equals("ERROR_USER_NOT_FOUND")) {
                        return Tasks.forException(new InvalidDetailsException());
                    } else {
                        return Tasks.forException(new InternalErrorException());
                    }
                } else {
                    return Tasks.forException(new InternalErrorException());
                }
            }
        });
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

    @Override
    public Task<QuerySnapshot> getUserPrivateByEmail(String iEmail) {
        return DB.getUserPrivateByEmail(iEmail);
    }
}
