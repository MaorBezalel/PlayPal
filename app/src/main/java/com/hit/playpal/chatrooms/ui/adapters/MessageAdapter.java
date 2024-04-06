package com.hit.playpal.chatrooms.ui.adapters;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagingData;
import androidx.paging.PagingDataAdapter;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import com.hit.playpal.entities.chats.Message;
import com.hit.playpal.R;
import com.squareup.picasso.Picasso;

public class MessageAdapter extends PagingDataAdapter<Message, MessageAdapter.MessageViewHolder> {
    private static final String TAG = "MessageAdapter";

    public MessageAdapter() {
        super(DIFF_CALLBACK);
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup iViewGroup, int iViewType) {
        Log.d(TAG, "onCreateViewHolder: creating a new view holder");
        View view = LayoutInflater.from(iViewGroup.getContext()).inflate(R.layout.item_chat_room_message, iViewGroup, false);
        return new MessageViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MessageViewHolder iHolder, int iPosition) {
        Message message = getItem(iPosition);

        if (message != null) {
            Log.d(TAG, "onBindViewHolder: message at position " + iPosition + " is not null");
            iHolder.bind(message);
        } else {
            Log.e(TAG, "onBindViewHolder: message at position " + iPosition + " is null");
        }
    }

    public void insertNewMessage(Message iMessage) {
//        if (iMessage != null) {
//            // FINDINGS: The list is immutable, so it is not possible to add an item to it directly.
////            snapshot().add(0, iMessage);
////            notifyItemInserted(0);
//        } else {
//            Log.e(TAG, "insertNewMessage: message is null");
//        }
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        private final ImageView MESSAGE_SENDER_PROFILE_PICTURE;
        private final TextView MESSAGE_SENDER_NAME;
        private final TextView MESSAGE_TIME;
        private final TextView MESSAGE_BODY;

        public MessageViewHolder(View iView){
            super(iView);
            MESSAGE_SENDER_PROFILE_PICTURE = iView.findViewById(R.id.imageview_message_sender_profile_picture);
            MESSAGE_SENDER_NAME = iView.findViewById(R.id.textview_message_sender_name);
            MESSAGE_TIME = iView.findViewById(R.id.textview_message_time);
            MESSAGE_BODY = iView.findViewById(R.id.textview_message_body);
        }

        public void bind(@NonNull Message iMessage) {
            Picasso.get().load(iMessage.getSender().getProfilePicture()).into(MESSAGE_SENDER_PROFILE_PICTURE);
            MESSAGE_SENDER_NAME.setText(iMessage.getSender().getDisplayName());
            MESSAGE_TIME.setText(iMessage.getSentAt().toString());
            MESSAGE_BODY.setText(iMessage.getContent().getData()); // for now, we assume that the content is a text message
        }
    }

    private static final DiffUtil.ItemCallback<Message> DIFF_CALLBACK =
            new DiffUtil.ItemCallback<Message>() {
                @Override
                public boolean areItemsTheSame(@NonNull Message iOldItem, @NonNull Message iNewItem) {
                    return iOldItem.getId().equals(iNewItem.getId());
                }

                @Override
                public boolean areContentsTheSame(@NonNull Message iOldItem, @NonNull Message iNewItem) {
                    return iOldItem.equals(iNewItem);
                }
            };
}