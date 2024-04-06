package com.hit.playpal.home.ui.fragments;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.hit.playpal.R;
import com.hit.playpal.chatrooms.ui.activities.TestChatRoomActivity;

public class ChatsFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater iInflater, ViewGroup iContainer, Bundle iSavedInstanceState) {
        return iInflater.inflate(R.layout.fragment_chats, iContainer, false);
    }

    @Override
    public void onViewCreated(View iView, Bundle iSavedInstanceState) {
        super.onViewCreated(iView, iSavedInstanceState);

        // TEST!
        // Create an Intent to start TestChatRoomActivity
        Intent intent = new Intent(getContext(), TestChatRoomActivity.class);
        startActivity(intent);
        getActivity().finish();
    }
}