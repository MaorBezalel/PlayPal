package com.hit.playpal.home.ui.fragments.chats;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputLayout;
import com.hit.playpal.R;

public class GroupChatDetailsFormFragment extends Fragment {
    private static final String TAG = "CreateGroupChatRoomDialogFragment";
    private CreateGroupChatRoomDialogFragment mFragmentParent;
    private ImageButton mCloseDialogButton;
    private ShapeableImageView mGroupChatRoomImage;
    private TextInputLayout mGroupChatRoomName;
    private TextInputLayout mGroupChatRoomDescription;
    private MaterialButton mNextButton;

    private final ActivityResultLauncher<Intent> mGetContent = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(), result -> {
                if (result.getResultCode() == Activity.RESULT_OK) {
                    // There are no request codes
                    Intent data = result.getData();
                    if (data != null) {
                        Uri selectedImage = data.getData();
                        mGroupChatRoomImage.setImageURI(selectedImage);
                    }
                }
            });

    @Override
    public View onCreateView(@NonNull LayoutInflater iInflater, ViewGroup iContainer,
                             Bundle savedInstanceState) {
        return iInflater.inflate(R.layout.fragment_group_chat_details_form, iContainer, false);
    }

    @Override
    public void onViewCreated(@NonNull View iView, Bundle iSavedInstanceState) {
        super.onViewCreated(iView, iSavedInstanceState);

        initCloseDialogButton(iView);
        initGroupChatRoomImage(iView);
        initGroupChatRoomName(iView);
        initGroupChatRoomDescription(iView);
        initNextButton(iView);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mFragmentParent = (CreateGroupChatRoomDialogFragment) getParentFragment();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }

    private void initCloseDialogButton(@NonNull View iView) {
        mCloseDialogButton = iView.findViewById(R.id.imagebutton_create_group_chat_close);
        mCloseDialogButton.setOnClickListener(v -> {
            mFragmentParent.dismiss();
        });
    }

    private void initGroupChatRoomImage(@NonNull View iView) {
        mGroupChatRoomImage = iView.findViewById(R.id.imageview_create_group_chat_group_picture);
        mGroupChatRoomImage.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
            mGetContent.launch(intent);
        });
    }

    private void initGroupChatRoomName(@NonNull View iView) {
        mGroupChatRoomName = iView.findViewById(R.id.textinputlayout_create_group_chat_group_name);
    }

    private void initGroupChatRoomDescription(@NonNull View iView) {
        mGroupChatRoomDescription = iView.findViewById(R.id.textinputlayout_create_group_chat_group_description);
    }

    private void initNextButton(@NonNull View iView) {
        mNextButton = iView.findViewById(R.id.button_create_group_chat_next);
        mNextButton.setOnClickListener(this::handleNextButtonClick);
    }

    private void handleNextButtonClick(View iView) {
        String groupChatRoomName = mGroupChatRoomName.getEditText().getText().toString();
        String groupChatRoomDescription = mGroupChatRoomDescription.getEditText().getText().toString();

        if (isGroupChatRoomNameValid() && isGroupChatRoomDescriptionValid()) {
            mFragmentParent.getViewModel().GROUP_CHAT_ROOM_TO_CREATE.setName(groupChatRoomName);

            // TODO: Need to find a way to set the description (currently not part of the firestore document)
            //FRAGMENT_PARENT.getViewModel().GROUP_CHAT_ROOM_TO_CREATE.setDescription(groupChatRoomDescription);



        } else {
            Toast.makeText(getContext(), "Please address the errors in the form before proceeding...", Toast.LENGTH_SHORT).show();
        }
    }

    private boolean isGroupChatRoomNameValid() {
        boolean isValid = false;
        String groupChatRoomName = mGroupChatRoomName.getEditText().getText().toString();

        if (groupChatRoomName.isEmpty()) {
            mGroupChatRoomName.setError("Group chat room name cannot be empty");
        } else if (groupChatRoomName.length() < 3) {
            mGroupChatRoomName.setError("Group chat room name must be at least 3 characters long");
        } else if (groupChatRoomName.length() > 20) {
            mGroupChatRoomName.setError("Group chat room name must be at most 20 characters long");
        } else {
            isValid = true;
            mGroupChatRoomName.setError(null);
        }

        return isValid;
    }

    private boolean isGroupChatRoomDescriptionValid() {
        boolean isValid = false;
        String groupChatRoomDescription = mGroupChatRoomDescription.getEditText().getText().toString();

        if (groupChatRoomDescription.length() > 256) {
            mGroupChatRoomName.setError("Group chat room description must be at most 256 characters long");
        } else {
            isValid = true;
            mGroupChatRoomName.setError(null);
        }

        return isValid;
    }
}