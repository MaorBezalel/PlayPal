package com.hit.playpal.profile.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.google.firebase.firestore.FirebaseFirestore;
import com.hit.playpal.R;
import com.hit.playpal.entities.games.FavoriteGames;
import com.hit.playpal.entities.users.User;
import com.hit.playpal.game.ui.activities.GameActivity;
import com.hit.playpal.home.adapters.GameAdapter;
import com.hit.playpal.home.adapters.IBindableGame;
import com.hit.playpal.home.adapters.IBindableUser;
import com.hit.playpal.home.adapters.IGameAdapter;
import com.hit.playpal.home.adapters.IUserAdapter;
import com.hit.playpal.home.adapters.UserAdapter;
import com.hit.playpal.profile.ui.activities.ProfileActivity;
public class FavoriteGamesFragment extends Fragment {

    private RecyclerView mRecyclerView;
    private GameAdapter<FavoriteGames> mGameAdapter;
    public FavoriteGamesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            String Uid = getArguments().getString("Uid");
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_favorite_games, container, false);
        Button buttonReturn = view.findViewById(R.id.buttonFavGamesReturn);
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().onBackPressed();
            }
        });

        mRecyclerView = view.findViewById(R.id.recyclerViewFavoriteGames);
        mGameAdapter = new GameAdapter<FavoriteGames>(new IGameAdapter() {
            @Override
            public void onGameClick(String iGameId) {
                Intent intent = new Intent(getContext(), GameActivity.class);
                intent.putExtra("gameId", iGameId);
                startActivity(intent);
            }
        }, new IBindableGame<FavoriteGames>() {
            @Override
            public String getTitle(FavoriteGames item) {
                return item.getGameName();
            }

            @Override
            public float getRating(FavoriteGames item) {
                return item.getGameRating();
            }

            @Override
            public String getBackgroundImage(FavoriteGames item) {
                return item.getGameImage();
            }

            @Override
            public String getId(FavoriteGames item) {
                //return item.getGameId();
                return null;
            }


        },this, FavoriteGames.class, FirebaseFirestore.getInstance().collection("fav_games"));

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mGameAdapter);

        return view;
    }
}