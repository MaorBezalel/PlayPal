package com.hit.playpal.profile.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.OnBackPressedCallback;
import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;


import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.firebase.firestore.DocumentSnapshot;
import com.hit.playpal.R;
import com.hit.playpal.chatrooms.ui.activities.ChatRoomActivity;
import com.hit.playpal.chatrooms.ui.enums.ChatRoomLocation;
import com.hit.playpal.entities.chats.o2o.OneToOneChatRoom;
import com.hit.playpal.entities.users.User;
import com.hit.playpal.paginatedsearch.games.fragments.GameSearchFragment;
import com.hit.playpal.paginatedsearch.games.enums.GameSearchType;
import com.hit.playpal.paginatedsearch.rooms.fragments.RoomSearchFragment;
import com.hit.playpal.paginatedsearch.rooms.enums.RoomSearchType;
import com.hit.playpal.paginatedsearch.users.fragments.UserSearchFragment;
import com.hit.playpal.paginatedsearch.users.enums.UserSearchType;
import com.hit.playpal.profile.domain.usecases.AddPendingFriendUseCase;

import com.hit.playpal.profile.domain.usecases.GetProfileAccountInfoUseCase;
import com.hit.playpal.profile.domain.usecases.GetStatusUseCase;
import com.hit.playpal.profile.domain.usecases.RemoveFriendUseCase;
import com.hit.playpal.profile.ui.viewmodels.ProfileViewModel;
import com.hit.playpal.settings.ui.activities.SettingsActivity;
import com.hit.playpal.utils.CurrentlyLoggedUser;
import com.squareup.picasso.Picasso;

import java.util.HashMap;
import java.util.Map;

public class ProfileActivity extends AppCompatActivity {

    private TextView mTextViewGetUserName;
    private ShapeableImageView mImageViewAvatar;
    private TextView mTextViewGetDisplayName;
    private TextView mTextViewGetAboutMe;
    private Button mButtonAddFriend;
    private Button mButtonRemoveFriend;
    private Button mButtonChat;

    private String mStatusBetweenThisAndOtherUser;
    private String mProfilePictureUrlInThisProfilePage;

    private final String mCurrentUserUid = CurrentlyLoggedUser.get().getUid();

    private String mUidThatBelongsToThisProfilePage;

    private FrameLayout mFragmentContainer;
    private ActivityResultLauncher<Intent> mSettingsResultLauncher;

    private ProfileViewModel mProfileViewModel;

    @Override
    protected void onCreate(Bundle iSavedInstanceState) {
        super.onCreate(iSavedInstanceState);
        setupUI();
        setupViewModel();
        setupButtons();
        fetchProfileData();
    }

    private void setupUI() {
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        applyWindowInsets(R.id.main);
        initializeViews();
        setupActivityResultLauncher();
    }

    private void applyWindowInsets(int iViewId) {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(iViewId), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    private void initializeViews() {
        mTextViewGetUserName = findViewById(R.id.textViewGetUserName);
        mImageViewAvatar = findViewById(R.id.imageViewAvatar);
        mTextViewGetDisplayName = findViewById(R.id.textViewGetDisplayName);
        mTextViewGetAboutMe = findViewById(R.id.textViewGetAboutMe);
        mFragmentContainer = findViewById(R.id.fragment_container);
        mButtonAddFriend = findViewById(R.id.buttonAddFriend);
        mButtonRemoveFriend = findViewById(R.id.buttonRemoveFriend);
        mButtonChat = findViewById(R.id.buttonChat);
    }

    private void setupActivityResultLauncher() {
        mSettingsResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleActivityResult
        );
    }

    private void handleActivityResult(@NonNull ActivityResult iResult) {
        if (iResult.getResultCode() == Activity.RESULT_OK) {
            Intent data = iResult.getData();
            if (data != null) {
                updateProfileData(data);
            }
        }
    }

    private void updateProfileData(@NonNull Intent iData) {
        String updatedUserName = iData.getStringExtra("username");
        String updatedDisplayName = iData.getStringExtra("display_name");
        String updatedAboutMe = iData.getStringExtra("about_me");
        String updatedAvatarImage = iData.getStringExtra("profile_picture");

        mTextViewGetUserName.setText(updatedUserName);
        mTextViewGetDisplayName.setText(updatedDisplayName);
        mTextViewGetAboutMe.setText(updatedAboutMe);

        if (updatedAvatarImage != null && !updatedAvatarImage.equals("null") && !updatedAvatarImage.isEmpty()) {
            Picasso.get().load(updatedAvatarImage).into(mImageViewAvatar);
        }
    }

    private void setupViewModel() {
        mProfileViewModel = new ProfileViewModel();
        mProfileViewModel.getOnSuccessfulOpenChat().observe(this, this::openChatRoom);
        mProfileViewModel.getOnFailedOpenChat().observe(this, this::handleOpenChatFailure);
    }

    private void openChatRoom(OneToOneChatRoom iOneToOneChatRoom) {
        Intent intentChatRoom = new Intent(ProfileActivity.this, ChatRoomActivity.class);
        intentChatRoom.putExtra("chatRoom", iOneToOneChatRoom);
        intentChatRoom.putExtra("chatRoomLocation", ChatRoomLocation.CHAT_BODY);
        startActivity(intentChatRoom);
    }

    private void handleOpenChatFailure(Throwable iThrowable) {
        Toast.makeText(ProfileActivity.this, "Failed to open chat room", Toast.LENGTH_SHORT).show();
        Log.e("ProfileActivity", "Failed to open chat room", iThrowable);
    }

    private void setupButtons() {
        setupReturnButton();
        setupOnBackPressedCallback();
        setupFriendsButton();
        setupRoomsButton();
        setupFavGamesButton();
    }

    private void setupReturnButton() {
        Button buttonReturn = findViewById(R.id.buttonProfileReturn);
        buttonReturn.setOnClickListener(v -> finish());
    }

    private void setupOnBackPressedCallback() {
        OnBackPressedCallback onBackPressedCallback = new OnBackPressedCallback(true) {
            @Override
            public void handleOnBackPressed() {
                if (mFragmentContainer.getVisibility() == View.VISIBLE) {
                    mFragmentContainer.setVisibility(View.GONE);
                } else {
                    finish();
                }
            }
        };
        this.getOnBackPressedDispatcher().addCallback(this, onBackPressedCallback);
    }

    private void setupFriendsButton() {
        Button buttonFriends = findViewById(R.id.buttonFriends);
        buttonFriends.setOnClickListener(v -> navigateToFragment(UserSearchFragment.class, mUidThatBelongsToThisProfilePage, UserSearchType.FRIENDS.toString()));
    }

    private void setupRoomsButton() {
        Button buttonRooms = findViewById(R.id.buttonRooms);
        buttonRooms.setOnClickListener(v -> navigateToFragment(RoomSearchFragment.class, mUidThatBelongsToThisProfilePage, RoomSearchType.JOINED.toString()));
    }

    private void setupFavGamesButton() {
        Button buttonFavGames = findViewById(R.id.buttonFavGames);
        buttonFavGames.setOnClickListener(v -> navigateToFragment(GameSearchFragment.class, mUidThatBelongsToThisProfilePage, GameSearchType.FAVORITES.toString()));
    }

    private void fetchProfileData() {
        Intent intent = getIntent();
        mUidThatBelongsToThisProfilePage = intent.getStringExtra("userId");

        if (mUidThatBelongsToThisProfilePage == null) {
            finish();
            return;
        }

        if (!mCurrentUserUid.equals(mUidThatBelongsToThisProfilePage)) {
            fetchStatus();
        }

        GetProfileAccountInfoUseCase profileAccountInfoUseCase = new GetProfileAccountInfoUseCase();
        profileAccountInfoUseCase.execute(mUidThatBelongsToThisProfilePage).addOnSuccessListener(this::displayProfileData);
    }

    private void fetchStatus() {
        GetStatusUseCase getStatusUseCase = new GetStatusUseCase();
        getStatusUseCase.execute(mCurrentUserUid, mUidThatBelongsToThisProfilePage).addOnCompleteListener(this::handleStatusResult);
    }

    private void handleStatusResult(@NonNull Task<String> iTask) {
        if (iTask.isSuccessful()) {
            mStatusBetweenThisAndOtherUser = iTask.getResult();
            mButtonChat.setVisibility(View.VISIBLE);
            Log.d("ProfileActivity", "Status: " + mStatusBetweenThisAndOtherUser);
            if ("noStatus".equals(mStatusBetweenThisAndOtherUser) || "pending".equals(mStatusBetweenThisAndOtherUser)) {
                findViewById(R.id.buttonSettings).setVisibility(View.GONE);
                mButtonAddFriend.setVisibility(View.VISIBLE);
            } else if ("friends".equals(mStatusBetweenThisAndOtherUser)) {
                mButtonRemoveFriend.setVisibility(View.VISIBLE);
                mButtonAddFriend.setVisibility(View.GONE);
                findViewById(R.id.buttonSettings).setVisibility(View.GONE);
            }
        } else {
            Exception e = iTask.getException();
            Log.e("ProfileActivity", "Error getting status", e);
        }
    }

    private void displayProfileData(DocumentSnapshot iDocument) {
        if (iDocument != null && iDocument.exists()) {
            String username = iDocument.getString("username");
            String displayName = iDocument.getString("display_name");
            String aboutMe = iDocument.getString("about_me");
            mProfilePictureUrlInThisProfilePage = iDocument.getString("profile_picture");

            mTextViewGetUserName.setText(username);
            mTextViewGetDisplayName.setText(displayName);
            mTextViewGetAboutMe.setText(aboutMe);

            // Load the image using Picasso
            if (!mProfilePictureUrlInThisProfilePage.isEmpty()) {
                Picasso.get().load(mProfilePictureUrlInThisProfilePage).into(mImageViewAvatar);
            }
        } else {
            Log.d("ProfileActivity", "No such user");
        }
    }

    private void navigateToFragment(@NonNull Class<? extends Fragment> iFragmentClass, String iUid, String iSearchType) {
        mFragmentContainer.setVisibility(View.VISIBLE);

        try {
            Fragment fragment = iFragmentClass.newInstance();
            Bundle args = new Bundle();
            args.putString("userId", iUid);
            args.putString("searchType", iSearchType);
            fragment.setArguments(args);

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.fragment_container, fragment)
                    .addToBackStack(null)
                    .commit();
        } catch (InstantiationException | IllegalAccessException e) {
            Log.e("ProfileActivity", "Failed to navigate to fragment", e);
        }
    }


    public void buttonSettingsFunc(View iView) {
        String displayName = mTextViewGetDisplayName.getText().toString();
        String userName = mTextViewGetUserName.getText().toString();
        String aboutMe = mTextViewGetAboutMe.getText().toString();

        Intent intent = new Intent(ProfileActivity.this, SettingsActivity.class);
        intent.putExtra("displayName", displayName);
        intent.putExtra("userName", userName);
        intent.putExtra("aboutMe", aboutMe);
        intent.putExtra("avatarImage", mProfilePictureUrlInThisProfilePage);

        mSettingsResultLauncher.launch(intent);
    }

    public void addFriendFunc(View iView) {
        mButtonAddFriend.setClickable(false);

        if ("pending".equals(mStatusBetweenThisAndOtherUser)) {
            Toast.makeText(ProfileActivity.this, "Friend request is pending", Toast.LENGTH_SHORT).show();
            mButtonAddFriend.setClickable(true);
        } else if ("noStatus".equals(mStatusBetweenThisAndOtherUser)) {
            Map<String, Object> otherUserData = new HashMap<>();
            otherUserData.put("display_name", mTextViewGetDisplayName.getText().toString()); // displayName is the display name of the other user
            otherUserData.put("profile_picture", mProfilePictureUrlInThisProfilePage); // avatarUrl is the URL of the other user's avatar
            otherUserData.put("uid", mUidThatBelongsToThisProfilePage); // Uid is the id of the other user
            otherUserData.put("username", mTextViewGetUserName.getText().toString());


            AddPendingFriendUseCase addPendingFriendUseCase = new AddPendingFriendUseCase();
            addPendingFriendUseCase.execute(mCurrentUserUid, mUidThatBelongsToThisProfilePage, otherUserData).addOnCompleteListener(task -> {
                mButtonAddFriend.setClickable(true);

                if (task.isSuccessful()) {
                    mStatusBetweenThisAndOtherUser = "pending";
                    Toast.makeText(ProfileActivity.this, "Friend request sent!", Toast.LENGTH_SHORT).show();
                } else {
                    // Handle the error
                    Log.e("ProfileActivity", "Failed to send friend request", task.getException());
                }
            });
        }
    }

    public void buttonRemoveFriendFunc(View iView) {
        RemoveFriendUseCase removeFriendUseCase = new RemoveFriendUseCase();
        ProgressBar progressBarRemoveFriend = findViewById(R.id.progressBarRemoveFriend);

        progressBarRemoveFriend.setVisibility(View.VISIBLE);
        mButtonRemoveFriend.setVisibility(View.GONE);

        removeFriendUseCase.execute(mCurrentUserUid, mUidThatBelongsToThisProfilePage).addOnCompleteListener(task -> {
            progressBarRemoveFriend.setVisibility(View.GONE);
            mButtonAddFriend.setVisibility(View.VISIBLE);

            if (task.isSuccessful()) {
                mStatusBetweenThisAndOtherUser = "noStatus";
                Toast.makeText(ProfileActivity.this, "Friend removed", Toast.LENGTH_SHORT).show();

            } else {
                // Handle the error
                Log.e("ProfileActivity", "Failed to remove friend", task.getException());
            }
        });
    }

    public void buttonChatFunc(View iView) {
        String otherUserDisplayName = mTextViewGetDisplayName.getText().toString();
        String otherUserAvatarUrl = mProfilePictureUrlInThisProfilePage;
        String otherUserUid = mUidThatBelongsToThisProfilePage;

        User thisUser = CurrentlyLoggedUser.get();
        User otherUser = new User();
        otherUser.setUid(otherUserUid);
        otherUser.setDisplayName(otherUserDisplayName);
        otherUser.setProfilePicture(otherUserAvatarUrl);

        mProfileViewModel.getOneToOneChatRoom(thisUser, otherUser);
    }

}