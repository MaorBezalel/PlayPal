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

    /**
     * Sets up the UI of the activity
     */
    private void setupUI() {
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_profile);
        applyWindowInsets(R.id.main);
        initializeViews();
        setupActivityResultLauncher();
    }

    /**
     * Applies the window insets to the main layout
     *
     * @param iViewId The ID of the view to apply the window insets to
     */
    private void applyWindowInsets(int iViewId) {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(iViewId), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Initializes the views of the activity
     */
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

    /**
     * Sets up the ActivityResultLauncher for handling activity results.
     * It registers a callback for the StartActivityForResult contract and links it to the handleActivityResult method.
     */
    private void setupActivityResultLauncher() {
        mSettingsResultLauncher = registerForActivityResult(
                new ActivityResultContracts.StartActivityForResult(),
                this::handleActivityResult
        );
    }

    /**
     * Handles the result of an activity that was started for a result.
     *
     * @param iResult The result of the activity that was started for a result
     */
    private void handleActivityResult(@NonNull ActivityResult iResult) {
        if (iResult.getResultCode() == Activity.RESULT_OK) {
            Intent data = iResult.getData();
            if (data != null) {
                updateProfileData(data);
            }
        }
    }

    /**
     * Updates the profile data with the data from the intent.
     *
     * @param iData The intent containing the updated profile data
     */
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

    /**
     * Sets up the ViewModel for the activity
     */
    private void setupViewModel() {
        mProfileViewModel = new ProfileViewModel();
        mProfileViewModel.getOnSuccessfulOpenChat().observe(this, this::openChatRoom);
        mProfileViewModel.getOnFailedOpenChat().observe(this, this::handleOpenChatFailure);
    }

    /**
     * Opens the chat room with the given OneToOneChatRoom
     *
     * @param iOneToOneChatRoom The OneToOneChatRoom to open
     */
    private void openChatRoom(OneToOneChatRoom iOneToOneChatRoom) {
        Intent intentChatRoom = new Intent(ProfileActivity.this, ChatRoomActivity.class);
        intentChatRoom.putExtra("chatRoom", iOneToOneChatRoom);
        intentChatRoom.putExtra("chatRoomLocation", ChatRoomLocation.CHAT_BODY);
        startActivity(intentChatRoom);
    }

    /**
     * Handles the failure to open the chat room
     *
     * @param iThrowable The Throwable that caused the failure
     */
    private void handleOpenChatFailure(Throwable iThrowable) {
        Toast.makeText(ProfileActivity.this, "Failed to open chat room", Toast.LENGTH_SHORT).show();
        Log.e("ProfileActivity", "Failed to open chat room", iThrowable);
    }

    /**
     * Sets up the buttons of the activity
     */
    private void setupButtons() {
        setupReturnButton();
        setupOnBackPressedCallback();
        setupFriendsButton();
        setupRoomsButton();
        setupFavGamesButton();
    }

    /**
     * Sets up the return button of the activity
     */
    private void setupReturnButton() {
        Button buttonReturn = findViewById(R.id.buttonProfileReturn);
        buttonReturn.setOnClickListener(v -> finish());
    }

    /**
     * Sets up the OnBackPressedCallback for the activity
     */
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

    /**
     * Sets up the Friends button of the activity
     */
    private void setupFriendsButton() {
        Button buttonFriends = findViewById(R.id.buttonFriends);
        buttonFriends.setOnClickListener(v -> navigateToFragment(UserSearchFragment.class, mUidThatBelongsToThisProfilePage, UserSearchType.FRIENDS.toString()));
    }

    /**
     * Sets up the Rooms button of the activity
     */
    private void setupRoomsButton() {
        Button buttonRooms = findViewById(R.id.buttonRooms);
        buttonRooms.setOnClickListener(v -> navigateToFragment(RoomSearchFragment.class, mUidThatBelongsToThisProfilePage, RoomSearchType.JOINED.toString()));
    }

    /**
     * Sets up the Favorite Games button of the activity
     */
    private void setupFavGamesButton() {
        Button buttonFavGames = findViewById(R.id.buttonFavGames);
        buttonFavGames.setOnClickListener(v -> navigateToFragment(GameSearchFragment.class, mUidThatBelongsToThisProfilePage, GameSearchType.FAVORITES.toString()));
    }

    /**
     * Fetches the profile data of the user whose profile is being viewed
     */
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

    /**
     * Fetches the status between the current user and the user whose profile is being viewed
     */
    private void fetchStatus() {
        GetStatusUseCase getStatusUseCase = new GetStatusUseCase();
        getStatusUseCase.execute(mCurrentUserUid, mUidThatBelongsToThisProfilePage).addOnCompleteListener(this::handleStatusResult);
    }

    /**
     * Handles the result of the GetStatusUseCase
     *
     * @param iTask The Task containing the result of the GetStatusUseCase
     */
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

    /**
     * Displays the profile data of the user whose profile is being viewed
     *
     * @param iDocument The DocumentSnapshot containing the profile data of the user
     */
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

    /**
     * Navigates to the fragment of the specified class
     *
     * @param iFragmentClass The class of the fragment to navigate to
     * @param iUid The UID of the user whose profile is being viewed
     * @param iSearchType The search type of the fragment
     */
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

    /**
     * Handles the click event of the Settings button.
     * It retrieves the current display name, username, about me text, and avatar image URL.
     * Then, it creates an Intent to start the SettingsActivity, and puts the retrieved data into the Intent as extras.
     * Finally, it launches the SettingsActivity using the ActivityResultLauncher.
     *
     * @param iView The View that was clicked
     */
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

    /**
     * Handles the click event of the Add Friend button.
     * It disables the button to prevent multiple clicks.
     * If the status between the current user and the user whose profile is being viewed is "pending",
     * it displays a toast message indicating that the friend request is pending.
     * If the status is "noStatus", it creates a Map containing the display name, profile picture URL, and UID of the other user.
     * Then, it executes the AddPendingFriendUseCase to send a friend request to the other user.
     * If the task is successful, it sets the status between the current user and the other user to "pending" and displays a toast message.
     * If the task is not successful, it logs the error.
     *
     * @param iView The View that was clicked
     */
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

    /**
     * Handles the click event of the Remove Friend button.
     * It disables the button to prevent multiple clicks.
     * It executes the RemoveFriendUseCase to remove the friend relationship between the current user and the user whose profile is being viewed.
     * If the task is successful, it sets the status between the current user and the other user to "noStatus" and displays a toast message.
     * If the task is not successful, it logs the error.
     *
     * @param iView The View that was clicked
     */
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

    /**
     * Handles the click event of the Chat button.
     * It retrieves the display name, avatar URL, and UID of the user whose profile is being viewed.
     * Then, it creates a User object for the current user and the other user.
     * Finally, it executes the getOneToOneChatRoom method of the ProfileViewModel to get the OneToOneChatRoom between the current user and the other user.
     *
     * @param iView The View that was clicked
     */
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