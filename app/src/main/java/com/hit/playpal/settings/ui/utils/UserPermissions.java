package com.hit.playpal.settings.ui.utils;

import android.Manifest;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.os.Build;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

public class UserPermissions {
    public static final int PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE = 2;
    public static final int PERMISSIONS_REQUEST_READ_MEDIA_IMAGES = 3;

    public static boolean checkReadExternalStoragePermission(Activity activity) {
        return ContextCompat.checkSelfPermission(activity, Manifest.permission.READ_EXTERNAL_STORAGE) == PackageManager.PERMISSION_GRANTED;
    }

    public static boolean checkReadMediaImagesPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            return ContextCompat.checkSelfPermission(activity, "android.permission.READ_MEDIA_IMAGES") == PackageManager.PERMISSION_GRANTED;
        } else {
            // READ_MEDIA_IMAGES permission does not exist in API levels lower than 31
            return true;
        }
    }

    public static void requestReadExternalStoragePermission(Activity activity) {
        ActivityCompat.requestPermissions(activity, new String[]{Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE);
    }

    public static void requestReadMediaImagesPermission(Activity activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.S) {
            ActivityCompat.requestPermissions(activity, new String[]{"android.permission.READ_MEDIA_IMAGES"}, PERMISSIONS_REQUEST_READ_MEDIA_IMAGES);
        }
    }
}