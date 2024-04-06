package com.hit.playpal.home.adapters.classes;

import androidx.lifecycle.LifecycleOwner;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.hit.playpal.entities.users.User;
import com.hit.playpal.home.adapters.interfaces.IBindableUser;
import com.hit.playpal.home.adapters.interfaces.IUserAdapter;

public class AllUsersAdapter extends UserAdapter<User>{


    public AllUsersAdapter(IUserAdapter iUserAdapter, LifecycleOwner iOwner) {
        super(iUserAdapter, new IBindableUser<User>() {
            @Override
            public String getUserId(User iItem) {
                return iItem.getUid();
            }

            @Override
            public String getUserImage(User iItem) {
                return iItem.getProfilePicture();
            }

            @Override
            public String getDisplayName(User iItem) {
                return iItem.getDisplayName();
            }
        }, iOwner, FirebaseFirestore.getInstance().collection("users"), User.class);

        mUserPrefixPath = "";
    }
}
