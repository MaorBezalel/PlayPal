package com.hit.playpal.profile.ui.activities;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;


import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.hit.playpal.R;
import com.hit.playpal.home.ui.activities.HomeActivity;
import com.hit.playpal.profile.domain.usecases.AddPendingFriendUseCase;

import com.hit.playpal.home.ui.fragments.GamesFragment;
import com.hit.playpal.profile.domain.usecases.GetProfileAccountInfoUseCase;
import com.hit.playpal.profile.domain.usecases.GetStatusUseCase;
import com.hit.playpal.profile.domain.usecases.RemoveFriendUseCase;
import com.hit.playpal.profile.ui.fragments.FavoriteGamesFragment;
import com.hit.playpal.profile.ui.fragments.FriendsFragment;
import com.hit.playpal.profile.ui.fragments.RoomsFragment;
import com.hit.playpal.settings.ui.activities.SettingsActivity;
import com.hit.playpal.utils.CurrentlyLoggedUser;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private GetProfileAccountInfoUseCase mProfileAccountInfoUseCase;
    private TextView mTextViewGetUserName;
    private ImageView mImageViewAvatar;
    private TextView mTextViewGetDisplayName;
    private TextView mTextViewGetAboutMe;
    private GetStatusUseCase mGetStatusUseCase;
    private String status;

    private final String  currentUser = CurrentlyLoggedUser.getCurrentlyLoggedUser().getUid();

    private String Uid;

    private FrameLayout mFragmentContainer;
    Button buttonAddFriend;
    Button buttonRemoveFriend;

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
        Uid = intent.getStringExtra("userId");

        if (Uid == null) {
            finish();
            return;
        }
        mTextViewGetUserName = findViewById(R.id.textViewGetUserName);
        mImageViewAvatar = findViewById(R.id.imageViewAvatar);
        mTextViewGetDisplayName = findViewById(R.id.textViewGetDisplayName);
        mTextViewGetAboutMe = findViewById(R.id.textViewGetAboutMe);
        mFragmentContainer = findViewById(R.id.fragment_container);
        Button buttonReturn = findViewById(R.id.buttonProfileReturn);
        Button buttonSettings = findViewById(R.id.buttonSettings);
        buttonAddFriend = findViewById(R.id.buttonAddFriend);
        buttonRemoveFriend = findViewById(R.id.buttonRemoveFriend);



        if (!currentUser.equals(Uid)) {
            mGetStatusUseCase = new GetStatusUseCase();
            mGetStatusUseCase.execute(currentUser, Uid).addOnCompleteListener(new OnCompleteListener<String>() {
                @Override
                public void onComplete(@NonNull Task<String> task) {
                    if (task.isSuccessful()) {
                        status = task.getResult();
                        Log.d("ProfileActivity", "Status: " + status);
                        if ("noStatus".equals(status) || "pending".equals(status)){
                            buttonSettings.setVisibility(View.GONE);
                            buttonAddFriend.setVisibility(View.VISIBLE);
                        } else if("friends".equals(status)){
                            buttonRemoveFriend.setVisibility(View.VISIBLE);
                            buttonAddFriend.setVisibility(View.GONE);
                            buttonSettings.setVisibility(View.GONE);
                        }
                    } else {
                        Exception e = task.getException();
                    }
                }
            });
        }
        buttonReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(ProfileActivity.this, HomeActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

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

    public void AddFriendFunc(View view) {
        buttonAddFriend.setClickable(false);

        if ("pending".equals(status)) {
            Toast.makeText(ProfileActivity.this, "Friend request is pending", Toast.LENGTH_SHORT).show();
            buttonAddFriend.setClickable(true);
        } else if("noStatus".equals(status)){
            Map<String, Object> otherUserData = new HashMap<>();
            otherUserData.put("display_name", mTextViewGetDisplayName.getText().toString());
            otherUserData.put("profile_picture", ""); // implement image giving
            otherUserData.put("uid", Uid); // Uid is the id of the other user

            AddPendingFriendUseCase addPendingFriendUseCase = new AddPendingFriendUseCase();
            addPendingFriendUseCase.execute(currentUser,Uid, otherUserData).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {
                    buttonAddFriend.setClickable(true);

                    if (task.isSuccessful()) {
                        status = "pending";
                        Toast.makeText(ProfileActivity.this, "Friend request sent!", Toast.LENGTH_SHORT).show();
                    } else {
                        // Handle the error
                        Log.e("ProfileActivity", "Failed to send friend request", task.getException());
                    }
                }
            });
        }
    }

    public void buttonRemoveFriendFunc(View view) {
        ProgressBar progressBar = findViewById(R.id.progressBarRemoveFriend);
        progressBar.setVisibility(View.VISIBLE);
        buttonRemoveFriend.setVisibility(View.GONE);
        RemoveFriendUseCase RemoveFriendUseCase = new RemoveFriendUseCase();
        RemoveFriendUseCase.execute(currentUser,Uid).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBar.setVisibility(View.GONE);
                buttonAddFriend.setVisibility(View.VISIBLE);

                if (task.isSuccessful()) {
                    status = "noStatus";
                    Toast.makeText(ProfileActivity.this, "Friend removed", Toast.LENGTH_SHORT).show();

                } else {
                    // Handle the error
                    Log.e("ProfileActivity", "Failed to remove friend", task.getException());
                }
            }
        });
    }
}