package com.hit.playpal.home.adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.paging.PagingConfig;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.hit.playpal.R;
import com.hit.playpal.entities.games.Game;
import com.hit.playpal.home.domain.util.GameFilterOptions;
import com.hit.playpal.home.domain.util.GameFilterType;
import com.hit.playpal.home.ui.fragments.GamesFragment;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;

public class GameAdapter extends FirestorePagingAdapter<Game, GameAdapter.GameViewHolder>{
    private static final int PAGE_SIZE = 20;
    private static final int PAGE_PREFETCH_DISTANCE = 5;
    private static final int EXCELLENT_RATING_COLOR = Color.rgb(0, 128, 0);
    private static final int GOOD_RATING_COLOR = Color.rgb(173, 255, 47);
    private static final int OKAY_RATING_COLOR = Color.rgb(255, 255, 0);
    private static final int POOR_RATING_COLOR = Color.rgb(255, 165, 0);
    private static final int EXTREMELY_POOR_RATING_COLOR = Color.rgb(255, 0, 0);
    private static final Query BASE_QUERY = FirebaseFirestore.getInstance().collection("games");
    private static final PagingConfig PAGING_CONFIG = new PagingConfig(PAGE_SIZE, PAGE_PREFETCH_DISTANCE, false);
    private final IGameAdapter iGameAdapter;
    public GameAdapter(IGameAdapter iGameAdapter, GamesFragment iGamesFragmentOwner) {
        super(new FirestorePagingOptions.Builder<Game>()
                .setLifecycleOwner(iGamesFragmentOwner)
                .setQuery(
                        BASE_QUERY,
                        PAGING_CONFIG,
                        Game.class)
                .build());

        this.iGameAdapter = iGameAdapter;
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup iParent, int iViewType) {
        View view = LayoutInflater.from(iParent.getContext()).inflate(R.layout.fragment_games_card, iParent, false);
        return new GameViewHolder(view);
    }

    public static class GameViewHolder extends RecyclerView.ViewHolder {
        public final CardView gameCard;
        public final TextView gameTitle;
        public final ImageView gameImage;
        public final TextView gameRating;

        public GameViewHolder(View view) {
            super(view);
            gameCard = itemView.findViewById(R.id.gameCard);
            gameTitle = itemView.findViewById(R.id.gameTitle);
            gameImage = itemView.findViewById(R.id.gameImage);
            gameRating = itemView.findViewById(R.id.gameRating);
        }
    }


    @Override
    protected void onBindViewHolder(@NonNull GameViewHolder iGameViewHolder, int i, @NonNull Game iGame) {

        iGameViewHolder.gameTitle.setText(iGame.getGameName());
        iGameViewHolder.gameRating.setText(new DecimalFormat("0.00").format(iGame.getRating()));
        iGameViewHolder.gameRating.setTextColor(getRatingColor(iGame.getRating()));

        if (iGame.getBackgroundImage() != null)
        {
            Picasso.get().load(iGame.getBackgroundImage()).into(iGameViewHolder.gameImage);
        }
        else
        {
            iGameViewHolder.gameImage.setImageResource(R.drawable.ic_unknown_game_image);

        }

        iGameViewHolder.gameCard.setOnClickListener(v -> {
            if(iGameAdapter == null) return;
            iGameAdapter.onGameClick(iGame.getGameId());
        });
    }


    public void applyFilters(GameFilterOptions iFilteringOptions, GameFilterType iFilteringType, GamesFragment gamesFragmentOwner) {

        Query filteredQuery = BASE_QUERY;

        if (iFilteringOptions != null)
        {
            switch(iFilteringType)
            {
                case BY_NAME:
                    filteredQuery = (iFilteringOptions.getGameName() != null) ?
                            filteredQuery.
                                    orderBy("game_name")
                                    .startAt(iFilteringOptions.getGameName())
                                    .endAt(iFilteringOptions.getGameName() + "\uf8ff")
                            : filteredQuery;
                    break;
                case BY_GENRE:
                    filteredQuery = (iFilteringOptions.getGenre() != null) ?
                            filteredQuery.
                                    whereArrayContainsAny("genres", iFilteringOptions.getGenre())
                            : filteredQuery;
                    break;
                case BY_PLATFORM:
                    filteredQuery = (iFilteringOptions.getPlatform() != null) ?
                            filteredQuery.
                                    whereArrayContainsAny("platforms", iFilteringOptions.getPlatform()) : filteredQuery;
                    break;
                case ALL:
                    break;
            }
        }
        
        FirestorePagingOptions<Game> newOptions = new FirestorePagingOptions.Builder<Game>()
                .setLifecycleOwner(gamesFragmentOwner)
                .setQuery(
                        filteredQuery,
                        PAGING_CONFIG,
                        Game.class)
                .build();

        super.updateOptions(newOptions);
    }

    private int getRatingColor(float iRating)
    {
        if (iRating >= 4.5)
        {
            return EXCELLENT_RATING_COLOR;
        }
        else if (iRating >= 4.0)
        {
            return GOOD_RATING_COLOR;
        }
        else if (iRating >= 3.0)
        {
            return OKAY_RATING_COLOR;
        }
        else if (iRating >= 2.0)
        {
            return POOR_RATING_COLOR;
        }
        else
        {
            return EXTREMELY_POOR_RATING_COLOR;
        }
    }

}
