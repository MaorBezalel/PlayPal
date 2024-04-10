package com.hit.playpal.chatrooms.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.imageview.ShapeableImageView;
import com.hit.playpal.R;
import com.hit.playpal.entities.chats.Message;
import com.hit.playpal.utils.DateUtils;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.MessageViewHolder>{

    private List<Message> mMessages;

    public MessageAdapter() {
        mMessages = new ArrayList<>();
    }
    @NonNull
    @Override
    public MessageAdapter.MessageViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_chat_room_message, parent, false);

        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageAdapter.MessageViewHolder holder, int position) {
        Message message = mMessages.get(position);

        holder.MESSAGE_SENDER_NAME.setText(message.getSender().getDisplayName());
        holder.MESSAGE_TIME.setText(DateUtils.getRelativeTimeDisplay(message.getSentAt().getTime()));
        holder.MESSAGE_BODY.setText(message.getContent().getData());

        if (message.getSender().getProfilePicture() == null || message.getSender().getProfilePicture().isEmpty()) {
            holder.MESSAGE_SENDER_PROFILE_PICTURE.setImageResource(R.drawable.ic_home_nav_search_groupchats);
        } else {
            Picasso.get().load(message.getSender().getProfilePicture()).into(holder.MESSAGE_SENDER_PROFILE_PICTURE);
        }
    }

    @Override
    public int getItemCount() {
        return mMessages.size();
    }

    public void addNewMessages(List<Message> iNewMessages) {
       int lastPosition = mMessages.size();
       mMessages.addAll(iNewMessages);
       notifyItemRangeInserted(lastPosition, iNewMessages.size());
    }

    public void addNewMessage(Message iNewMessage) {
        mMessages.add(0, iNewMessage);
        notifyDataSetChanged();
    }


    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        private final ShapeableImageView MESSAGE_SENDER_PROFILE_PICTURE;
        private final TextView MESSAGE_SENDER_NAME;
        private final TextView MESSAGE_TIME;
        private final TextView MESSAGE_BODY;
        public MessageViewHolder(View iView) {
            super(iView);
            MESSAGE_SENDER_PROFILE_PICTURE = iView.findViewById(R.id.imageview_message_sender_profile_picture);
            MESSAGE_SENDER_NAME = iView.findViewById(R.id.textview_message_sender_name);
            MESSAGE_TIME = iView.findViewById(R.id.textview_message_time);
            MESSAGE_BODY = iView.findViewById(R.id.textview_message_body);
        }
    }
}
