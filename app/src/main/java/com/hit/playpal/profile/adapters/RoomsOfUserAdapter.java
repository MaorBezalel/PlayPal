package com.hit.playpal.profile.adapters;

import androidx.lifecycle.LifecycleOwner;

import com.google.firebase.firestore.FirebaseFirestore;
import com.hit.playpal.entities.chats.ChatRoom;
import com.hit.playpal.paginatedsearch.rooms.utils.IBindableRoom;
import com.hit.playpal.paginatedsearch.rooms.adapters.IRoomAdapter;
import com.hit.playpal.paginatedsearch.rooms.adapters.RoomAdapter;
import com.hit.playpal.entities.chats.AllChatRoom;
import com.hit.playpal.entities.chats.enums.ChatRoomType;

public class RoomsOfUserAdapter extends RoomAdapter<AllChatRoom> {
    public RoomsOfUserAdapter(IRoomAdapter<AllChatRoom> iRoomAdapter, LifecycleOwner iOwner, String iUid) {
        super(iRoomAdapter, new IBindableRoom<AllChatRoom>() {
            @Override
            public String getRoomName(AllChatRoom item) {
                return item.getName();
            }

            @Override
            public String getRoomId(AllChatRoom item) {
                return item.getId();
            }

            @Override
            public String getRoomImage(AllChatRoom item) {
                return item.getProfilePicture();
            }

            @Override
            public String getGameImage(AllChatRoom item) {
                return item.getGame().getBackgroundImage();
            }

            @Override
            public int getMembersCount(AllChatRoom item) {
                return item.getMembersUid() == null ? 0 : item.getMembersUid().size();
            }

            @Override
            public ChatRoom getChatRoom(AllChatRoom item) {
                return item.convertToChatRoom();
            }
        }, iOwner, AllChatRoom.class, FirebaseFirestore.getInstance().collection("chat_rooms").whereEqualTo("type", ChatRoomType.GROUP).whereArrayContains("members_uid", iUid));
    }
}
