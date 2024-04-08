package com.hit.playpal.home.ui.fragments.chats;

import android.os.Bundle;

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
import com.hit.playpal.home.adapters.creategroupchat.GamesAdapter;
import com.hit.playpal.home.domain.usecases.games.GenerateQueryForAllGamesUseCase;

public class GroupChatGameFormFragment extends Fragment {
    private static final String TAG = "GroupChatGameFormFragment";

    private ImageButton mBackButton;
    private MaterialButton mNextButton;
    private RecyclerView mGameListRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private GamesAdapter mGamesAdapter;

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

    private void initBackButton(@NonNull View iView) {
        mBackButton = iView.findViewById(R.id.imagebutton_create_group_chat_back);
        mBackButton.setOnClickListener(v -> requireActivity().onBackPressed());
    }

    private void initNextButton(@NonNull View iView) {
        mNextButton = iView.findViewById(R.id.button_create_group_chat_next);
        mNextButton.setOnClickListener(v -> {
            if (mGamesAdapter.getSelectedGame() == null) {
                Toast.makeText(requireContext(), "Please select a game", Toast.LENGTH_SHORT).show();
                return;
            }

            // TODO: Navigate to the next fragment
        });
    }

    private void initGameListRecyclerView(@NonNull View iView) {
        mGameListRecyclerView = iView.findViewById(R.id.recyclerview_create_group_chat_game_list);
        mLinearLayoutManager = new LinearLayoutManager(requireContext());
        mGamesAdapter = new GamesAdapter(GenerateQueryForAllGamesUseCase.invoke(), getViewLifecycleOwner());

        mGameListRecyclerView.setLayoutManager(mLinearLayoutManager);
        mGameListRecyclerView.setAdapter(mGamesAdapter);
    }
}