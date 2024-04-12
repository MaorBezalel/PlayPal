package com.hit.playpal.auth.ui.validations;

import android.util.Log;
import android.util.Patterns;

import androidx.annotation.NonNull;

import com.hit.playpal.utils.Out;

import org.jetbrains.annotations.Contract;

public class AuthValidations {

    public static boolean isEmailValid(@NonNull String iEmail, Out<String> oInvalidationReason) {
        boolean isValid = false;

        if (isEmailEmpty(iEmail)) {
            oInvalidationReason.set("Email cannot be empty.");
        } else if (!isEmailInCorrectFormat(iEmail)) {
            oInvalidationReason.set("Email is not in the correct format.");
        } else {
            isValid = true;
            oInvalidationReason.set(null);
        }

        return isValid;
    }
    @Contract(pure = true)
    private static boolean isEmailEmpty(@NonNull String iEmail) {
        return iEmail.isEmpty();
    }
    private static boolean isEmailInCorrectFormat(@NonNull String iEmail) {
        return Patterns.EMAIL_ADDRESS.matcher(iEmail).matches();
    }

    public static boolean isUsernameValid(@NonNull String iUsername, Out<String> oInvalidationReason) {
        boolean isValid = false;

        if (isUsernameEmpty(iUsername)) {
            oInvalidationReason.set("Username cannot be empty.");
        } else if (!isUsernameInCorrectLength(iUsername)) {
            oInvalidationReason.set("Username must be between 2 and 20 characters.");
        } else if (!isUsernameInCorrectFormat(iUsername)) {
            oInvalidationReason.set("Username can only contain letters, numbers, and the following characters: . _ -");
        } else {
            isValid = true;
            oInvalidationReason.set(null);
        }

        return isValid;
    }
    private static boolean isUsernameEmpty(@NonNull String iUsername) {
        return iUsername.isEmpty();
    }
    private static boolean isUsernameInCorrectLength(@NonNull String iUsername) {
        return iUsername.length() >= 2 && iUsername.length() <= 20;
    }
    private static boolean isUsernameInCorrectFormat(@NonNull String iUsername) {
        return iUsername.matches("^[a-zA-Z0-9._-]*$");
    }

    public static boolean isEmailOrUsernameValid(@NonNull String iEmailOrUsername, Out<String> oInvalidationReason) {
        boolean isValid = false;

        if (isEmailOrUsernameEmpty(iEmailOrUsername)) {
            oInvalidationReason.set("Email/Username cannot be empty.");
        } else if (!isEmailValid(iEmailOrUsername, Out.notRequired()) && !isUsernameValid(iEmailOrUsername, Out.notRequired())) {
            oInvalidationReason.set("Email/Username is not in the correct format.");
        } else {
            isValid = true;
            oInvalidationReason.set(null);
        }

        return isValid;
    }
    private static boolean isEmailOrUsernameEmpty(@NonNull String iEmailOrUsername) {
        return iEmailOrUsername.isEmpty();
    }

    public static boolean isDisplayNameValid(@NonNull String iDisplayName, Out<String> oInvalidationReason) {
        boolean isValid = false;

        if (isDisplayNameEmpty(iDisplayName)) {
            oInvalidationReason.set("Display name cannot be empty.");
        } else if (!isDisplayNameInCorrectLength(iDisplayName)) {
            oInvalidationReason.set("Display name must be between 2 and 20 characters.");
        } else {
            isValid = true;
            oInvalidationReason.set(null);
        }

        return isValid;
    }
    private static boolean isDisplayNameEmpty(@NonNull String iDisplayName) {
        return iDisplayName.isEmpty();
    }
    private static boolean isDisplayNameInCorrectLength(@NonNull String iDisplayName) {
        return iDisplayName.length() >= 2 && iDisplayName.length() <= 20;
    }

    public static boolean isPasswordValid(@NonNull String iPassword, Out<String> oInvalidationReason) {
        boolean isValid = false;

        if (isPasswordEmpty(iPassword)) {
            oInvalidationReason.set("Password cannot be empty.");
        } else if (!isPasswordInCorrectLength(iPassword)) {
            oInvalidationReason.set("Password must be between 6 and 20 characters.");
        } else {
            isValid = true;
            oInvalidationReason.set(null);
        }

        return isValid;
    }
    private static boolean isPasswordEmpty(@NonNull String iPassword) {
        return iPassword.isEmpty();
    }
    private static boolean isPasswordInCorrectLength(@NonNull String iPassword) {
        return iPassword.length() >= 6 && iPassword.length() <= 20;
    }

    public static boolean isConfirmPasswordValid(@NonNull String iPassword, @NonNull String iConfirmPassword, Out<String> oInvalidationReason) {
        boolean isValid = false;

        if (isConfirmPasswordEmpty(iConfirmPassword)) {
            oInvalidationReason.set("Confirm password cannot be empty.");
        } else if (!isConfirmPasswordEqualToPassword(iPassword, iConfirmPassword)) {
            oInvalidationReason.set("Passwords do not match.");
        } else {
            isValid = true;
            oInvalidationReason.set(null);
        }

        return isValid;
    }
    private static boolean isConfirmPasswordEmpty(@NonNull String iConfirmPassword) {
        return iConfirmPassword.isEmpty();
    }
    private static boolean isConfirmPasswordEqualToPassword(@NonNull String iPassword, @NonNull String iConfirmPassword) {
        return iPassword.equals(iConfirmPassword);
    }
}
