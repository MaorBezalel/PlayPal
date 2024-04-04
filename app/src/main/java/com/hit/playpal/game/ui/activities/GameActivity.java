package com.hit.playpal.game.ui.activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.res.ResourcesCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.lifecycle.ViewModelProvider;

import com.hit.playpal.R;
import com.hit.playpal.entities.games.Game;
import com.hit.playpal.entities.games.enums.Genre;
import com.hit.playpal.entities.games.enums.Platform;
import com.hit.playpal.game.ui.viewmodels.GameViewModel;
import com.squareup.picasso.Picasso;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

public class GameActivity extends AppCompatActivity {

    private String mCurrentGameId;
    private ImageView mGameImage;
    private TextView mGameName;
    private TextView mGameRating;
    private TextView mGameReleaseDate;
    private GameViewModel mGameViewModel;
    private LinearLayout mGameAddToFavoritesLayout;
    private LinearLayout mGameFavoriteStatusChangeLayout;
    private LinearLayout mGameRemoveFromFavoritesLayout;
    private LinearLayout mGameLayout;
    private Button mGameAddToFavoritesBtn;
    private Button mGameRemoveFromFavoritesBtn;
    private ProgressBar mGameProgressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_game);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        mCurrentGameId = intent.getStringExtra("gameId");

        initializeUI();
        initializeViewModel();
        initializeGame();
    }

    private void initializeUI() {
        mGameImage = findViewById(R.id.game_activity_image);
        mGameName = findViewById(R.id.game_activity_title);
        mGameRating = findViewById(R.id.game_activity_rating);
        mGameLayout = findViewById(R.id.game_activity_gameLayout);
        mGameAddToFavoritesBtn = findViewById(R.id.game_add_to_favorites);
        mGameProgressBar = findViewById(R.id.game_activity_progressBar);
        mGameReleaseDate = findViewById(R.id.game_activity_release_date);
        mGameRemoveFromFavoritesBtn = findViewById(R.id.game_remove_from_favorites);
        mGameAddToFavoritesLayout = findViewById(R.id.game_add_to_favorites_layout);
        mGameFavoriteStatusChangeLayout = findViewById(R.id.game_load_between_favorite_states);
        mGameRemoveFromFavoritesLayout = findViewById(R.id.game_remove_from_favorites_layout);



        mGameAddToFavoritesBtn.setOnClickListener(v -> {
                    showUpdatingFavoritesState();
                    mGameViewModel.updateGameFavoriteStatus();
                });
        mGameRemoveFromFavoritesBtn.setOnClickListener(v -> {
                    showUpdatingFavoritesState();
                    mGameViewModel.updateGameFavoriteStatus();
                });


        mGameProgressBar.setVisibility(View.VISIBLE);
        mGameLayout.setVisibility(View.GONE);
    }

    private void initializeGame()
    {
        mGameViewModel.loadGameAndUserGameFavoriteStatus(mCurrentGameId);
    }

    private void initializeViewModel() {
        mGameViewModel = new ViewModelProvider(this).get(GameViewModel.class);

        mGameViewModel.getLoadGameDetailsSuccess().observe(this, (String s) -> {
            showGameDetails(mGameViewModel.getGame().getValue());
            changeGameFavoriteScreen(mGameViewModel.getIsInFavorites().getValue());
        });

        mGameViewModel.getUpdateFavoriteStatusSuccess().observe(this, (String s) -> {
            changeGameFavoriteScreen(mGameViewModel.getIsInFavorites().getValue());
        });

        mGameViewModel.getLoadGameDetailsError().observe(this, (String s) -> {
            showInitializationErrorMessage(s);
        });

        mGameViewModel.getUpdateFavoriteStatusError().observe(this, (String s) -> {
            showUpdateFavoritesErrorMessage(s);
        });
    }

    private void showGameDetails(Game iGame)
    {
        Picasso.get().load(iGame.getBackgroundImage()).into(mGameImage);
        mGameName.setText(iGame.getGameName());
        mGameRating.setText(new DecimalFormat("0.00").format(iGame.getRating()));
        mGameReleaseDate.setText(new SimpleDateFormat("yyyy-MM-dd").format(iGame.getReleaseDate()));

        Typeface customFont = ResourcesCompat.getFont(this, R.font.dosis_extrabold);

        List<Genre> genres = iGame.getGenres();
        LinearLayout gameGenresLayout = findViewById(R.id.game_genres_list_layout);
        for(int i=0; i<genres.size(); i++)
        {
            TextView genreTextView = new TextView(this);
            genreTextView.setText(getResources().getTextArray(R.array.game_genres)[i]);
            genreTextView.setGravity(Gravity.CENTER);
            genreTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            genreTextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.primary_500, getTheme()));
            genreTextView.setTypeface(customFont);
            gameGenresLayout.addView(genreTextView);
        }

        List<Platform> platforms = iGame.getPlatforms();
        LinearLayout gamePlatformsLayout = findViewById(R.id.game_platforms_list_layout);
        for(int i=0; i<platforms.size(); i++)
        {
            TextView platformTextView = new TextView(this);
            platformTextView.setText(getResources().getTextArray(R.array.game_platforms)[i]);
            platformTextView.setGravity(Gravity.CENTER);
            platformTextView.setTextSize(TypedValue.COMPLEX_UNIT_SP, 20);
            platformTextView.setTextColor(ResourcesCompat.getColor(getResources(), R.color.primary_500, getTheme()));
            platformTextView.setTypeface(customFont);
            gamePlatformsLayout.addView(platformTextView);
        }

        mGameProgressBar.setVisibility(View.GONE);
        mGameLayout.setVisibility(View.VISIBLE);
    }

    private void changeGameFavoriteScreen(boolean iNewStatus)
    {
        if(iNewStatus)
        {
            showAlreadyInFavorites();
        }
        else
        {
            showAddToFavorites();
        }
    }

    private void showAlreadyInFavorites() {
        mGameAddToFavoritesLayout.setVisibility(View.GONE);
        mGameFavoriteStatusChangeLayout.setVisibility(View.GONE);
        mGameRemoveFromFavoritesLayout.setVisibility(View.VISIBLE);
    }

    private void showAddToFavorites() {
        mGameAddToFavoritesLayout.setVisibility(View.VISIBLE);
        mGameFavoriteStatusChangeLayout.setVisibility(View.GONE);
        mGameRemoveFromFavoritesLayout.setVisibility(View.GONE);
    }

    private void showUpdatingFavoritesState() {
        mGameAddToFavoritesLayout.setVisibility(View.GONE);
        mGameFavoriteStatusChangeLayout.setVisibility(View.VISIBLE);
        mGameRemoveFromFavoritesLayout.setVisibility(View.GONE);
    }
    private void showInitializationErrorMessage(String iErrorMessage) {
        Toast.makeText(this, iErrorMessage, Toast.LENGTH_LONG).show();
        finish();
    }
    private void showUpdateFavoritesErrorMessage(String iErrorMessage) {
        Toast.makeText(this, iErrorMessage, Toast.LENGTH_LONG).show();
        changeGameFavoriteScreen(mGameViewModel.getIsInFavorites().getValue());
    }

}