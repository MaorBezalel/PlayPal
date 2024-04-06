package com.hit.playpal.profile.domain.usecases;

import android.util.Log;

import androidx.annotation.NonNull;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.hit.playpal.entities.users.Relationship;
import com.hit.playpal.profile.data.repositories.ProfileRepository;

import java.text.Collator;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

public class GetFriendListByDisplayNameUseCase {
    private final ProfileRepository mProfileRepository;

    public GetFriendListByDisplayNameUseCase() {
        this.mProfileRepository = new ProfileRepository();
    }

    public Task<List<Relationship>> execute(String iUid, DocumentSnapshot lastVisible, int limit) {
        Task<QuerySnapshot> task = mProfileRepository.getUserFriendsByDisplayName(iUid, lastVisible, limit);
        return task.continueWith(new Continuation<QuerySnapshot, List<Relationship>>() {
            @Override
            public List<Relationship> then(@NonNull Task<QuerySnapshot> task) throws Exception {
                List<DocumentSnapshot> documentSnapshots = task.getResult().getDocuments();

                List<Relationship> relationships = new ArrayList<>();
                for (DocumentSnapshot document : documentSnapshots) {
                    try {
                        Relationship relationship = document.toObject(Relationship.class);
                        assert relationship != null;
                        relationships.add(relationship);
                    } catch (Exception e) {
                        Log.e("GetFriendListByDisplayNameUseCase", "Error processing friend: ", e);
                    }
                }

                // Sort the relationships by the display name of the friends
                relationships.sort((r1, r2) -> r1.getOther_user().getDisplayName().compareToIgnoreCase(r2.getOther_user().getDisplayName()));

                return relationships;
            }
        });
    }
}