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

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.hit.playpal.R;
import com.hit.playpal.paginatedsearch.games.enums.GameSearchType;
import com.hit.playpal.paginatedsearch.rooms.enums.RoomSearchType;
import com.hit.playpal.paginatedsearch.users.enums.UserSearchType;
import com.hit.playpal.utils.CurrentlyLoggedUser;

import io.reactivex.rxjava3.exceptions.UndeliverableException;
import io.reactivex.rxjava3.plugins.RxJavaPlugins;

public class HomeActivity extends AppCompatActivity {
    private static final String TAG = "HomeActivity";

    private static final String ARG_SEARCH_TYPE = "searchType";
    private static final String ARG_USER_ID = "userId";

    private NavController mNavController;
    private ImageButton mBtnMyProfile;
    private ImageButton mBtnNotifications;
    private ImageButton mBtnGames;
    private ImageButton mBtnUserSearch;
    private ImageButton mBtnChats;
    private ImageButton mBtnGroupChatSearch;

    private BottomNavigationView mBottomNavigationView;


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

        RxJavaPlugins.setErrorHandler(e -> {
            if (e instanceof UndeliverableException && e.getCause() instanceof InterruptedException) {
                return; // ignore InterruptedExceptions as they were likely caused by dispose being called
            }
        });

        initViews();
        setListeners();
    }

    private void setListeners() {
//        mBtnMyProfile.setOnClickListener(v -> navigateToMyProfileFragment());
//        mBtnNotifications.setOnClickListener(v -> navigateToNotificationsFragment());
//        mBtnGames.setOnClickListener(v -> navigateToGamesFragment());
//        mBtnUserSearch.setOnClickListener(v -> navigateToSearchFragment());
//        mBtnChats.setOnClickListener(v -> navigateToChatsFragment());
//        mBtnGroupChatSearch.setOnClickListener(v -> navigateToGroupChatSearchFragment());

        mBottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.navbutton_myprofile) {
                navigateToMyProfileFragment();
                return true;
            } else if (item.getItemId() == R.id.navbutton_notifications) {
                navigateToNotificationsFragment();
                return true;
            } else if (item.getItemId() == R.id.navbutton_games) {
                navigateToGamesFragment();
                return true;
            } else if (item.getItemId() == R.id.navbutton_chats) {
                navigateToChatsFragment();
                return true;
            }

//                else if (item.getItemId() == R.id.navbutton_usersearch) {
//                    navigateToUserSearchFragment();
//                    return true;
//                }
//              else if (item.getItemId() == R.id.navbutton_groupsearch) {
//                navigateToGroupChatSearchFragment();
//                return true;
//            }
                else if (item.getItemId() == R.id.navbutton_search) {
                navigateToSearchFragment();
                return true;
                }

              else {
                return false;
            }
        });
    }



    private void initViews() {
        mBottomNavigationView = findViewById(R.id.bottomNavigationView);
        mBottomNavigationView.setSelectedItemId(R.id.navbutton_chats);
        mNavController = ((NavHostFragment) getSupportFragmentManager().findFragmentById(R.id.homeContainerFragments)).getNavController();

//        mBtnMyProfile = findViewById(R.id.myProfileButton);
//        mBtnNotifications = findViewById(R.id.notificationsButton);
//        mBtnGames = findViewById(R.id.gamesButton);
//        mBtnUserSearch = findViewById(R.id.userSearchButton);
//        mBtnChats = findViewById(R.id.chatsButton);
//        mBtnGroupChatSearch = findViewById(R.id.groupChatSearchButton);
    }

    private void navigateToMyProfileFragment() {
        String Uid = CurrentlyLoggedUser.get().getUid();
        Bundle bundle = new Bundle();
        bundle.putString(ARG_USER_ID, Uid);
        mNavController.navigate(R.id.profileActivity, bundle);
    }

    private void navigateToNotificationsFragment() {
        mNavController.navigate(R.id.notificationsFragment);
    }

    private void navigateToGamesFragment() {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_SEARCH_TYPE, GameSearchType.ALL.toString());
        mNavController.navigate(R.id.gameSearchFragment, bundle);
    }

    private void navigateToSearchFragment() {
        mNavController.navigate(R.id.searchFragment);
    }

    private void navigateToUserSearchFragment() {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_SEARCH_TYPE, UserSearchType.ALL.toString());
        mNavController.navigate(R.id.userSearchFragment, bundle);
    }

    private void navigateToChatsFragment() {
        mNavController.navigate(R.id.recentChatsFragment);
    }
    private void navigateToGroupChatSearchFragment() {
        Bundle bundle = new Bundle();
        bundle.putString(ARG_SEARCH_TYPE, RoomSearchType.ALL.toString());
        mNavController.navigate(R.id.roomSearchFragment, bundle);
    }
}