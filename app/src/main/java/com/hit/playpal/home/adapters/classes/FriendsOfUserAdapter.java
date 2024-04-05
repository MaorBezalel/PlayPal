package com.hit.playpal.home.adapters.classes;

import androidx.lifecycle.LifecycleOwner;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.hit.playpal.entities.users.Relationship;
import com.hit.playpal.entities.users.enums.RelationshipStatus;
import com.hit.playpal.home.adapters.interfaces.IBindableUser;
import com.hit.playpal.home.adapters.interfaces.IUserAdapter;

public class FriendsOfUserAdapter extends UserAdapter<Relationship>{

    public FriendsOfUserAdapter(IUserAdapter iUserAdapter,  LifecycleOwner iOwner, String iCurrentUserId) {
        super(iUserAdapter, new IBindableUser<Relationship>() {
            @Override
            public String getUserId(Relationship iItem) {
                return iItem.getOther_user().getUid();
            }

            @Override
            public String getUserImage(Relationship iItem) {
                return iItem.getOther_user().getProfilePicture();
            }

            @Override
            public String getDisplayName(Relationship iItem) {
                return iItem.getOther_user().getDisplayName();
            }
        }, iOwner, FirebaseFirestore.getInstance().collection("users").document(iCurrentUserId).collection("relationships").whereEqualTo("status", RelationshipStatus.friends), Relationship.class);

        mUserPrefixPath = "other_user.";
    }
}
