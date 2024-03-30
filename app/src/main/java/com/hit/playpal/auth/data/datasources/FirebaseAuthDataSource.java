package com.hit.playpal.auth.data.datasources;

import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;

public class FirebaseAuthDataSource {
    private final FirebaseAuth AUTH = FirebaseAuth.getInstance();

    public Task<AuthResult> loginWithEmail(String iEmail, String iPassword) {
        return AUTH.signInWithEmailAndPassword(iEmail, iPassword);
    }

    public Task<AuthResult> createUser(String iEmail, String iPassword) {
        return AUTH.createUserWithEmailAndPassword(iEmail, iPassword);
    }

    public FirebaseUser getCurrentUser() {
        return AUTH.getCurrentUser();
    }

    public String getCurrentUserUid() {
        FirebaseUser user = getCurrentUser();

        if (user != null) {
            return user.getUid();
        } else {
            return null;
        }
    }
}
