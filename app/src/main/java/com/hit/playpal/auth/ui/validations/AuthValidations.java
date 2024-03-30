package com.hit.playpal.auth.ui.validations;

import android.util.Patterns;

import androidx.annotation.NonNull;

public class AuthValidations {

    public static boolean isEmailValid(String iEmail) {
        return Patterns.EMAIL_ADDRESS.matcher(iEmail).matches();
    }

    public static boolean isUsernameValid(@NonNull String iUsername) {
        return iUsername.length() >= 2 &&
                iUsername.length() <= 32 &&
                iUsername.matches("^[a-zA-Z0-9._-]*$");
    }

    public static boolean isDisplayNameValid(@NonNull String iDisplayName) {
        return iDisplayName.length() >= 2 && iDisplayName.length() <= 32;
    }

    public static boolean isPasswordValid(@NonNull String iPassword) {
        return iPassword.length() >= 6 && iPassword.length() <= 20;
    }

    public static boolean isConfirmPasswordValid(@NonNull String iPassword, @NonNull String iConfirmPassword) {
        return iPassword.equals(iConfirmPassword);
    }
}
