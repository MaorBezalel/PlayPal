package com.hit.playpal.home.ui.adapters.users;

import androidx.lifecycle.LifecycleOwner;

import com.google.firebase.firestore.FirebaseFirestore;
import com.hit.playpal.paginatedsearch.users.adapters.IUserAdapter;
import com.hit.playpal.entities.users.User;
import com.hit.playpal.paginatedsearch.users.adapters.UserAdapter;
import com.hit.playpal.paginatedsearch.users.utils.IBindableUser;

public class AllUsersAdapter extends UserAdapter<User> {


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
