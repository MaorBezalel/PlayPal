package com.hit.playpal.profile.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;


import com.hit.playpal.R;
import com.hit.playpal.profile.domain.usecases.GetProfileAccountInfoUseCase;
import com.hit.playpal.profile.ui.fragments.FavoriteGamesFragment;
import com.hit.playpal.profile.ui.fragments.FriendsFragment;
import com.hit.playpal.profile.ui.fragments.RoomsFragment;
import com.hit.playpal.settings.ui.activities.SettingsActivity;
import com.hit.playpal.utils.CurrentlyLoggedUser;
import com.squareup.picasso.Picasso;

public class ProfileActivity extends AppCompatActivity {

    private GetProfileAccountInfoUseCase mProfileAccountInfoUseCase;
    private TextView mTextViewGetUserName;
    private ImageView mImageViewAvatar;
    private TextView mTextViewGetDisplayName;
    private TextView mTextViewGetAboutMe;

    private final String  currentUser = CurrentlyLoggedUser.getCurrentlyLoggedUser().getUid();

    private FrameLayout mFragmentContainer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        String Uid = intent.getStringExtra("userId");

        mTextViewGetUserName = findViewById(R.id.textViewGetUserName);
        mImageViewAvatar = findViewById(R.id.imageViewAvatar);
        mTextViewGetDisplayName = findViewById(R.id.textViewGetDisplayName);
        mTextViewGetAboutMe = findViewById(R.id.textViewGetAboutMe);
        mFragmentContainer = findViewById(R.id.fragment_container);
        Button buttonSettings = findViewById(R.id.buttonSettings);
        Button buttonAddFriend = findViewById(R.id.buttonAddFriend);

        if (!currentUser.equals(Uid)) {
            buttonSettings.setVisibility(View.GONE);
            buttonAddFriend.setVisibility(View.VISIBLE);}

        mProfileAccountInfoUseCase = new GetProfileAccountInfoUseCase();
        mProfileAccountInfoUseCase.execute(Uid).addOnSuccessListener(document -> {
            if (document != null && document.exists()) {
                String username = document.getString("username");
                String displayName = document.getString("display_name");
                String aboutMe = document.getString("about_me");
                String avatarUrl = document.getString("profile_picture");

                mTextViewGetUserName.setText(username);
                mTextViewGetDisplayName.setText(displayName);
                mTextViewGetAboutMe.setText(aboutMe);

// Load the image using Picasso
                assert avatarUrl != null;
                if (!avatarUrl.isEmpty()) {
                    Picasso.get().load(avatarUrl).into(mImageViewAvatar);
                }
            } else {
                Log.d("ProfileActivity", "No such user");
            }
        });

        Button buttonFriends = findViewById(R.id.buttonFriends);
        buttonFriends.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToFragment(FriendsFragment.class, Uid);
            }
        });

        Button buttonRooms = findViewById(R.id.buttonRooms);
        buttonRooms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToFragment(RoomsFragment.class, Uid);
            }
        });

        Button buttonFavGames = findViewById(R.id.buttonFavGames);
        buttonFavGames.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navigateToFragment(FavoriteGamesFragment.class, Uid);
            }
        });
    }

    private void navigateToFragment(Class<? extends Fragment> fragmentClass, String Uid) {
        mFragmentContainer.setVisibility(View.VISIBLE);

        try {
            Fragment fragment = fragmentClass.newInstance();
            Bundle args = new Bundle();
            args.putString("userId", Uid);
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        } catch (InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onBackPressed() {
        if (mFragmentContainer.getVisibility() == View.VISIBLE) {
            mFragmentContainer.setVisibility(View.GONE);
        } else {
            super.onBackPressed();
        }
    }


    public void buttonSettingsFunc(View view) {
        Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
        startActivity(intent);
    }
}