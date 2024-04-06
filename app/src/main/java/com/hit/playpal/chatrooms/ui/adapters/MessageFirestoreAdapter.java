package com.hit.playpal.chatrooms.ui.adapters;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.paging.PagingConfig;
import androidx.recyclerview.widget.RecyclerView;

import com.firebase.ui.firestore.paging.FirestorePagingAdapter;
import com.firebase.ui.firestore.paging.FirestorePagingOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.hit.playpal.R;
import com.hit.playpal.chatrooms.ui.fragments.ChatRoomBodyFragment;
import com.hit.playpal.entities.chats.Message;
import com.squareup.picasso.Picasso;

import java.text.SimpleDateFormat;
import java.util.Locale;

public class MessageFirestoreAdapter extends FirestorePagingAdapter<Message, MessageFirestoreAdapter.MessageViewHolder> {
    private static final int PAGE_SIZE = 20;
    private static final int PAGE_PREFETCH_DISTANCE = 5;
    private static final PagingConfig PAGING_CONFIG = new PagingConfig(PAGE_SIZE, PAGE_PREFETCH_DISTANCE, false);

    public MessageFirestoreAdapter(String iChatRoomId, ChatRoomBodyFragment iChatRoomBodyFragment) {
        super(new FirestorePagingOptions.Builder<Message>()
                .setLifecycleOwner(iChatRoomBodyFragment)
                .setQuery(
                        FirebaseFirestore.getInstance()
                                .collection("chat_rooms")
                                .document(iChatRoomId)
                                .collection("messages")
                                .orderBy("sent_at", Query.Direction.DESCENDING),
                        PAGING_CONFIG,
                        Message.class)
                .build());
    }

    @Override
    protected void onBindViewHolder(@NonNull MessageViewHolder iHolder, int iPosition, @NonNull Message iMessage) {
        SimpleDateFormat sdf = new SimpleDateFormat("MM/dd/yyyy HH:mm", Locale.US);

        Picasso.get().load(iMessage.getSender().getProfilePicture()).into(iHolder.MESSAGE_SENDER_PROFILE_PICTURE);
        iHolder.MESSAGE_SENDER_NAME.setText(iMessage.getSender().getDisplayName());
        iHolder.MESSAGE_TIME.setText(sdf.format(iMessage.getSentAt()));
        iHolder.MESSAGE_BODY.setText(iMessage.getContent().getData()); // for now, we assume that the content is a text message
    }

    @NonNull
    @Override
    public MessageViewHolder onCreateViewHolder(@NonNull ViewGroup iParent, int iViewType) {
        // Create a new instance of the ViewHolder, in this case we are using a custom
        // layout called R.layout.message for each item
        View view = LayoutInflater.from(iParent.getContext())
                .inflate(R.layout.item_chat_room_message, iParent, false);

        return new MessageViewHolder(view);
    }

    public static class MessageViewHolder extends RecyclerView.ViewHolder {
        public final ImageView MESSAGE_SENDER_PROFILE_PICTURE;
        public final TextView MESSAGE_SENDER_NAME;
        public final TextView MESSAGE_TIME;
        public final TextView MESSAGE_BODY;


        public MessageViewHolder(@NonNull View iView) {
            super(iView);
            MESSAGE_SENDER_PROFILE_PICTURE = iView.findViewById(R.id.imageview_message_sender_profile_picture);
            MESSAGE_SENDER_NAME = iView.findViewById(R.id.textview_message_sender_name);
            MESSAGE_TIME = iView.findViewById(R.id.textview_message_time);
            MESSAGE_BODY = iView.findViewById(R.id.textview_message_body);
        }
    }
}