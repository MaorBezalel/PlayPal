package com.hit.playpal.settings.ui.activities;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;


import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hit.playpal.R;
import com.hit.playpal.auth.ui.activities.AuthActivity;
import com.hit.playpal.entities.users.User;
import com.hit.playpal.profile.ui.activities.ProfileActivity;
import com.hit.playpal.settings.domain.usecases.CheckIfUserNameIsUniqueUseCase;
import com.hit.playpal.settings.domain.usecases.UpdateUserProfileUseCase;
import com.hit.playpal.settings.ui.utils.SettingsValidations;
import com.hit.playpal.utils.CurrentlyLoggedUser;
import com.hit.playpal.utils.Out;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.Objects;

public class SettingsActivity extends AppCompatActivity {

    private final String mCurrentUserUid = CurrentlyLoggedUser.get().getUid();
    private EditText mEditTextSetDisplayName;
    private EditText mEditTextSetUserName;
    private EditText mEditTextSetAboutMe;
    private ShapeableImageView mImageSetViewProfilePicture;
    private Button mButtonSaveSettings;
    private ProgressBar mProgressBarSavingSettings;
    private Uri mSelectedImageUri;
    private boolean mIsImageChanged;
    private String mOriginalUserName;
    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle iSavedInstanceState) {
        super.onCreate(iSavedInstanceState);
        setupUI();
        setupEditTexts();
        setupButtons();
    }

    /**
     * Sets up the user interface for the SettingsActivity. Retrieves user data from the intent, initializes views, and populates them with the user data.
     */
    private void setupUI() {
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        setupWindowInsets();

        Intent intent = getIntent();
        String displayName = intent.getStringExtra("displayName");
        String userName = intent.getStringExtra("userName");
        String aboutMe = intent.getStringExtra("aboutMe");
        String avatarImage = intent.getStringExtra("avatarImage");

        mButtonSaveSettings = findViewById(R.id.buttonSaveSettings);
        mProgressBarSavingSettings = findViewById(R.id.progressBarSavingSettings);
        mEditTextSetDisplayName = findViewById(R.id.editTextSetDisplayName);
        mEditTextSetUserName = findViewById(R.id.editTextSetUserName);
        mEditTextSetAboutMe = findViewById(R.id.editTextSetAboutMe);
        mImageSetViewProfilePicture = findViewById(R.id.imageSetViewAvatar);
        mAuth = FirebaseAuth.getInstance();

        mEditTextSetDisplayName.setText(displayName);
        mEditTextSetUserName.setText(userName);
        mEditTextSetAboutMe.setText(aboutMe);
        mOriginalUserName = mEditTextSetUserName.getText().toString().trim();

        if (avatarImage != null && !avatarImage.isEmpty()) {
            Picasso.get().load(avatarImage).into(mImageSetViewProfilePicture);
            // Convert the URL to a Uri and assign it to selectedImageUri
            mSelectedImageUri = Uri.parse(avatarImage);
        }
    }

    /**
     * Adjusts the padding of the main view to account for system UI elements like the status bar and navigation bar.
     * This ensures the content of the app is not obscured by these system elements.
     */
    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

    /**
     * Sets up the EditText fields for the SettingsActivity.
     * It creates a TextWatcher that makes the save settings button visible after any text changes in the EditText fields.
     * The TextWatcher is then added to the display name, username, and about me EditText fields.
     */
    private void setupEditTexts() {
        // Create a TextWatcher
        TextWatcher textWatcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // No action needed here
            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // No action needed here
            }
            @Override
            public void afterTextChanged(Editable s) {
                mButtonSaveSettings.setVisibility(View.VISIBLE);
            }
        };

        mEditTextSetDisplayName.addTextChangedListener(textWatcher);
        mEditTextSetUserName.addTextChangedListener(textWatcher);
        mEditTextSetAboutMe.addTextChangedListener(textWatcher);
    }

    /**
     * Sets up the buttons for the SettingsActivity.
     * It assigns click listeners to the return button and the profile picture view.
     * The return button starts the ProfileActivity and finishes the current activity.
     * The profile picture view launches an image picker when clicked.
     */
    private void setupButtons() {
        Button buttonSettingsReturn = findViewById(R.id.buttonSettingsReturn);
        buttonSettingsReturn.setOnClickListener(v -> {
            Intent intent12 = new Intent(SettingsActivity.this, ProfileActivity.class);
            intent12.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
            startActivity(intent12);
            finish();
        });

        mImageSetViewProfilePicture.setOnClickListener(v -> {
            Intent intent1 = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            mImagePickerLauncher.launch(intent1);
        });
    }

    /**
     * ActivityResultLauncher for handling the result of an image selection activity.
     * If the result is OK, it calls the handleImageSelection method with the selected image's URI.
     * If the result is CANCELED, it shows a toast message indicating that the image selection was cancelled.
     */
    private final ActivityResultLauncher<Intent> mImagePickerLauncher = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    Intent data = result.getData();
                    if (data != null && data.getData() != null) {
                        handleImageSelection(data.getData());
                    }
                } else if (result.getResultCode() == Activity.RESULT_CANCELED) {
                    // Handle the case where the user cancelled the image selection
                    Toast.makeText(this, "Image selection cancelled", Toast.LENGTH_SHORT).show();
                }
            }
    );

    /**
     * Handles the selection of an image from the image picker.
     * It first gets the real path of the image URI and validates the image.
     * If the image is valid, it sets the image to the ImageView and updates the selectedImageUri.
     * If the image is not valid or an exception occurs while loading the image, it shows an appropriate message to the user.
     */
    private void handleImageSelection(Uri iNewImageUri) {
        String filePath = getRealPathFromURI(iNewImageUri);
        if (filePath == null) {
            Toast.makeText(this, "Failed to get image path", Toast.LENGTH_SHORT).show();
            return;
        }
        Out<String> invalidationReason = Out.of(String.class);
        if (!SettingsValidations.isAvatarImageValid(filePath, invalidationReason)) {
            Snackbar.make(findViewById(android.R.id.content), invalidationReason.get(), Snackbar.LENGTH_LONG).show();
            return;
        }

        try {
            Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), iNewImageUri);
            // Set the Bitmap directly to the ImageView
            mImageSetViewProfilePicture.setImageBitmap(bitmap);
            // Only update selectedImageUri if everything was successful
            mSelectedImageUri = iNewImageUri;
            mIsImageChanged = true;
            mButtonSaveSettings.setVisibility(View.VISIBLE);
        } catch (IOException e) {
            Log.e("SettingsActivity", "Failed to load image", e);
        }
    }

    /**
     * Handles the saving of user settings.
     * It first retrieves the user input from the EditText fields and validates them.
     * If the input is valid and the username has been changed, it checks the uniqueness of the new username.
     * If the username hasn't been changed, it directly updates the user profile with the new settings.
     */
    public void saveSettings(View iView) {
        String userName = mEditTextSetUserName.getText().toString().trim();
        String displayName = mEditTextSetDisplayName.getText().toString().trim();
        String aboutMe = mEditTextSetAboutMe.getText().toString().trim();
        Uri imageUri = mIsImageChanged ? mSelectedImageUri : null;

        if (performInputValidation(userName, displayName, aboutMe)) {
            if (!userName.equals(mOriginalUserName)) {
                // The username has been changed, check its uniqueness
                checkUserNameAndProceed(userName, displayName, aboutMe, imageUri);
            } else {
                // The username hasn't been changed, no need to check its uniqueness
                updateProfile(userName, displayName, aboutMe, imageUri);
            }
        }
    }

    /**
     * Checks if the provided username is unique and proceeds with updating the profile if it is.
     * It uses the CheckIfUserNameIsUniqueUseCase to validate the uniqueness of the username.
     * If the username is unique, it calls the updateProfile method to update the user profile.
     * If the username is not unique, it logs an error and sets an error message on the username EditText field.
     */
    private void checkUserNameAndProceed(String iUserName, String iDisplayName, String iAboutMe, Uri mImageUri) {
        CheckIfUserNameIsUniqueUseCase checkIfUserNameIsUniqueUseCase = new CheckIfUserNameIsUniqueUseCase();
        checkIfUserNameIsUniqueUseCase.isUsernameValidAndUnique(iUserName).addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                updateProfile(iUserName, iDisplayName, iAboutMe, mImageUri);
            } else {
                // Handle the error
                Log.e("ProfileActivity", "Failed to validate username", task.getException());
                mEditTextSetUserName.setError(Objects.requireNonNull(task.getException()).getMessage());
            }
        });
    }

    /**
     * Updates the user profile with the provided details.
     * It first makes the progress bar visible and the save settings button invisible.
     * Then, it executes the UpdateUserProfileUseCase with the provided details.
     * If the update is successful, it creates a result intent with the updated details and sets the result of the activity.
     * If the update is not successful, it logs an error and makes the save settings button visible again.
     */
    private void updateProfile(String iUserName, String iDisplayName, String iAboutMe, Uri mImageUri) {
        mProgressBarSavingSettings.setVisibility(View.VISIBLE);
        mButtonSaveSettings.setVisibility(View.GONE);
        UpdateUserProfileUseCase updateUserProfileUseCase = new UpdateUserProfileUseCase();
        updateUserProfileUseCase.execute(mCurrentUserUid, iUserName, iDisplayName, iAboutMe, mImageUri).addOnCompleteListener(task -> {
            mProgressBarSavingSettings.setVisibility(View.GONE);

            if (task.isSuccessful()) {
                Toast.makeText(SettingsActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                

                Intent resultIntent = new Intent();
                resultIntent.putExtra("username", iUserName);
                resultIntent.putExtra("display_name", iDisplayName);
                resultIntent.putExtra("about_me", iAboutMe);
                if (mImageUri != null) {
                    resultIntent.putExtra("profile_picture", mImageUri.toString());
                }
                setResult(Activity.RESULT_OK, resultIntent);
                finish();
            } else {
                // Handle the error
                Log.e("SettingsActivity", "Failed to update profile", task.getException());
                mButtonSaveSettings.setVisibility(View.VISIBLE);
                Toast.makeText(SettingsActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
            }
        });
    }

    /**
     * Validates the user input for username, display name, and about me.
     * It uses the SettingsValidations class to validate each input.
     * If an input is not valid, it sets an error message on the corresponding EditText field and sets the isValid flag to false.
     * If an input is valid, it clears any existing error message on the corresponding EditText field.
     * It returns the isValid flag which indicates whether all inputs are valid.
     */
    private boolean performInputValidation(String iUsername, String iDisplayName,String iAboutMe) {
        boolean isValid = true;
        Out<String> invalidationReason = Out.of(String.class);

        if (!SettingsValidations.isUsernameValid(iUsername, invalidationReason)) {
            mEditTextSetUserName.setError(invalidationReason.get());
            isValid = false;
        } else {
            mEditTextSetUserName.setError(null);
        }

        if (!SettingsValidations.isDisplayNameValid(iDisplayName, invalidationReason)) {
            mEditTextSetDisplayName.setError(invalidationReason.get());
            isValid = false;
        } else {
            mEditTextSetDisplayName.setError(null);
        }

        if (!SettingsValidations.isAboutMeValid(iAboutMe, invalidationReason)) {
            mEditTextSetAboutMe.setError(invalidationReason.get());
            isValid = false;
        } else {
            mEditTextSetAboutMe.setError(null);
        }

        return isValid;
    }

    /**
     * Retrieves the real file path from a content URI.
     * It queries the content resolver with the content URI and the projection MediaStore.Images.Media.DATA.
     * If the query returns a cursor, it retrieves the column index for MediaStore.Images.Media.DATA, moves the cursor to the first row, and gets the string at the column index.
     * It then closes the cursor and returns the retrieved path.
     * If the query does not return a cursor, it returns null.
     */
    private String getRealPathFromURI(Uri iContentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(iContentUri, proj, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        return null;
    }

    /**
     * Logs out the current user and starts the AuthActivity.
     * It first signs out the current user using FirebaseAuth.signOut().
     * Then, it starts the AuthActivity and clears the task stack to prevent the user from going back to the SettingsActivity.
     */
    public void logOutFunc(View iView) {
        mAuth.signOut();
        Intent intent = new Intent(SettingsActivity.this, AuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    /**
     * Starts the ProfileActivity and finishes the current activity.
     */
    @Override
    protected void onResume() {
        super.onResume();

        // Only fetch the latest user details from the database if the image has not been changed
        if (!mIsImageChanged) {
            fetchUserDetails();
        }
    }

    /**
     * Fetches the latest user details from the database and updates the UI with the new details.
     * It uses the CurrentlyLoggedUser class to get the current user's UID and fetches the user details from the Firestore database.
     * If the user details are successfully fetched, it updates the UI with the latest user details.
     * If the user details are not fetched successfully, it logs an error.
     */
    private void fetchUserDetails() {
        String currentUser = CurrentlyLoggedUser.get().getUid();
        FirebaseFirestore DB = FirebaseFirestore.getInstance();
        DB.collection("users").document(currentUser).get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                DocumentSnapshot document = task.getResult();
                if (document.exists()) {
                    // Update the UI with the latest user details
                    String updatedAvatarImage = document.getString("profile_picture");
                    if (updatedAvatarImage != null && !updatedAvatarImage.isEmpty()) {
                        Picasso.get().load(updatedAvatarImage).into(mImageSetViewProfilePicture);
                        // Convert the URL to a Uri and assign it to selectedImageUri
                        mSelectedImageUri = Uri.parse(updatedAvatarImage);
                    }
                } else {
                    Log.d("SettingsActivity", "No such user");
                }
            } else {
                Log.d("SettingsActivity", "Failed to fetch user details", task.getException());
            }
        });
    }
}