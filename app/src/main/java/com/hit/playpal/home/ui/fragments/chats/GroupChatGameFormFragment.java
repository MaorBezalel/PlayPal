package com.hit.playpal.home.ui.fragments.chats;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.Toast;

import com.google.android.material.button.MaterialButton;
import com.hit.playpal.R;
import com.hit.playpal.entities.games.Game;
import com.hit.playpal.home.ui.adapters.creategroupchat.CreateGroupChatGamesAdapter;
import com.hit.playpal.home.domain.usecases.games.GenerateQueryForAllGamesUseCase;

public class GroupChatGameFormFragment extends Fragment {
    private static final String TAG = "GroupChatGameFormFragment";
    private CreateGroupChatRoomDialogFragment mFragmentParent;
    private ImageButton mBackButton;
    private MaterialButton mNextButton;
    private RecyclerView mGameListRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private CreateGroupChatGamesAdapter mCreateGroupChatGamesAdapter;
    private OnBackPressedCallback mOnBackPressedCallback;


    @Override
    public View onCreateView(@NonNull LayoutInflater iInflater, ViewGroup iContainer,
                             Bundle savedInstanceState) {
        return iInflater.inflate(R.layout.fragment_group_chat_game_form, iContainer, false);
    }

    @Override
    public void onViewCreated(@NonNull View iView, Bundle iSavedInstanceState) {
        super.onViewCreated(iView, iSavedInstanceState);

        initBackButton(iView);
        initNextButton(iView);
        initGameListRecyclerView(iView);
    }

    @Override
    public void onAttach(@NonNull Context context) {
        super.onAttach(context);
        mFragmentParent = (CreateGroupChatRoomDialogFragment) getParentFragment();
    }

    private void initBackButton(@NonNull View iView) {
        mBackButton = iView.findViewById(R.id.imagebutton_create_group_chat_back);
        mBackButton.setOnClickListener(v -> getParentFragmentManager().popBackStack());
    }

    private void initNextButton(@NonNull View iView) {
        mNextButton = iView.findViewById(R.id.button_create_group_chat_next);
        mNextButton.setOnClickListener(v -> {
            Game selectedGame = mCreateGroupChatGamesAdapter.getSelectedGame();

            if (selectedGame != null) {
                mFragmentParent.getViewModel().setGroupChatRoomGame(selectedGame);

                // Create a new instance of GroupChatInitialMembersFormFragment
                GroupChatInitialMembersFormFragment groupChatInitialMembersFormFragment = new GroupChatInitialMembersFormFragment();

                // Replace the current fragment with the new instance
                mFragmentParent.getChildFragmentManager().beginTransaction()
                        .replace(R.id.linearlayout_create_group_chat_room_dialog, groupChatInitialMembersFormFragment)
                        .addToBackStack(null) // add transaction to back stack to enable going back
                        .commit();
            } else {
                Toast.makeText(requireContext(), "Please select a game", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void initGameListRecyclerView(@NonNull View iView) {
        mGameListRecyclerView = iView.findViewById(R.id.recyclerview_create_group_chat_game_list);
        mLinearLayoutManager = new LinearLayoutManager(requireContext());
        mCreateGroupChatGamesAdapter = new CreateGroupChatGamesAdapter(GenerateQueryForAllGamesUseCase.invoke(), getViewLifecycleOwner());

        mGameListRecyclerView.setLayoutManager(mLinearLayoutManager);
        mGameListRecyclerView.setAdapter(mCreateGroupChatGamesAdapter);
    }
}