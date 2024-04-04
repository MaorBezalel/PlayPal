package com.hit.playpal.profile.ui.adapters;


import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.hit.playpal.R;
import com.hit.playpal.entities.users.Relationship;
import com.squareup.picasso.Picasso;

import java.util.List;

public class FriendsAdapter extends RecyclerView.Adapter<FriendsAdapter.ViewHolder> {
    private List<Relationship> friends;
    private OnFriendClickListener onFriendClickListener;

    public FriendsAdapter(List<Relationship> friends, OnFriendClickListener onFriendClickListener) {
        this.friends = friends;
        this.onFriendClickListener = onFriendClickListener;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_friends_card, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Relationship relationship = friends.get(position);
        holder.displayName.setText(relationship.getOther_user().getDisplayName());

        // Load the profile picture using Picasso
        if (!relationship.getOther_user().getProfilePicture().isEmpty()) {
            Picasso.get().load(relationship.getOther_user().getProfilePicture()).into(holder.profilePicture);
        }

        // Set click listener
        holder.itemView.setOnClickListener(v -> onFriendClickListener.onFriendClick(relationship));
    }

    @Override
    public int getItemCount() {
        if (friends == null) {
            return 0;
        } else {
            return friends.size();
        }
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView displayName;
        ImageView profilePicture;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            displayName = itemView.findViewById(R.id.textViewGetDisplayNameOfFriend);
            profilePicture = itemView.findViewById(R.id.imageViewAvatarOfFriend);
        }
    }

    public interface OnFriendClickListener {
        void onFriendClick(Relationship relationship);
    }

}