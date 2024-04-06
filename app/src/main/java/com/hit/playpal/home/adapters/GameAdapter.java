package com.hit.playpal.home.adapters;

import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.cardview.widget.CardView;
import androidx.lifecycle.LifecycleOwner;
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

public class GameAdapter<T> extends FirestorePagingAdapter<T, GameAdapter.GameViewHolder>{
    private static final int PAGE_SIZE = 20;
    private static final int PAGE_PREFETCH_DISTANCE = 5;
    private static final int EXCELLENT_RATING_COLOR = Color.rgb(0, 128, 0);
    private static final int GOOD_RATING_COLOR = Color.rgb(173, 255, 47);
    private static final int OKAY_RATING_COLOR = Color.rgb(255, 255, 0);
    private static final int POOR_RATING_COLOR = Color.rgb(255, 165, 0);
    private static final int EXTREMELY_POOR_RATING_COLOR = Color.rgb(255, 0, 0);
    private Query mBaseQuery ;
    private static final PagingConfig PAGING_CONFIG = new PagingConfig(PAGE_SIZE, PAGE_PREFETCH_DISTANCE, false);
    private final IGameAdapter mGameAdapter;
    private final IBindableGame<T> mBindable;
    private final Class<T> mItemClass;
    private final LifecycleOwner mCurrentLifecycleOwner;
    public GameAdapter(
            IGameAdapter iGameAdapter,
            IBindableGame<T> iBindable ,
            LifecycleOwner iOwner,
            Class<T> itemClass,
            Query iBaseQuery) {
        super(new FirestorePagingOptions.Builder<T>()
                .setLifecycleOwner(iOwner)
                .setQuery(
                        iBaseQuery,
                        PAGING_CONFIG,
                        itemClass)
                .build());

        this.mGameAdapter = iGameAdapter;
        this.mBindable = iBindable;
        this.mItemClass = itemClass;
        this.mBaseQuery = iBaseQuery;
        this.mCurrentLifecycleOwner = iOwner;
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
            gameTitle = itemView.findViewById(R.id.fragment_games_title);
            gameImage = itemView.findViewById(R.id.fragment_games_image);
            gameRating = itemView.findViewById(R.id.gameRating);
        }
    }


    @Override
    protected void onBindViewHolder(@NonNull GameViewHolder iGameViewHolder, int i, @NonNull T iItem) {
        iGameViewHolder.gameTitle.setText(this.mBindable.getTitle(iItem));
        iGameViewHolder.gameRating.setText(new DecimalFormat("0.00").format(this.mBindable.getRating(iItem)));
        iGameViewHolder.gameRating.setTextColor(getRatingColor(this.mBindable.getRating(iItem)));

        if (this.mBindable.getBackgroundImage(iItem) != null)
        {
            Picasso.get().load(this.mBindable.getBackgroundImage(iItem)).into(iGameViewHolder.gameImage);
        }
        else
        {
            iGameViewHolder.gameImage.setImageResource(R.drawable.ic_unknown_game_image);

        }

        iGameViewHolder.gameCard.setOnClickListener(v -> {
            if(mGameAdapter == null) return;
            mGameAdapter.onGameClick(this.mBindable.getId(iItem));
        });
    }


    public void applyFilters(GameFilterOptions iFilteringOptions, GameFilterType iFilteringType) {

        Query filteredQuery = mBaseQuery;

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
        
        FirestorePagingOptions<T> newOptions = new FirestorePagingOptions.Builder<T>()
                .setLifecycleOwner(mCurrentLifecycleOwner)
                .setQuery(
                        filteredQuery,
                        PAGING_CONFIG,
                        mItemClass)
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
