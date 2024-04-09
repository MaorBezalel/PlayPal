package com.hit.playpal.home.adapters.recentchats;
import android.util.Log;
import android.view.LayoutInflater;
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
import com.hit.playpal.entities.chats.AllChatRoom;
import com.hit.playpal.entities.chats.ChatRoom;
import com.hit.playpal.entities.chats.OneToOneChatRoom;
import com.hit.playpal.entities.chats.GroupChatRoom;
import com.hit.playpal.entities.users.User;
import com.hit.playpal.utils.CurrentlyLoggedUser;
import com.hit.playpal.utils.DateUtils;
import com.squareup.picasso.Picasso;

import java.util.function.BiFunction;

public class ChatsRecyclerViewAdapter extends FirestorePagingAdapter<AllChatRoom, ChatsRecyclerViewAdapter.ChatsViewHolder>  {
    private static final String TAG = "ChatsAdapter";

    public static final int PAGE_SIZE = 20;
    public static final int PAGE_PREFETCH_DISTANCE = 5;
    public static final PagingConfig PAGING_CONFIG = new PagingConfig(PAGE_SIZE, PAGE_PREFETCH_DISTANCE, false);
    private final IChatsAdapter mChatsAdapter;
    private final LifecycleOwner mCurrentLifecycleOwner;
    private Query mQuery;

    public ChatsRecyclerViewAdapter(IChatsAdapter iChatsAdapter, LifecycleOwner iOwner, Query iBaseQuery) {
        super(new FirestorePagingOptions.Builder<AllChatRoom>()
                        .setLifecycleOwner(iOwner)
                        .setQuery(iBaseQuery, PAGING_CONFIG, AllChatRoom.class)
                        .build()
        );

        mChatsAdapter = iChatsAdapter;
        mQuery = iBaseQuery;
        mCurrentLifecycleOwner = iOwner;
    }

    @Override
    protected void onBindViewHolder(@NonNull ChatsViewHolder iHolder, int iPosition, @NonNull AllChatRoom iAllChatRoom) {
        ChatRoom chatRoom = iAllChatRoom.convertToChatRoom();

        determineAndSetChatRoomName(iHolder, chatRoom);
        determineAndSetChatRoomImage(iHolder, chatRoom);
        determineAndSetChatRoomLastMessageTime(iHolder, chatRoom);
        determineAndSetChatRoomLastMessageContent(iHolder, chatRoom);

        iHolder.CHAT_ROOM_CARD.setOnClickListener(iView -> mChatsAdapter.onChatRoomCardClicked(chatRoom));
    }

    @NonNull
    @Override
    public ChatsViewHolder onCreateViewHolder(@NonNull ViewGroup iParent, int iViewType) {
        View view = LayoutInflater.from(iParent.getContext()).inflate(R.layout.item_chat_room_card, iParent, false);
        return new ChatsViewHolder(view);
    }

    public static class ChatsViewHolder extends RecyclerView.ViewHolder {
        public final MaterialCardView CHAT_ROOM_CARD;
        public final ImageView CHAT_ROOM_IMAGE;
        public final TextView CHAT_ROOM_NAME;
        public final TextView CHAT_ROOM_LAST_MESSAGE_TIME;
        public final TextView CHAT_ROOM_LAST_MESSAGE_CONTENT;

        public ChatsViewHolder(@NonNull View iView) {
            super(iView);

            CHAT_ROOM_CARD = iView.findViewById(R.id.materialcardview_chats_chat_room_card);
            CHAT_ROOM_IMAGE = iView.findViewById(R.id.imageview_chats_chat_room_image);
            CHAT_ROOM_NAME = iView.findViewById(R.id.textview_chats_chat_room_name);
            CHAT_ROOM_LAST_MESSAGE_TIME = iView.findViewById(R.id.textview_chats_chat_room_last_message_time);
            CHAT_ROOM_LAST_MESSAGE_CONTENT = iView.findViewById(R.id.textview_chats_chat_room_last_message_content);
        }
    }

    public void updateQuery(Query iQuery) {
        mQuery = iQuery;
        FirestorePagingOptions<AllChatRoom> options = new FirestorePagingOptions.Builder<AllChatRoom>()
                .setLifecycleOwner(mCurrentLifecycleOwner)
                .setQuery(iQuery, PAGING_CONFIG, AllChatRoom.class)
                .build();

        super.updateOptions(options);
    }

    private void determineAndSetChatRoomName(@NonNull ChatsViewHolder iHolder, @NonNull ChatRoom iChatRoom) {
        if (iChatRoom instanceof OneToOneChatRoom) {
            OneToOneChatRoom oneToOneChatRoom = (OneToOneChatRoom) iChatRoom;
            User thisUser = CurrentlyLoggedUser.getCurrentlyLoggedUser();
            iHolder.CHAT_ROOM_NAME.setText(oneToOneChatRoom.getOtherUserDisplayName(thisUser.getUid()));
        } else if (iChatRoom instanceof GroupChatRoom) {
            GroupChatRoom groupChatRoom = (GroupChatRoom) iChatRoom;
            iHolder.CHAT_ROOM_NAME.setText(groupChatRoom.getName());
        } else {
            Log.e(TAG, "Unknown chat room type: " + iChatRoom.getClass().getName());
            iHolder.CHAT_ROOM_NAME.setText("Error: Unknown");
        }
    }

    private void determineAndSetChatRoomImage(@NonNull ChatsViewHolder iHolder, @NonNull ChatRoom iChatRoom) {
        if (iChatRoom instanceof OneToOneChatRoom) {
            OneToOneChatRoom oneToOneChatRoom = (OneToOneChatRoom) iChatRoom;
            User thisUser = CurrentlyLoggedUser.getCurrentlyLoggedUser();
            Picasso.get().load(oneToOneChatRoom.getOtherUserProfilePicture(thisUser.getUid())).into(iHolder.CHAT_ROOM_IMAGE);
        } else if (iChatRoom instanceof GroupChatRoom) {
            GroupChatRoom groupChatRoom = (GroupChatRoom) iChatRoom;
            Picasso.get().load(groupChatRoom.getProfilePicture()).into(iHolder.CHAT_ROOM_IMAGE);
        } else {
            Log.e(TAG, "Unknown chat room type: " + iChatRoom.getClass().getName());
        }
    }

    private void determineAndSetChatRoomLastMessageTime(@NonNull ChatsViewHolder iHolder, @NonNull ChatRoom iChatRoom) {
        long lastMessageTimestamp = iChatRoom.getLastMessage().getSentAt().getTime();
        String lastMessageTime = DateUtils.getRelativeTimeDisplay(lastMessageTimestamp);

        iHolder.CHAT_ROOM_LAST_MESSAGE_TIME.setText(lastMessageTime);
    }

    private void determineAndSetChatRoomLastMessageContent(@NonNull ChatsViewHolder iHolder, @NonNull ChatRoom iChatRoom) {
        BiFunction<String, String, String> lastMessageContentFormatter = (iSenderDisplayName, iContent) -> iSenderDisplayName + ": " + iContent;
        String thisUserUid = CurrentlyLoggedUser.getCurrentlyLoggedUser().getUid();
        String lastMessageContent = iChatRoom.getLastMessage().getContent().getData(); // for now, we assume that the content is a text message
        String lastMessageSenderUid = iChatRoom.getLastMessage().getSender().getUid();
        String lastMessageSenderDisplayName = null;

        if (thisUserUid.equals(lastMessageSenderUid)) {
            lastMessageSenderDisplayName = "You";
        } else {
            lastMessageSenderDisplayName = iChatRoom.getLastMessage().getSender().getDisplayName();
        }

        iHolder.CHAT_ROOM_LAST_MESSAGE_CONTENT.setText(lastMessageContentFormatter.apply(lastMessageSenderDisplayName, lastMessageContent));
    }
}
