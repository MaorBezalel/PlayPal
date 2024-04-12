package com.hit.playpal.home.ui.fragments.chats;

import android.content.Context;
import android.os.Bundle;

import androidx.activity.OnBackPressedCallback;
import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.paging.LoadState;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.TextView;
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
    private ProgressBar mProgressBar;
    private TextView mNoResultsFound;
    private TextView mDbError;

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
        initProgressBarAndLoadingState(iView);
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

    private void initProgressBarAndLoadingState(@NonNull View iView) {
        mProgressBar = iView.findViewById(R.id.progressbar_create_group_chat_game_list_loading);
        mNoResultsFound = iView.findViewById(R.id.textview_create_group_chat_no_games_found);
        mDbError = iView.findViewById(R.id.textview_create_group_chat_game_list_error);

        if (mCreateGroupChatGamesAdapter == null) {
            throw new IllegalStateException("Adapter not initialized");
        } else {
            mCreateGroupChatGamesAdapter.addLoadStateListener(loadStates -> {
                if (loadStates.getRefresh() instanceof LoadState.Loading) {
                    mProgressBar.setVisibility(View.VISIBLE);
                    mNoResultsFound.setVisibility(View.INVISIBLE);
                    mDbError.setVisibility(View.INVISIBLE);
                    mGameListRecyclerView.setVisibility(View.INVISIBLE);
                } else if (loadStates.getRefresh() instanceof LoadState.Error) {
                    mProgressBar.setVisibility(View.INVISIBLE);
                    mNoResultsFound.setVisibility(View.INVISIBLE);
                    mDbError.setVisibility(View.VISIBLE);
                    mGameListRecyclerView.setVisibility(View.INVISIBLE);
                } else if (loadStates.getRefresh() instanceof LoadState.NotLoading) {
                    if (mCreateGroupChatGamesAdapter.getItemCount() == 0) {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        mNoResultsFound.setVisibility(View.VISIBLE);
                        mDbError.setVisibility(View.INVISIBLE);
                        mGameListRecyclerView.setVisibility(View.INVISIBLE);
                    } else {
                        mProgressBar.setVisibility(View.INVISIBLE);
                        mNoResultsFound.setVisibility(View.INVISIBLE);
                        mDbError.setVisibility(View.INVISIBLE);
                        mGameListRecyclerView.setVisibility(View.VISIBLE);
                    }
                }

                return null;
            });
        }
    }
}