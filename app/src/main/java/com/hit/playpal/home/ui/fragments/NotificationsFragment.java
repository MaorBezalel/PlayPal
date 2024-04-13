package com.hit.playpal.home.ui.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.hit.playpal.R;
import com.hit.playpal.entities.relationships.enums.RelationshipStatus;
import com.hit.playpal.home.ui.adapters.notifications.FriendRequestsNotificationsAdapter;
import com.hit.playpal.home.ui.adapters.notifications.IFriendRequestsNotificationsAdapter;
import com.hit.playpal.home.ui.viewmodels.NotificationsViewModel;

public class NotificationsFragment extends Fragment {
    private static final String TAG = "NotificationsFragment";
    private FriendRequestsNotificationsAdapter mFriendRequestsNotificationsAdapter;
    private RecyclerView mFriendRequestsRecyclerView;
    private ProgressBar mProgressBar;
    private TextView mNoNotificationsErrorTextView;
    private TextView mDbErrorTextView;
    private NotificationsViewModel mNotificationsViewModel;

    @Override
    public View onCreateView(@NonNull LayoutInflater iInflater, ViewGroup iContainer, Bundle iSavedInstanceState) {
        View view = iInflater.inflate(R.layout.fragment_notifications, iContainer, false);

        initializeUI(view);
        initializeRecyclerView(view);
        setLoadingState();
        initializeViewModel();
        fetchFriendRequestsOfCurrentUser();

        return view;
    }

    private void initializeUI(View iView)
    {
        mFriendRequestsRecyclerView = iView.findViewById(R.id.fragment_notifications_list);
        mProgressBar = iView.findViewById(R.id.fragment_notifications_progressBar);
        mNoNotificationsErrorTextView = iView.findViewById(R.id.fragment_notifications_not_found_error);
        mDbErrorTextView = iView.findViewById(R.id.fragment_notifications_db_error);
    }

    private void initializeViewModel()
    {
        mNotificationsViewModel = new ViewModelProvider(this).get(NotificationsViewModel.class);
        mNotificationsViewModel.getFetchFriendRequestsSuccess().observe(getViewLifecycleOwner(), iSuccess -> {
            if(mNotificationsViewModel.getFriendRequests().getValue().isEmpty())
            {
                setNoItemsState();
            }
            else
            {
                mFriendRequestsNotificationsAdapter.setFriendRequests(mNotificationsViewModel.getFriendRequests().getValue());
                setLoadedState();
            }
        });
        mNotificationsViewModel.getFetchFriendRequestsError().observe(getViewLifecycleOwner(), iError -> {
            setDbErrorState();
        });
        mNotificationsViewModel.getUpdateFriendRequestStatusSuccess().observe(getViewLifecycleOwner(), iSuccess -> {
            Toast.makeText(getContext(), iSuccess.name(), Toast.LENGTH_SHORT).show();
        });
        mNotificationsViewModel.getUpdateFriendRequestStatusError().observe(getViewLifecycleOwner(), iError -> {
            Toast.makeText(getContext(), iError, Toast.LENGTH_SHORT).show();
        });
    }

    private void initializeRecyclerView(View iView)
    {
        mFriendRequestsRecyclerView = iView.findViewById(R.id.fragment_notifications_list);
        mFriendRequestsNotificationsAdapter = new FriendRequestsNotificationsAdapter(
                new IFriendRequestsNotificationsAdapter() {
                    @Override
                    public void onFriendRequestAccept(String iNotifierUid, ProgressBar iProgressBar, TextView iAcceptedText, TextView iRejectedText) {
                        mNotificationsViewModel.updatePendingFriendRequest(iNotifierUid, RelationshipStatus.friends)
                                .whenComplete((aVoid, throwable) -> {
                                    if(throwable == null)
                                    {
                                        iAcceptedText.setVisibility(View.VISIBLE);
                                        iRejectedText.setVisibility(View.GONE);
                                        iProgressBar.setVisibility(View.GONE);
                                    }
                                });
                    }

                    @Override
                    public void onFriendRequestReject(String iNotifierUid, ProgressBar iProgressBar, TextView iAcceptedText, TextView iRejectedText) {
                        mNotificationsViewModel.updatePendingFriendRequest(iNotifierUid, RelationshipStatus.none)
                                .whenComplete((aVoid, throwable) -> {
                                    if(throwable == null)
                                    {
                                        iAcceptedText.setVisibility(View.GONE);
                                        iRejectedText.setVisibility(View.VISIBLE);
                                        iProgressBar.setVisibility(View.GONE);
                                    }
                                });
                    }
                });
        mFriendRequestsRecyclerView.setAdapter(mFriendRequestsNotificationsAdapter);
        mFriendRequestsRecyclerView.setLayoutManager(new LinearLayoutManager(getContext()));
    }

    private void fetchFriendRequestsOfCurrentUser()
    {
        mNotificationsViewModel.fetchFriendRequestsOfCurrentUser();
    }

    private void setLoadingState()
    {
        mProgressBar.setVisibility(View.VISIBLE);
        mFriendRequestsRecyclerView.setVisibility(View.GONE);
        mNoNotificationsErrorTextView.setVisibility(View.GONE);
        mDbErrorTextView.setVisibility(View.GONE);
    }

    private void setNoItemsState()
    {
        mProgressBar.setVisibility(View.GONE);
        mFriendRequestsRecyclerView.setVisibility(View.GONE);
        mNoNotificationsErrorTextView.setVisibility(View.VISIBLE);
        mDbErrorTextView.setVisibility(View.GONE);
    }

    private void setLoadedState()
    {
        mProgressBar.setVisibility(View.GONE);
        mFriendRequestsRecyclerView.setVisibility(View.VISIBLE);
        mNoNotificationsErrorTextView.setVisibility(View.GONE);
        mDbErrorTextView.setVisibility(View.GONE);
    }

    private void setDbErrorState()
    {
        mProgressBar.setVisibility(View.GONE);
        mFriendRequestsRecyclerView.setVisibility(View.GONE);
        mNoNotificationsErrorTextView.setVisibility(View.GONE);
        mDbErrorTextView.setVisibility(View.VISIBLE);
    }

}
