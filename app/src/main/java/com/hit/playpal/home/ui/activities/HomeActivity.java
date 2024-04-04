package com.hit.playpal.home.ui.activities;

import android.os.Bundle;
import android.widget.ImageButton;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import com.hit.playpal.R;
import com.hit.playpal.utils.CurrentlyLoggedUser;

public class HomeActivity extends AppCompatActivity {
    private NavController mNavController;
    private ImageButton mBtnMyProfile;
    private ImageButton mBtnNotifications;
    private ImageButton mBtnGames;
    private ImageButton mBtnSearch;
    private ImageButton mBtnChats;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_home);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        initViews();
        setListeners();
    }

    private void setListeners() {
        mBtnMyProfile.setOnClickListener(v -> navigateToMyProfileFragment());
        mBtnNotifications.setOnClickListener(v -> navigateToNotificationsFragment());
        mBtnGames.setOnClickListener(v -> navigateToGamesFragment());
        mBtnSearch.setOnClickListener(v -> navigateToSearchFragment());
        mBtnChats.setOnClickListener(v -> navigateToChatsFragment());
    }



    private void initViews() {
        mNavController = ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.homeContainerFragments)).getNavController();
        mBtnMyProfile = findViewById(R.id.myProfileButton);
        mBtnNotifications = findViewById(R.id.notificationsButton);
        mBtnGames = findViewById(R.id.gamesButton);
        mBtnSearch = findViewById(R.id.searchButton);
        mBtnChats = findViewById(R.id.chatsButton);
    }

    private void navigateToMyProfileFragment() {
        String Uid = CurrentlyLoggedUser.getCurrentlyLoggedUser().getUid();
        Bundle bundle = new Bundle();
        bundle.putString("userId", Uid);
        mNavController.navigate(R.id.profile_activity, bundle);
    }

    private void navigateToNotificationsFragment() {
        mNavController.navigate(R.id.notifications_fragment);
    }

    private void navigateToGamesFragment() {
        mNavController.navigate(R.id.games_fragment);
    }

    private void navigateToSearchFragment() {
        mNavController.navigate(R.id.search_fragment);
    }

    private void navigateToChatsFragment() {
        mNavController.navigate(R.id.chats_fragment);
    }
}