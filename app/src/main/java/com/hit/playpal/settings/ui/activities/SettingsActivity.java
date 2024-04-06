package com.hit.playpal.settings.ui.activities;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.graphics.drawable.LayerDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.hit.playpal.R;
import com.hit.playpal.home.ui.activities.HomeActivity;
import com.hit.playpal.profile.ui.activities.ProfileActivity;
import com.hit.playpal.settings.ui.utilities.UserPermissions;
import com.hit.playpal.utils.CurrentlyLoggedUser;

import java.io.IOException;

public class SettingsActivity extends AppCompatActivity {

    private final String  currentUser = CurrentlyLoggedUser.getCurrentlyLoggedUser().getUid();
    private TextView textViewSetDisplayName;
    private TextView textViewSetUserName;
    private TextView textViewSetAboutMe;
    private ImageView imageSetViewAvatar;
    private Button buttonSettingsReturn;
    private static final int PICK_IMAGE_REQUEST = 1;


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
        textViewSetDisplayName = findViewById(R.id.textViewSetDisplayName);
        textViewSetUserName = findViewById(R.id.textViewSetUserName);
        textViewSetAboutMe = findViewById(R.id.textViewSetAboutMe);
        imageSetViewAvatar = findViewById(R.id.imageSetViewAvatar);

        textViewSetDisplayName.setText(displayName);
        textViewSetUserName.setText(userName);
        textViewSetAboutMe.setText(aboutMe);
        assert avatarImage != null;
        if (!avatarImage.isEmpty()) {
            imageSetViewAvatar.setImageResource(Integer.parseInt(avatarImage));
        }

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
            Uri selectedImageUri = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(this.getContentResolver(), selectedImageUri);
                Drawable drawable = new BitmapDrawable(getResources(), bitmap);

                // Get the edit icon drawable
                Drawable editIcon = ContextCompat.getDrawable(this, R.drawable.edit);

                // Create a new LayerDrawable with the new bitmap and the edit icon
                Drawable[] layers = new Drawable[2];
                layers[0] = drawable;
                layers[1] = editIcon;
                LayerDrawable layerDrawable = new LayerDrawable(layers);

                // Set the new LayerDrawable to the ImageView
                imageSetViewAvatar.setImageDrawable(layerDrawable);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}