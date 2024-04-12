package com.hit.playpal.home.ui.fragments.chats;

import android.content.Context;
import android.os.Bundle;

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
import com.hit.playpal.entities.users.User;
import com.hit.playpal.home.ui.adapters.creategroupchat.CreateGroupChatInitialMembersAdapter;
import com.hit.playpal.home.domain.usecases.users.GenerateQueryForAllUsersUseCase;
import com.hit.playpal.home.ui.viewmodels.CreateGroupChatRoomViewModel;
import com.hit.playpal.utils.CurrentlyLoggedUser;

import java.util.Set;

public class GroupChatInitialMembersFormFragment extends Fragment {
    private static final String TAG = "GroupChatGameFormFragment";
    private CreateGroupChatRoomDialogFragment mFragmentParent;
    private ImageButton mBackButton;
    private MaterialButton mCreateGroupChatButton;
    private RecyclerView mInitialMembersRecyclerView;
    private LinearLayoutManager mLinearLayoutManager;
    private CreateGroupChatInitialMembersAdapter mCreateGroupChatInitialMembersAdapter;
    private ProgressBar mRecyclerViewProgressBar;
    private ProgressBar mCreateGroupChatProgressBar;
    private TextView mNoResultsFound;
    private TextView mDbError;


    @Override
    public View onCreateView(@NonNull LayoutInflater iInflater, ViewGroup container, Bundle savedInstanceState) {
        return iInflater.inflate(R.layout.fragment_group_chat_initial_members_form, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View iView, Bundle savedInstanceState) {
        super.onViewCreated(iView, savedInstanceState);

        initBackButton(iView);
        initCreateGroupChatButton(iView);
        initInitialMembersRecyclerView(iView);
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

    private void initCreateGroupChatButton(@NonNull View iView) {
        mCreateGroupChatButton = iView.findViewById(R.id.button_create_group_chat_create);
        mCreateGroupChatButton.setOnClickListener(v -> {
            User currentlyLoggedUser = CurrentlyLoggedUser.getCurrentlyLoggedUser();
            Set<User> selectedUsers = mCreateGroupChatInitialMembersAdapter.getSelectedUsers();
            int minimumNumberOfMembers = CreateGroupChatInitialMembersAdapter.MINIMUM_NUMBER_OF_MEMBERS;

            if (selectedUsers.size() < minimumNumberOfMembers) {
                Toast.makeText(requireContext(), "Please select at least " + minimumNumberOfMembers + " initial members", Toast.LENGTH_SHORT).show();
            } else {
                CreateGroupChatRoomViewModel viewModel = mFragmentParent.getViewModel();

                mCreateGroupChatProgressBar = iView.findViewById(R.id.progressbar_create_group_chat_create);
                mCreateGroupChatProgressBar.setVisibility(View.VISIBLE);
                mCreateGroupChatButton.setVisibility(View.INVISIBLE);

                viewModel.setGroupChatRoomInitialMembers(currentlyLoggedUser, selectedUsers);
                viewModel.createGroupChatRoom(this::handleCreateGroupChatRoomSuccess, this::handleCreateGroupChatRoomFailure);
            }
        });
    }

    private void initInitialMembersRecyclerView(@NonNull View iView) {
        mInitialMembersRecyclerView = iView.findViewById(R.id.recyclerview_create_group_chat_initial_members);
        mLinearLayoutManager = new LinearLayoutManager(requireContext());
        mInitialMembersRecyclerView.setLayoutManager(mLinearLayoutManager);

        mCreateGroupChatInitialMembersAdapter = new CreateGroupChatInitialMembersAdapter(GenerateQueryForAllUsersUseCase.invoke(
                CurrentlyLoggedUser.getCurrentlyLoggedUser().getUsername()),
                getViewLifecycleOwner()
        );
        mInitialMembersRecyclerView.setAdapter(mCreateGroupChatInitialMembersAdapter);
    }

    private void handleCreateGroupChatRoomSuccess() {
        mCreateGroupChatProgressBar.setVisibility(View.INVISIBLE);
        mCreateGroupChatButton.setVisibility(View.VISIBLE);

        Toast.makeText(requireContext(), "Group chat room created successfully", Toast.LENGTH_SHORT).show();
        mFragmentParent.dismiss();
    }

    private void handleCreateGroupChatRoomFailure() {
        mCreateGroupChatProgressBar.setVisibility(View.INVISIBLE);
        mCreateGroupChatButton.setVisibility(View.VISIBLE);

        Toast.makeText(requireContext(), "Failed to create group chat room, please try again later", Toast.LENGTH_SHORT).show();
    }

    private void initProgressBarAndLoadingState(@NonNull View iView) {
        mRecyclerViewProgressBar = iView.findViewById(R.id.progressbar_create_group_chat_initial_members);
        mNoResultsFound = iView.findViewById(R.id.textview_create_group_chat_initial_members_no_members);
        mDbError = iView.findViewById(R.id.textview_create_group_chat_initial_members_error);

        if (mCreateGroupChatInitialMembersAdapter == null) {
            throw new IllegalStateException("Adapter not initialized");
        } else {
            mCreateGroupChatInitialMembersAdapter.addLoadStateListener(loadStates -> {
                if (loadStates.getRefresh() instanceof LoadState.Loading) {
                    mRecyclerViewProgressBar.setVisibility(View.VISIBLE);
                    mNoResultsFound.setVisibility(View.INVISIBLE);
                    mDbError.setVisibility(View.INVISIBLE);
                    mInitialMembersRecyclerView.setVisibility(View.INVISIBLE);
                } else if (loadStates.getRefresh() instanceof LoadState.Error) {
                    mRecyclerViewProgressBar.setVisibility(View.INVISIBLE);
                    mNoResultsFound.setVisibility(View.INVISIBLE);
                    mDbError.setVisibility(View.VISIBLE);
                    mInitialMembersRecyclerView.setVisibility(View.INVISIBLE);
                } else if (loadStates.getRefresh() instanceof LoadState.NotLoading) {
                    if (mCreateGroupChatInitialMembersAdapter.getItemCount() == 0) {
                        mRecyclerViewProgressBar.setVisibility(View.INVISIBLE);
                        mNoResultsFound.setVisibility(View.VISIBLE);
                        mDbError.setVisibility(View.INVISIBLE);
                        mInitialMembersRecyclerView.setVisibility(View.INVISIBLE);
                    } else {
                        mRecyclerViewProgressBar.setVisibility(View.INVISIBLE);
                        mNoResultsFound.setVisibility(View.INVISIBLE);
                        mDbError.setVisibility(View.INVISIBLE);
                        mInitialMembersRecyclerView.setVisibility(View.VISIBLE);
                    }
                }

                return null;
            });
        }
    }
}