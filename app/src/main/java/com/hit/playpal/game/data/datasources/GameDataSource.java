package com.hit.playpal.game.data.datasources;

import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.Collection;

public class GameDataSource {
    private static CollectionReference sGameInstance = FirebaseFirestore.getInstance().collection("games");


    public Task<DocumentSnapshot> getGame(String iGameId)
    {
        return sGameInstance.document(iGameId).get();
    }
}
