package com.hit.playpal.chatrooms.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hit.playpal.R;
import com.hit.playpal.entities.chats.Participant;
import com.hit.playpal.paginatedsearch.users.adapters.IUserAdapter;

import java.util.List;
import java.util.stream.Collectors;

public class UsersInGroupChatAdapter extends RecyclerView.Adapter<UsersInGroupChatAdapter.UserViewHolder>{
    private List<Participant> mParticipantsList;
    private List<Participant> mCurrentFilteredParticipantsList;
    private final IUserAdapter ON_USER_CLICKED;

    public UsersInGroupChatAdapter(List<Participant> iUserList, IUserAdapter iUserAdapter)
    {
        mCurrentFilteredParticipantsList = mParticipantsList = iUserList;
        ON_USER_CLICKED = iUserAdapter;
    }
    @NonNull
    @Override
    public UsersInGroupChatAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_search_user_card, parent, false);

        return new UsersInGroupChatAdapter.UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersInGroupChatAdapter.UserViewHolder holder, int position) {
        Participant user = mCurrentFilteredParticipantsList.get(position);
        holder.userDisplayName.setText(user.getDisplayName());
        holder.userCard.setOnClickListener(v -> ON_USER_CLICKED.onUserClick(user.getUserUid()));

        // TODO: find a way to put userimage content inside imageView
        holder.userImage.setImageResource(R.drawable.ic_home_nav_myprofile);
    }

    @Override
    public int getItemCount() {
        return mCurrentFilteredParticipantsList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public final CardView userCard;
        public final ImageView userImage;
        public final TextView userDisplayName;

        public UserViewHolder(View view) {
            super(view);
            userCard = view.findViewById(R.id.userCard);
            userImage = view.findViewById(R.id.fragment_search_users_image);
            userDisplayName = view.findViewById(R.id.fragment_search_users_displayname);
        }
    }

    public void applyFilters(String iUserDisplayName)
    {
        if(iUserDisplayName == null || iUserDisplayName.isEmpty())
        {
            mCurrentFilteredParticipantsList = mParticipantsList;
        }
        else
        {
            mCurrentFilteredParticipantsList = mParticipantsList.
                    stream().
                    filter(user -> user.getDisplayName().contains(iUserDisplayName)).
                    collect(Collectors.toList());
        }

        notifyDataSetChanged();
    }


}
