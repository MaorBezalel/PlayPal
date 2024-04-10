package com.hit.playpal.settings.ui.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
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
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.snackbar.Snackbar;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.hit.playpal.R;
import com.hit.playpal.auth.ui.activities.AuthActivity;
import com.hit.playpal.profile.ui.activities.ProfileActivity;
import com.hit.playpal.settings.domain.usecases.CheckIfUserNameIsUniqueUseCase;
import com.hit.playpal.settings.domain.usecases.UpdateUserProfileUseCase;
import com.hit.playpal.settings.ui.utilities.SettingsValidations;
import com.hit.playpal.settings.ui.utilities.UserPermissions;
import com.hit.playpal.utils.CurrentlyLoggedUser;
import com.hit.playpal.utils.Out;
import com.squareup.picasso.Picasso;

import java.io.IOException;

public class SettingsActivity extends AppCompatActivity {

    private final String  currentUser = CurrentlyLoggedUser.getCurrentlyLoggedUser().getUid();
    private EditText editTextSetDisplayName;
    private EditText editTextSetUserName;
    private EditText editTextSetAboutMe;
    private ShapeableImageView imageSetViewAvatar;
    private Button buttonSettingsReturn;
    private Button buttonSaveSettings;

    private ProgressBar progressBarSavingSettings;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri selectedImageUri;
    private boolean isImageChanged;
    private String originalUserName;
    private FirebaseFirestore DB;
    private FirebaseAuth mAuth;
    private Button buttonLogOut;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_settings);
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        Intent intent = getIntent();
        String displayName = intent.getStringExtra("displayName");
        String userName = intent.getStringExtra("userName");
        String aboutMe = intent.getStringExtra("aboutMe");
        String avatarImage = intent.getStringExtra("avatarImage");

        buttonSettingsReturn = findViewById(R.id.buttonSettingsReturn);
        buttonSaveSettings = findViewById(R.id.buttonSaveSettings);
        progressBarSavingSettings = findViewById(R.id.progressBarSavingSettings);
        editTextSetDisplayName = findViewById(R.id.editTextSetDisplayName);
        editTextSetUserName = findViewById(R.id.editTextSetUserName);
        editTextSetAboutMe = findViewById(R.id.editTextSetAboutMe);
        imageSetViewAvatar = findViewById(R.id.imageSetViewAvatar);
        buttonLogOut = findViewById(R.id.buttonLogOut);
        DB = FirebaseFirestore.getInstance();
        mAuth = FirebaseAuth.getInstance();

        editTextSetDisplayName.setText(displayName);
        editTextSetUserName.setText(userName);
        editTextSetAboutMe.setText(aboutMe);
        originalUserName = editTextSetUserName.getText().toString().trim();
        if (avatarImage != null && !avatarImage.isEmpty()) {
            Picasso.get().load(avatarImage).into(imageSetViewAvatar);
            // Convert the URL to a Uri and assign it to selectedImageUri
            selectedImageUri = Uri.parse(avatarImage);
        }

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
                buttonSaveSettings.setVisibility(View.VISIBLE);
            }
        };

        editTextSetDisplayName.addTextChangedListener(textWatcher);
        editTextSetUserName.addTextChangedListener(textWatcher);
        editTextSetAboutMe.addTextChangedListener(textWatcher);

        buttonSettingsReturn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(SettingsActivity.this, ProfileActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
                startActivity(intent);
            }
        });

        imageSetViewAvatar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!UserPermissions.checkReadExternalStoragePermission(SettingsActivity.this) ||
                        !UserPermissions.checkReadMediaImagesPermission(SettingsActivity.this)) {
                    UserPermissions.requestReadExternalStoragePermission(SettingsActivity.this);
                    UserPermissions.requestReadMediaImagesPermission(SettingsActivity.this);
                } else {
                    openImagePicker();
                }
            }
        });
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           @NonNull String[] permissions,
                                           @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == UserPermissions.PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE ||
                requestCode == UserPermissions.PERMISSIONS_REQUEST_READ_MEDIA_IMAGES) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                openImagePicker();
            } else {
                Toast.makeText(this, "Permission denied to read your External storage", Toast.LENGTH_SHORT).show();
            }
            return;
        }
    }

    private void openImagePicker() {
        Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri newImageUri = data.getData();
            String filePath = getRealPathFromURI(newImageUri);
            Out<String> invalidationReason = Out.of(String.class);
            if (!SettingsValidations.isAvatarImageValid(filePath, invalidationReason)) {
                Snackbar.make(findViewById(android.R.id.content), invalidationReason.get(), Snackbar.LENGTH_LONG).show();
                return;
            }

            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), newImageUri);
                // Set the Bitmap directly to the ImageView
                imageSetViewAvatar.setImageBitmap(bitmap);
                // Only update selectedImageUri if everything was successful
                selectedImageUri = newImageUri;
                 isImageChanged = true;
                buttonSaveSettings.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else if (resultCode == RESULT_CANCELED) {
            // Handle the case where the user cancelled the image selection
            Toast.makeText(this, "Image selection cancelled", Toast.LENGTH_SHORT).show();
        }
    }
    public void saveSettings(View view) {
        String userName = editTextSetUserName.getText().toString().trim();
        String displayName = editTextSetDisplayName.getText().toString().trim();
        String aboutMe = editTextSetAboutMe.getText().toString().trim();
        Uri imageUri = isImageChanged ? selectedImageUri : null;

        if (performInputValidation(userName, displayName, aboutMe)) {
            if (!userName.equals(originalUserName)) {
                // The username has been changed, check its uniqueness
                CheckIfUserNameIsUniqueUseCase checkIfUserNameIsUniqueUseCase = new CheckIfUserNameIsUniqueUseCase();
                checkIfUserNameIsUniqueUseCase.isUsernameValidAndUnique(userName).addOnCompleteListener(new OnCompleteListener<Boolean>() {
                    @Override
                    public void onComplete(@NonNull Task<Boolean> task) {
                        if (task.isSuccessful()) {
                            updateProfile(userName, displayName, aboutMe, imageUri);
                        } else {
                            // Handle the error
                            Log.e("ProfileActivity", "Failed to validate username", task.getException());
                            editTextSetUserName.setError(task.getException().getMessage());
                        }
                    }
                });
            } else {
                // The username hasn't been changed, no need to check its uniqueness
                updateProfile(userName, displayName, aboutMe, imageUri);
            }
        }
    }

    private void updateProfile(String userName, String displayName, String aboutMe, Uri imageUri) {
        progressBarSavingSettings.setVisibility(View.VISIBLE);
        buttonSaveSettings.setVisibility(View.GONE);
        UpdateUserProfileUseCase updateUserProfileUseCase = new UpdateUserProfileUseCase();
        updateUserProfileUseCase.execute(currentUser, userName, displayName, aboutMe, imageUri).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                progressBarSavingSettings.setVisibility(View.GONE);

                if (task.isSuccessful()) {
                    Toast.makeText(SettingsActivity.this, "Profile updated successfully", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SettingsActivity.this, ProfileActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    // Handle the error
                    Log.e("SettingsActivity", "Failed to update profile", task.getException());
                    buttonSaveSettings.setVisibility(View.VISIBLE);
                    Toast.makeText(SettingsActivity.this, "Failed to update profile", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    private boolean performInputValidation(String iUsername, String iDisplayName,String iAboutMe) {
        boolean isValid = true;
        Out<String> invalidationReason = Out.of(String.class);

        if (!SettingsValidations.isUsernameValid(iUsername, invalidationReason)) {
            editTextSetUserName.setError(invalidationReason.get());
            isValid = false;
        } else {
            editTextSetUserName.setError(null);
        }

        if (!SettingsValidations.isDisplayNameValid(iDisplayName, invalidationReason)) {
            editTextSetDisplayName.setError(invalidationReason.get());
            isValid = false;
        } else {
            editTextSetDisplayName.setError(null);
        }

        if (!SettingsValidations.isAboutMeValid(iAboutMe, invalidationReason)) {
            editTextSetAboutMe.setError(invalidationReason.get());
            isValid = false;
        } else {
            editTextSetAboutMe.setError(null);
        }

        return isValid;
    }

    private String getRealPathFromURI(Uri contentUri) {
        String[] proj = { MediaStore.Images.Media.DATA };
        Cursor cursor = getContentResolver().query(contentUri, proj, null, null, null);
        if (cursor != null) {
            int column_index = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            String path = cursor.getString(column_index);
            cursor.close();
            return path;
        }
        return null;
    }

    public void logOutFunc(View view) {
        mAuth.signOut();
        Intent intent = new Intent(SettingsActivity.this, AuthActivity.class);
        startActivity(intent);
        finish();
    }
}