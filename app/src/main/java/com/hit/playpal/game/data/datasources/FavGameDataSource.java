package com.hit.playpal.game.data.datasources;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hit.playpal.entities.games.FavoriteGames;
import com.hit.playpal.entities.users.User;
import com.hit.playpal.game.data.utils.exceptions.DatabaseErrorException;

import java.util.List;

public class FavGameDataSource {
    private final CollectionReference favGameInstance = FirebaseFirestore.getInstance().collection("fav_games");

    public Task<Void> addGameToFavorites(String iGameName, User iCurrentlyLoggedUser) {

        return favGameInstance.
                add(new FavoriteGames(iGameName, iCurrentlyLoggedUser)).
                continueWithTask(task -> {
            if (task.isSuccessful()) {
                return Tasks.forResult(null);
            } else {
                return Tasks.forException(task.getException());
            }
        });
    }

    public Task<Void> removeGameFromFavorites(String iDocumentId)
    {
        return favGameInstance.
                document(iDocumentId).
                delete();
    }


    public Task<QuerySnapshot> getGameFavoriteStatus(String iGameName, User iCurrentlyLoggedUser)
    {
        return favGameInstance.
                whereEqualTo("game_name", iGameName).
                whereEqualTo("user", iCurrentlyLoggedUser).
                get();
    }



}
