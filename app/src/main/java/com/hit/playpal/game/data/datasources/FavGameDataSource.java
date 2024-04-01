package com.hit.playpal.game.data.datasources;

import com.google.android.gms.tasks.Task;
import com.google.android.gms.tasks.Tasks;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.hit.playpal.entities.games.FavoriteGames;
import com.hit.playpal.entities.users.User;

public class FavGameDataSource {
    private static CollectionReference sFavGameInstance = FirebaseFirestore.getInstance().collection("fav_games");

    // TODO: Delete after AndroidViewModel is implemented
    private static User sCurrentUser = new User("123", "username", "displayname", "picture", "aboutme");

    public Task<Void> addGameToFavorites(String iGameName)
    {

            FavoriteGames favGame = new FavoriteGames();
            favGame.setGameName(iGameName);
            favGame.setUser(sCurrentUser);

            return sFavGameInstance.add(favGame).continueWith(task -> {
                if (task.isSuccessful())
                {
                    return null;
                }
                else
                {
                    throw new Exception("Failed to add game to favorites");
                }
            });

    }

    public Task<QuerySnapshot> getGameFavoriteStatus(String iGameName)
    {
        return sFavGameInstance.
                whereEqualTo("game_name", iGameName).
                whereEqualTo("user.uid",sCurrentUser).get();
    }



}
