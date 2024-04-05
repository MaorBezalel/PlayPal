package com.hit.playpal.profile.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.paging.LoadState;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.TextView;

import com.hit.playpal.R;
import com.hit.playpal.entities.games.FavoriteGames;
import com.hit.playpal.entities.games.enums.Genre;
import com.hit.playpal.entities.games.enums.Platform;
import com.hit.playpal.game.ui.activities.GameActivity;

import com.hit.playpal.home.adapters.classes.GameAdapter;
import com.hit.playpal.home.adapters.classes.GameFavoriteByUserAdapter;
import com.hit.playpal.home.adapters.interfaces.IGameAdapter;
import com.hit.playpal.home.domain.util.GameFilterOptions;
import com.hit.playpal.home.domain.util.GameFilterType;

import java.util.List;

public class FavoriteGamesFragment extends Fragment {

    private String mCurrentUserId;
    private RecyclerView mRecyclerView;
    private GameAdapter<FavoriteGames> mGameAdapter;
    private GameFilterOptions mCurrentGameFilterOptions;
    private SearchView mSearchView;
    private Button mSearchButton;
    private Button mClearButton;
    private ProgressBar mProgressBar;

    private TextView mNoGamesFound;
    private TextView mErrorText;
    private boolean isFragmentFirstCreation = true;
    public FavoriteGamesFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCurrentUserId = getArguments().getString("userId");
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
            public void onClick(View v) {getActivity().onBackPressed();}
        });

        mCurrentGameFilterOptions = new GameFilterOptions();

        mSearchView = view.findViewById(R.id.fragment_favorite_games_search);
        mSearchButton = view.findViewById(R.id.fragment_favorite_games_searchBtn);
        mClearButton = view.findViewById(R.id.fragment_favorite_games_clearBtn);

        mProgressBar = view.findViewById(R.id.fragment_favorite_games_progressBar);
        mNoGamesFound = view.findViewById(R.id.fragment_favorite_games_no_fav_games_found_error);
        mErrorText = view.findViewById(R.id.fragment_favorite_games_db_error);

        mRecyclerView = view.findViewById(R.id.recyclerViewFavoriteGames);
        mGameAdapter = new GameFavoriteByUserAdapter(
                new IGameAdapter() {
                    @Override
                    public void onGameClick(String iGameId) {
                        Intent intent = new Intent(getContext(), GameActivity.class);
                        intent.putExtra("gameId", iGameId);
                        startActivity(intent);
                    }
                }, this, mCurrentUserId);

        mGameAdapter.addLoadStateListener(loadStates -> {
            if(loadStates.getRefresh() instanceof LoadState.NotLoading)
            {
                if(mGameAdapter.getItemCount() == 0)
                {
                    mProgressBar.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.GONE);
                    mNoGamesFound.setVisibility(View.VISIBLE);
                    mErrorText.setVisibility(View.GONE);
                }
                else
                {
                    mProgressBar.setVisibility(View.GONE);
                    mRecyclerView.setVisibility(View.VISIBLE);
                    mNoGamesFound.setVisibility(View.GONE);
                    mErrorText.setVisibility(View.GONE);
                }
            }
            else if (loadStates.getRefresh() instanceof LoadState.Loading)
            {
                mProgressBar.setVisibility(View.VISIBLE);
                mRecyclerView.setVisibility(View.GONE);
                mNoGamesFound.setVisibility(View.GONE);
                mErrorText.setVisibility(View.GONE);
            }
            else if (loadStates.getRefresh() instanceof LoadState.Error)
            {
                mProgressBar.setVisibility(View.GONE);
                mRecyclerView.setVisibility(View.GONE);
                mNoGamesFound.setVisibility(View.GONE);
                mErrorText.setVisibility(View.VISIBLE);
            }
            return null;
        });

        mRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
        mRecyclerView.setAdapter(mGameAdapter);


        mSearchButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameFilterOptions gameFilterOptions = createFilterOptions();

                if(gameFilterOptions.equals(mCurrentGameFilterOptions)) return;

                mCurrentGameFilterOptions = gameFilterOptions;

                mGameAdapter.applyFilters(mCurrentGameFilterOptions, GameFilterType.BY_NAME);
            }
        });

        mClearButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GameFilterOptions gameFilterOptions = createFilterOptions();

                if(gameFilterOptions.getGameName().trim().isEmpty()) return;

                mCurrentGameFilterOptions = gameFilterOptions;

                mGameAdapter.applyFilters(mCurrentGameFilterOptions, GameFilterType.ALL);

                mSearchView.setQuery("", false);

                mSearchView.clearFocus();
            }
        });

        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        if(mGameAdapter != null && !isFragmentFirstCreation)
        {
            mGameAdapter.refresh();
        }
        isFragmentFirstCreation = false;
    }

    private GameFilterOptions createFilterOptions()
    {
        GameFilterOptions gameFilterOptions = new GameFilterOptions();

        String gameName = mSearchView.getQuery().toString();

        if(!gameName.isEmpty())
        {
            gameFilterOptions.setGameName(gameName);
        }

        return gameFilterOptions;
    }
}