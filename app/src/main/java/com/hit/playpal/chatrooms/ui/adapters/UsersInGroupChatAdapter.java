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
import com.hit.playpal.entities.chats.group.GroupProfile;
import com.hit.playpal.paginatedsearch.users.adapters.IUserAdapter;
import com.squareup.picasso.Picasso;

import java.util.List;
import java.util.stream.Collectors;

public class UsersInGroupChatAdapter extends RecyclerView.Adapter<UsersInGroupChatAdapter.UserViewHolder>{
    private List<GroupProfile.Participant> mParticipantsList;
    private List<GroupProfile.Participant> mCurrentFilteredParticipantsList;
    private final IUserAdapter ON_USER_CLICKED;

    public UsersInGroupChatAdapter(List<GroupProfile.Participant> iUserList, IUserAdapter iUserAdapter)
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
        GroupProfile.Participant user = mCurrentFilteredParticipantsList.get(position);
        holder.userName.setText(user.getUsername());
        holder.userCard.setOnClickListener(v -> ON_USER_CLICKED.onUserClick(user.getUserUid()));

        if(user.getProfilePicture() != null && !user.getProfilePicture().isEmpty())
        {
            Picasso.get().load(user.getProfilePicture()).into(holder.userImage);
        }
        else
        {
            holder.userImage.setImageResource(R.drawable.ic_home_nav_myprofile);
        }
    }

    @Override
    public int getItemCount() {
        return mCurrentFilteredParticipantsList.size();
    }

    public static class UserViewHolder extends RecyclerView.ViewHolder {
        public final CardView userCard;
        public final ImageView userImage;
        public final TextView userName;

        public UserViewHolder(View view) {
            super(view);
            userCard = view.findViewById(R.id.userCard);
            userImage = view.findViewById(R.id.fragment_search_users_image);
            userName = view.findViewById(R.id.fragment_search_users_displayname);
        }
    }

    public void applyFilters(String iUserName)
    {
        if(iUserName == null || iUserName.isEmpty())
        {
            mCurrentFilteredParticipantsList = mParticipantsList;
        }
        else
        {
            mCurrentFilteredParticipantsList = mParticipantsList.
                    stream().
                    filter(user -> user.getUsername().contains(iUserName)).
                    collect(Collectors.toList());
        }

        notifyDataSetChanged();
    }


}
