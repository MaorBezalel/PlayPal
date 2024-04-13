package com.hit.playpal.settings.ui.utils;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import androidx.annotation.NonNull;

import com.hit.playpal.utils.Out;

import java.io.File;

public class SettingsValidations {
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

    public static boolean isAboutMeValid(@NonNull String iAboutMe, Out<String> oInvalidationReason) {
        boolean isValid = false;

         if (!isAboutMeInCorrectLength(iAboutMe)) {
            oInvalidationReason.set("About me can not exceed 300 characters.");
        } else {
            isValid = true;
            oInvalidationReason.set(null);
        }

        return isValid;
    }

    private static boolean isAboutMeInCorrectLength(String iAboutMe) {
        return  iAboutMe.length() <= 300;
    }

    public static boolean isAvatarImageValid(@NonNull String iAvatarImage, Out<String> oInvalidationReason) {
        boolean isValid = false;

 {
            if (!isAvatarImageSizeValid(iAvatarImage)) {
                oInvalidationReason.set("Avatar image file size is too large. Maximum size is 5MB.");
            } else if (!isAvatarImageTypeValid(iAvatarImage)) {
                oInvalidationReason.set("Invalid file type. Only JPG, JPEG and PNG images are allowed.");
            } else {
                isValid = true;
                oInvalidationReason.set(null);
            }
        }

        return isValid;
    }

    private static boolean isAvatarImageSizeValid(String iAvatarImage) {
        File file = new File(iAvatarImage);
        long fileSizeInKB = file.length() / 1024;
        long fileSizeInMB = fileSizeInKB / 1024;
        return fileSizeInMB <= 5;
    }

    private static boolean isAvatarImageTypeValid(String iAvatarImage) {
        String fileExtension = iAvatarImage.substring(iAvatarImage.lastIndexOf(".") + 1);
        return fileExtension.equalsIgnoreCase("jpeg") || fileExtension.equalsIgnoreCase("png") || fileExtension.equalsIgnoreCase("jpg");
    }


}
