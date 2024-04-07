package com.hit.playpal.chatrooms.ui.adapters;

import android.annotation.SuppressLint;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.hit.playpal.R;
import com.hit.playpal.entities.users.User;

import java.util.List;
import java.util.stream.Collectors;

public class UsersInGroupChatAdapter extends RecyclerView.Adapter<UsersInGroupChatAdapter.UserViewHolder>{
    private List<User> mUserList;
    private List<User> mCurrentFilteredUserList;

    public UsersInGroupChatAdapter(List<User> iUserList)
    {
        mCurrentFilteredUserList = mUserList = iUserList;
    }
    @NonNull
    @Override
    public UsersInGroupChatAdapter.UserViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fragment_search_user_card, parent, false);

        return new UsersInGroupChatAdapter.UserViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull UsersInGroupChatAdapter.UserViewHolder holder, int position) {
        User user = mCurrentFilteredUserList.get(position);
        holder.userDisplayName.setText(user.getDisplayName());

        // TODO: find a way to put userimage content inside imageView
        holder.userImage.setImageResource(R.drawable.ic_home_nav_myprofile);
    }

    @Override
    public int getItemCount() {
        return mCurrentFilteredUserList.size();
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

    public void applyFilters(String iUserName)
    {
        if(iUserName == null || iUserName.isEmpty())
        {
            mCurrentFilteredUserList = mUserList;
        }
        else
        {
            mCurrentFilteredUserList = mUserList.
                    stream().
                    filter(user -> user.getDisplayName().contains(iUserName)).
                    collect(Collectors.toList());
        }

        mUserList = mCurrentFilteredUserList;

        notifyDataSetChanged();
    }


}
