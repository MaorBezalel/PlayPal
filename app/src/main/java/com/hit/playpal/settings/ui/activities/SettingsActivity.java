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

    private void setupWindowInsets() {
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });
    }

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

    public void logOutFunc(View iView) {
        mAuth.signOut();
        Intent intent = new Intent(SettingsActivity.this, AuthActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        finish();
    }

    @Override
    protected void onResume() {
        super.onResume();

        // Only fetch the latest user details from the database if the image has not been changed
        if (!mIsImageChanged) {
            fetchUserDetails();
        }
    }

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