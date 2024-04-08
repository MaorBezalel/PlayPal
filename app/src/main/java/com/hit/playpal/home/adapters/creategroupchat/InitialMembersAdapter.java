package com.hit.playpal.home.adapters.creategroupchat;

import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.lifecycle.LifecycleOwner;
import androidx.paging.PagingConfig;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.android.material.card.MaterialCardView;
import com.google.firebase.firestore.Query;
import com.hit.playpal.R;
import com.hit.playpal.entities.users.User;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class InitialMembersAdapter extends FirestorePagingAdapter<User, InitialMembersAdapter.InitialMembersViewHolder> {
    private static final String TAG = "InitialMembersAdapter";
    private static final int PAGE_SIZE = 20;
    private static final int PAGE_PREFETCH_DISTANCE = 5;
    public static final int MINIMUM_NUMBER_OF_MEMBERS = 2;

    private Set<MaterialCardView> mSelectedCardViews;

    private Set<User> mSelectedUsers;
    public Set<User> getSelectedUsers() {
        return mSelectedUsers;
    }


    private static final PagingConfig PAGING_CONFIG = new PagingConfig(PAGE_SIZE, PAGE_PREFETCH_DISTANCE, false);

    public InitialMembersAdapter(Query iQuery, LifecycleOwner iOwner) {
        super(new FirestorePagingOptions.Builder<User>()
                .setLifecycleOwner(iOwner)
                .setQuery(iQuery, PAGING_CONFIG, User.class)
                .build()
        );

        mSelectedCardViews = new HashSet<>();
        mSelectedUsers = new HashSet<>();
    }

    public static class InitialMembersViewHolder extends RecyclerView.ViewHolder {
        public final MaterialCardView CARD_VIEW;
        public final ImageView PROFILE_PICTURE_VIEW;
        public final TextView USERNAME_TEXT_VIEW;

        public InitialMembersViewHolder(@NonNull View iItemView) {
            super(iItemView);

            CARD_VIEW = iItemView.findViewById(R.id.cardview_create_group_chat_initial_member);
            PROFILE_PICTURE_VIEW = iItemView.findViewById(R.id.imageview_create_group_chat_initial_member_profile_picture);
            USERNAME_TEXT_VIEW = iItemView.findViewById(R.id.textview_create_group_chat_initial_member_username);
        }
    }

    @Override
    protected void onBindViewHolder(@NonNull InitialMembersViewHolder iHolder, int iPosition, @NonNull User iCurrentUser) {
        iHolder.USERNAME_TEXT_VIEW.setText(iCurrentUser.getUsername());
        Picasso.get().load(iCurrentUser.getProfilePicture()).into(iHolder.PROFILE_PICTURE_VIEW);

        iHolder.CARD_VIEW.setOnClickListener(v -> {
            if (mSelectedCardViews.contains(iHolder.CARD_VIEW)) {
                mSelectedCardViews.remove(iHolder.CARD_VIEW);
                mSelectedUsers.remove(iCurrentUser);
                iHolder.CARD_VIEW.setChecked(false);
            } else {
                mSelectedCardViews.add(iHolder.CARD_VIEW);
                mSelectedUsers.add(iCurrentUser);
                iHolder.CARD_VIEW.setChecked(true);
            }
        });
    }

    @NonNull
    @Override
    public InitialMembersViewHolder onCreateViewHolder(@NonNull ViewGroup iParent, int iViewType) {
        View view = View.inflate(iParent.getContext(), R.layout.item_initial_member_for_group_creation, null);
        return new InitialMembersViewHolder(view);
    }
}