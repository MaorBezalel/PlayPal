package com.hit.playpal.home.ui.adapters.notifications;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.hit.playpal.R;
import com.hit.playpal.entities.notifications.Notification;
import com.hit.playpal.utils.DateUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class FriendRequestsNotificationsAdapter extends RecyclerView.Adapter<FriendRequestsNotificationsAdapter.FriendRequestsNotificationsViewHolder> {

    private List<Notification> mFriendRequests;
    private final IFriendRequestsNotificationsAdapter mFriendRequestsNotificationsAdapter;

    public FriendRequestsNotificationsAdapter(IFriendRequestsNotificationsAdapter iFriendRequestsNotificationsAdapter) {
        mFriendRequests = new ArrayList<>();
        mFriendRequestsNotificationsAdapter = iFriendRequestsNotificationsAdapter;
    }

    @NonNull
    @Override
    public FriendRequestsNotificationsViewHolder onCreateViewHolder(@NonNull ViewGroup iParent, int viewType) {
        View view = LayoutInflater.from(iParent.getContext()).inflate(R.layout.fragment_notifications_friend_request_card, iParent, false);
        return new FriendRequestsNotificationsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull FriendRequestsNotificationsViewHolder holder, int position) {
        Notification notification = mFriendRequests.get(position);

        holder.MESSAGE_CONTNET.setText(notification.getNotifierName() + " sent you a friend request");
        holder.TIME_SENT.setText(DateUtils.getRelativeTimeDisplay(notification.getHappenedAt().getTime()));
        holder.ACCEPT_BUTTON.setOnClickListener(v -> {
            mFriendRequestsNotificationsAdapter.onFriendRequestAccept(
                    notification.getNotifierUid(),
                    holder.PROGRESS_BAR,
                    holder.ACCEPTED_TEXT,
                    holder.REJECTED_TEXT);
            holder.PROGRESS_BAR.setVisibility(View.VISIBLE);
            holder.BUTTONS_LAYOUT.setVisibility(View.GONE);
        });
        holder.REJECT_BUTTON.setOnClickListener(v -> {
            mFriendRequestsNotificationsAdapter.onFriendRequestReject(
                    notification.getNotifierUid(),
                    holder.PROGRESS_BAR,
                    holder.ACCEPTED_TEXT,
                    holder.REJECTED_TEXT);
            holder.PROGRESS_BAR.setVisibility(View.VISIBLE);
            holder.BUTTONS_LAYOUT.setVisibility(View.GONE);
        });

        if(notification.getNotifierImage() == null || notification.getNotifierImage().trim().isEmpty())
        {
            holder.SENDER_PROFILE_IMAGE.setImageResource(R.drawable.ic_home_nav_myprofile);
        }
        else
        {
            Picasso.get().load(notification.getNotifierImage()).into(holder.SENDER_PROFILE_IMAGE);
        }
    }

    @Override
    public int getItemCount() {
        return mFriendRequests.size();
    }

    public static class FriendRequestsNotificationsViewHolder extends RecyclerView.ViewHolder{
        private final ShapeableImageView SENDER_PROFILE_IMAGE;
        private final TextView MESSAGE_CONTNET;
        private final TextView TIME_SENT;
        private final Button ACCEPT_BUTTON;
        private final Button REJECT_BUTTON;
        private final ProgressBar PROGRESS_BAR;
        private final LinearLayout BUTTONS_LAYOUT;
        private final TextView ACCEPTED_TEXT;
        private final TextView REJECTED_TEXT;

        public FriendRequestsNotificationsViewHolder(@NonNull View itemView) {
            super(itemView);
            SENDER_PROFILE_IMAGE = itemView.findViewById(R.id.fragment_notifications_friend_request_userImage);
            MESSAGE_CONTNET = itemView.findViewById(R.id.fragment_nofitications_friend_request_message);
            ACCEPT_BUTTON = itemView.findViewById(R.id.fragment_notification_friend_request_acceptBtn);
            REJECT_BUTTON = itemView.findViewById(R.id.fragment_notification_friend_request_rejectBtn);
            TIME_SENT = itemView.findViewById(R.id.fragment_notifications_time);
            PROGRESS_BAR = itemView.findViewById(R.id.fragment_notifications_friend_request_progressBar);
            BUTTONS_LAYOUT = itemView.findViewById(R.id.fragment_notification_buttonsLayout);
            ACCEPTED_TEXT = itemView.findViewById(R.id.fragment_notifications_friend_request_accepted_msg);
            REJECTED_TEXT = itemView.findViewById(R.id.fragment_notifications_friend_request_rejected_msg);
        }
    }

    public void setFriendRequests(List<Notification> iFriendRequestsNotifications)
    {
        mFriendRequests.addAll(iFriendRequestsNotifications);
        notifyDataSetChanged();
    }
}
