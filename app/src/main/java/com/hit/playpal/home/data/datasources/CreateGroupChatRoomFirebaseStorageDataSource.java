package com.hit.playpal.home.data.datasources;

import android.net.Uri;
import android.util.Log;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

public class CreateGroupChatRoomFirebaseStorageDataSource {
    private final FirebaseStorage STORAGE = FirebaseStorage.getInstance();

    public Task<Uri> uploadGroupChatRoomProfileImage(String iChatRoomId, Uri iImageUri) {
        StorageReference imageReference = STORAGE.getReference().child("group_chat_room_profile_images/" + iChatRoomId);

        return imageReference.putFile(iImageUri).continueWithTask(task -> {
            if (task.isSuccessful()) {
                Log.i("CreateGroupChatRoomFirebaseStorageDataSource", "Image uploaded successfully");
                return imageReference.getDownloadUrl();
            } else {
                Log.e("CreateGroupChatRoomFirebaseStorageDataSource", "Image upload failed", task.getException());
                return Tasks.forException(task.getException());
            }
        });
    }
}
