package com.hit.playpal.entities.chats;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.PropertyName;
import com.hit.playpal.entities.chats.enums.ChatRoomType;
import com.hit.playpal.entities.messages.Message;

import java.util.ArrayList;
import java.util.List;

/**
 * Entity class that represents a chat room.
 */
public abstract class ChatRoom implements Parcelable {
    @DocumentId protected String mId;
    @DocumentId public String getId() { return mId; }
    @DocumentId public void setId(String iId) { mId = iId; }

    @PropertyName("type") protected ChatRoomType mType;
    @PropertyName("type") public ChatRoomType getType() { return mType; }
    @PropertyName("type") public void setType(ChatRoomType iType) { mType = iType; }

    @PropertyName("members_uid") protected List<String> mMembersUid;
    @PropertyName("members_uid") public List<String> getMembersUid() { return mMembersUid; }
    @PropertyName("members_uid") public void setMembersUid(List<String> iMembersUid) { mMembersUid = iMembersUid; }

    @PropertyName("last_message") protected Message mLastMessage;
    @PropertyName("last_message") public Message getLastMessage() { return mLastMessage; }
    @PropertyName("last_message") public void setLastMessage(Message iLastMessage) { mLastMessage = iLastMessage; }

    public ChatRoom() {
        mMembersUid = new ArrayList<>();
        mLastMessage = new Message();
    }
    public ChatRoom(String iId, ChatRoomType iType, List<String> iMembersUid, Message iLastMessage) {
        mId = iId;
        mType = iType;
        mMembersUid = iMembersUid;
        mLastMessage = iLastMessage;
    }

    protected ChatRoom(@NonNull Parcel iIn) {
        mId = iIn.readString();
        mType = ChatRoomType.valueOf(iIn.readString());

        mMembersUid = new ArrayList<>();
        iIn.readStringList(mMembersUid);

        mLastMessage = iIn.readParcelable(Message.class.getClassLoader());
    }

    @Override
    public void writeToParcel(@NonNull Parcel iDest, int iFlags) {
        iDest.writeString(mId);
        iDest.writeString(mType.name());
        Log.d("ChatRoom", "writeToParcel: mMembersUid = " + mMembersUid);
        iDest.writeStringList(mMembersUid);
        iDest.writeParcelable(mLastMessage, iFlags);
    }

    @Override
    public int describeContents() {
        return 0;
    }
}
