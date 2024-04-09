package com.hit.playpal.paginatedsearch.games.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.paging.LoadState;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hit.playpal.R;
import com.hit.playpal.entities.games.enums.Genre;
import com.hit.playpal.entities.games.enums.Platform;
import com.hit.playpal.game.ui.activities.GameActivity;
import com.hit.playpal.home.adapters.MultiSelectionAdapter;
import com.hit.playpal.home.adapters.games.AllGamesAdapter;
import com.hit.playpal.profile.adapters.GameFavoriteByUserAdapter;
import com.hit.playpal.paginatedsearch.games.utils.GameFilterOptions;
import com.hit.playpal.paginatedsearch.games.enums.GameFilterType;
import com.hit.playpal.paginatedsearch.games.enums.GameSearchType;
import com.hit.playpal.paginatedsearch.games.adapters.GameAdapter;
import com.hit.playpal.paginatedsearch.games.adapters.IGameAdapter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameSearchFragment extends Fragment {
    private static final String TAG = "GameSearchFragment";
    private static final String ARG_SEARCH_TYPE = "searchType";
    private static final String ARG_USER_ID = "userId";
    private static final String ARG_GAME_ID = "gameId";
    private static final String FILTER_BY_GENRE_PLACEHOLDER = "Select Genres";
    private static final String FILTER_BY_PLATFORM_PLACEHOLDER = "Select Platforms";
    private static final String GAME_SEARCH_ALL_TITLE = "Game Browser";
    private static final String GAME_FAVORITES_SEARCH_TITLE = "Favorite Games";
    // UI ENTITIES:
    private Button mFilterByNameOption;
    private Button mFilterByGenreOption;
    private Button mFilterByPlatformOption;
    private Button mUnFilterOption;
    private Button mFilterButton;

    private SearchView mSearchView;
    private Spinner mGenreFilter;
    private Spinner mPlatformFilter;


    private ProgressBar mProgressBar;
    private TextView mNoGamesFoundError;
    private TextView mDbErrorError;


    // RECYCLER VIEW:
    private RecyclerView mGamesRecyclerView;


    // ADAPTERS & MANAGERS
    private MultiSelectionAdapter mPlatformsAdapter;
    private MultiSelectionAdapter mGenresAdapter;
    private GameAdapter<?> mGameAdapter;
    private LinearLayoutManager mLinearLayoutManager;


    // GAME FILTERING
    private GameFilterType mCurrentGameFilterType;
    private GameFilterOptions mCurrentGameFilterOptions;

    private GameSearchType mCurrentGameSearchType;
    private String mCurrentUserId;

    private TextView mGameSearchTitle;

    private boolean isFragmentFirstCreation = true;



    @Override
    public void onCreate(Bundle iSavedInstanceState) {
        super.onCreate(iSavedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater iInflater, ViewGroup iContainer,
                             Bundle savedInstanceState) {
        View view = iInflater.inflate(R.layout.fragment_search_games, iContainer, false);

        if(getArguments() != null) {
            mCurrentGameSearchType = GameSearchType.valueOf(getArguments().getString(ARG_SEARCH_TYPE));

            if(mCurrentGameSearchType == GameSearchType.FAVORITES) {
                mCurrentUserId = getArguments().getString(ARG_USER_ID);
            }
        }

        initializeComponents(view);

        return view;
    }



    private void initializeComponents(View iView) {
        initializeUIBaseEntities(iView);
        initializeRecyclerView(iView);
        initializeFilteringOptions(iView);
    }

    private void initializeUIBaseEntities(View iView)
    {
        mGameSearchTitle = iView.findViewById(R.id.games_fragment_title);
        mProgressBar = iView.findViewById(R.id.games_fragment_progress_bar);
        mNoGamesFoundError = iView.findViewById(R.id.games_fragment_no_games_found_error);
        mDbErrorError = iView.findViewById(R.id.games_fragment_db_error);

        mFilterByNameOption = iView.findViewById(R.id.games_fragment_searchByNameBtn);
        mFilterByGenreOption = iView.findViewById(R.id.games_fragment_searchByGenreBtn);
        mFilterByPlatformOption = iView.findViewById(R.id.games_fragment_searchByPlatformBtn);
        mUnFilterOption = iView.findViewById(R.id.games_fragment_searchAllBtn);

        mFilterByNameOption.setOnClickListener(this::changeFilterType);
        mFilterByGenreOption.setOnClickListener(this::changeFilterType);
        mFilterByPlatformOption.setOnClickListener(this::changeFilterType);
        mUnFilterOption.setOnClickListener(v -> {
            changeFilterType(v);
            filterGames(v);
        });

        mFilterButton = iView.findViewById(R.id.games_fragment_filterBtn);
        mFilterButton.setOnClickListener(this::filterGames);
    }

    private void initializeRecyclerView(View iView) {
        mLinearLayoutManager = new LinearLayoutManager(requireContext());
        mGamesRecyclerView = iView.findViewById(R.id.games_fragment_gameList);

        switch(mCurrentGameSearchType)
        {
            case ALL:
                mGameAdapter = new AllGamesAdapter(new IGameAdapter() {
                    @Override
                    public void onGameClick(String iGameId) {
                        Intent intent = new Intent(getActivity(), GameActivity.class);
                        intent.putExtra(ARG_GAME_ID, iGameId);
                        startActivity(intent);
                    }
                }, this);

                mGameSearchTitle.setText(GAME_SEARCH_ALL_TITLE);
                break;

                case FAVORITES:
                mGameAdapter = new GameFavoriteByUserAdapter(new IGameAdapter() {
                    @Override
                    public void onGameClick(String iGameId) {
                        Intent intent = new Intent(getActivity(), GameActivity.class);
                        intent.putExtra(ARG_GAME_ID, iGameId);
                        startActivity(intent);
                    }
                }, this, mCurrentUserId);

                mGameSearchTitle.setText(GAME_FAVORITES_SEARCH_TITLE);
                break;
        }

        mGamesRecyclerView.setLayoutManager(mLinearLayoutManager);
        mGamesRecyclerView.setAdapter(mGameAdapter);

        mGameAdapter.addLoadStateListener(loadStates -> {
            if (loadStates.getRefresh() instanceof LoadState.Loading)
            {
                mProgressBar.setVisibility(View.VISIBLE);

                mGamesRecyclerView.setVisibility(View.GONE);
                mNoGamesFoundError.setVisibility(View.GONE);
                mDbErrorError.setVisibility(View.GONE);
            }
            else if (loadStates.getRefresh() instanceof LoadState.Error)
            {
                mProgressBar.setVisibility(View.GONE);
                mGamesRecyclerView.setVisibility(View.GONE);
                mNoGamesFoundError.setVisibility(View.GONE);
                mDbErrorError.setVisibility(View.VISIBLE);
            }
            else if (loadStates.getRefresh() instanceof LoadState.NotLoading)
            {
                if (mGameAdapter.getItemCount() == 0) {
                    mProgressBar.setVisibility(View.GONE);
                    mGamesRecyclerView.setVisibility(View.GONE);
                    mNoGamesFoundError.setVisibility(View.VISIBLE);
                    mDbErrorError.setVisibility(View.GONE);
                }
                else
                {
                    mProgressBar.setVisibility(View.GONE);
                    mGamesRecyclerView.setVisibility(View.VISIBLE);
                    mNoGamesFoundError.setVisibility(View.GONE);
                    mDbErrorError.setVisibility(View.GONE);
                }
            }
            return null;
        });
    }



    private void initializeFilteringOptions(View iView)
    {
        mSearchView = iView.findViewById(R.id.games_fragment_searchBar);
        mGenreFilter = iView.findViewById(R.id.games_fragment_genreBar);
        mPlatformFilter = iView.findViewById(R.id.games_fragment_platformBar);

        CharSequence[] genres = getResources().getTextArray(R.array.game_genres);
        List<CharSequence> genresToDisplay = Arrays.asList(genres);
        mGenresAdapter = new MultiSelectionAdapter(requireContext(), genresToDisplay, FILTER_BY_GENRE_PLACEHOLDER);
        mGenreFilter.setAdapter(mGenresAdapter);

        CharSequence[] platforms = getResources().getTextArray(R.array.game_platforms);
        List<CharSequence> platformsToDisplay = Arrays.asList(platforms);
        mPlatformsAdapter = new MultiSelectionAdapter(requireContext(), platformsToDisplay, FILTER_BY_PLATFORM_PLACEHOLDER);
        mPlatformFilter.setAdapter(mPlatformsAdapter);
    }



    private GameFilterOptions createFilterOptions()
    {
        GameFilterOptions gameFilterOptions = new GameFilterOptions();

        String gameName = mSearchView.getQuery().toString();
        List<Genre> genres = initializeSelectedGenres();
        List<Platform> platforms = initializeSelectedPlatforms();

        switch(mCurrentGameFilterType)
        {
            case BY_NAME:
                gameFilterOptions.setGameName((!gameName.isEmpty()) ? gameName : null);
                break;
            case BY_GENRE:
                gameFilterOptions.setGenre((!genres.isEmpty()) ? genres : null);
                break;
            case BY_PLATFORM:
                gameFilterOptions.setPlatform((!platforms.isEmpty()) ? platforms : null);
            case ALL:
                break;
        }

        return gameFilterOptions;
    }

    private void filterGames(View view) {
        GameFilterOptions gameFilterOptions = createFilterOptions();

        if(gameFilterOptions.equals(mCurrentGameFilterOptions)) return;

        mCurrentGameFilterOptions = gameFilterOptions;

        mGameAdapter.applyFilters(mCurrentGameFilterOptions, mCurrentGameFilterType);

        mSearchView.clearFocus();
    }

    public void changeFilterType(View iView)
    {
        mCurrentGameFilterType = GameFilterType.valueOf(iView.getTag().toString());

        switch(mCurrentGameFilterType)
        {
            case BY_NAME:
                mSearchView.setVisibility(View.VISIBLE);
                mGenreFilter.setVisibility(View.GONE);
                mPlatformFilter.setVisibility(View.GONE);
                mFilterButton.setVisibility(View.VISIBLE);

                mSearchView.setQuery("", false);
                break;

            case BY_GENRE:
                mSearchView.setVisibility(View.GONE);
                mGenreFilter.setVisibility(View.VISIBLE);
                mPlatformFilter.setVisibility(View.GONE);
                mFilterButton.setVisibility(View.VISIBLE);

                mGenresAdapter.uncheckAllItems();
                break;

            case BY_PLATFORM:
                mSearchView.setVisibility(View.GONE);
                mGenreFilter.setVisibility(View.GONE);
                mPlatformFilter.setVisibility(View.VISIBLE);
                mFilterButton.setVisibility(View.VISIBLE);

                mPlatformsAdapter.uncheckAllItems();
                break;

            case ALL:
                mSearchView.setVisibility(View.GONE);
                mGenreFilter.setVisibility(View.GONE);
                mPlatformFilter.setVisibility(View.GONE);
                mFilterButton.setVisibility(View.GONE);
                break;
        }
    }


    private List<Genre> initializeSelectedGenres() {
        boolean[] selectedGenres = mGenresAdapter.getCheckedItems();
        List<Genre> selectedGenresList = new ArrayList<>();

        for (int i = 0; i < selectedGenres.length; i++) {
            if (selectedGenres[i]) {
                selectedGenresList.add(Genre.valueOf(mGenresAdapter.getItem(i).toString().toUpperCase().replace(" ", "_")));
            }
        }

        return selectedGenresList;
    }

    private List<Platform> initializeSelectedPlatforms() {
        boolean[] selectedPlatforms = mPlatformsAdapter.getCheckedItems();
        List<Platform> selectedPlatformsList = new ArrayList<>();

        for (int i = 0; i < selectedPlatforms.length; i++) {
            if (selectedPlatforms[i]) {
                selectedPlatformsList.add(Platform.valueOf(mPlatformsAdapter.getItem(i).toString().toUpperCase().replace(" ", "_")));
            }
        }

        return selectedPlatformsList;
    }
    @Override
    public void onResume() {
        super.onResume();
        if(mGameAdapter != null && !isFragmentFirstCreation && mCurrentGameSearchType == GameSearchType.FAVORITES)
        {
            mGameAdapter.refresh();
        }
        isFragmentFirstCreation = false;
    }
}
