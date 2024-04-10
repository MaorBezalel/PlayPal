package com.hit.playpal.home.ui.fragments.chats;

import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.Fragment;

import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.imageview.ShapeableImageView;
import com.google.android.material.textfield.TextInputLayout;
import com.hit.playpal.R;
import com.hit.playpal.home.ui.viewmodels.CreateGroupChatRoomViewModel;

public class CreateGroupChatRoomDialogFragment extends DialogFragment {
    private static final String TAG = "CreateGroupChatRoomDialogFragment";
    private CreateGroupChatRoomViewModel mViewModel = new CreateGroupChatRoomViewModel();
    public CreateGroupChatRoomViewModel getViewModel() {
        return mViewModel;
    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();

        if (dialog != null) {
            int width = ViewGroup.LayoutParams.MATCH_PARENT;
            int height = ViewGroup.LayoutParams.MATCH_PARENT;
            dialog.getWindow().setLayout(width, height);
            dialog.getWindow().setBackgroundDrawable(null);
            dialog.getWindow().setWindowAnimations(R.style.DialogAnimation);
        }
    }

    @Override
    public View onCreateView(@NonNull LayoutInflater iInflater, ViewGroup iContainer, Bundle iSavedInstanceState) {
        return iInflater.inflate(R.layout.fragment_create_group_chat_room_dialog, iContainer, false);
    }

    @Override
    public void onViewCreated(@NonNull View iView, Bundle iSavedInstanceState) {
        super.onViewCreated(iView, iSavedInstanceState);

        // Create a new instance of GroupChatDetailsFormFragment
        GroupChatDetailsFormFragment groupChatDetailsFormFragment = new GroupChatDetailsFormFragment();

        // Use a FragmentTransaction to add the fragment to the current activity
        getChildFragmentManager().beginTransaction()
                .replace(R.id.linearlayout_create_group_chat_room_dialog, groupChatDetailsFormFragment)
                .commit();
    }

}