package com.hit.playpal.home.ui.adapters.creategroupchat;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.paging.PagingConfig;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.Query;
import com.hit.playpal.R;
import com.hit.playpal.entities.games.Game;
import com.squareup.picasso.Picasso;

public class CreateGroupChatGamesAdapter extends FirestorePagingAdapter<Game, CreateGroupChatGamesAdapter.GameViewHolder> {
    private static final int PAGE_SIZE = 20;
    private static final int PAGE_PREFETCH_DISTANCE = 5;
    private static final PagingConfig PAGING_CONFIG = new PagingConfig(PAGE_SIZE, PAGE_PREFETCH_DISTANCE, false);

    private MaterialCardView mSelectedCardView;

    private Game mSelectedGame;
    public Game getSelectedGame() {
        return mSelectedGame;
    }

    public CreateGroupChatGamesAdapter(Query iQuery, LifecycleOwner iOwner) {
        super(new FirestorePagingOptions.Builder<Game>()
                .setLifecycleOwner(iOwner)
                .setQuery(iQuery, PAGING_CONFIG, Game.class)
                .build()
        );
    }

    public static class GameViewHolder extends RecyclerView.ViewHolder {
        public final MaterialCardView CARD_VIEW;
        public final ImageView GAME_IMAGE_VIEW;
        public final TextView GAME_TITLE_TEXT_VIEW;

        public GameViewHolder(@NonNull View itemView) {
            super(itemView);

            CARD_VIEW = itemView.findViewById(R.id.cardview_create_group_chat_game_card);
            GAME_IMAGE_VIEW = itemView.findViewById(R.id.imageview_create_group_chat_game_image);
            GAME_TITLE_TEXT_VIEW = itemView.findViewById(R.id.textview_create_group_chat_game_title);
        }
    }

    @Override
    protected void onBindViewHolder(@NonNull GameViewHolder iHolder, int iPosition, @NonNull Game iCurrentGame) {
        iHolder.GAME_TITLE_TEXT_VIEW.setText(iCurrentGame.getGameName());
        Picasso.get().load(iCurrentGame.getBackgroundImage()).into(iHolder.GAME_IMAGE_VIEW);

        iHolder.CARD_VIEW.setOnClickListener(v -> {
            if (iHolder.CARD_VIEW.isChecked() && iCurrentGame == mSelectedGame) {
                iHolder.CARD_VIEW.setChecked(false);
                mSelectedGame = null;
                mSelectedCardView = null;
            } else {
                iHolder.CARD_VIEW.setChecked(true);

                if (mSelectedCardView != null) {
                    mSelectedCardView.setChecked(false);
                }

                mSelectedCardView = iHolder.CARD_VIEW;
                mSelectedGame = iCurrentGame;
            }
        });
    }

    @NonNull
    @Override
    public GameViewHolder onCreateViewHolder(@NonNull ViewGroup iParent, int iViewType) {
        View view = View.inflate(iParent.getContext(), R.layout.item_game_for_group_creation, null);
        return new GameViewHolder(view);
    }
}