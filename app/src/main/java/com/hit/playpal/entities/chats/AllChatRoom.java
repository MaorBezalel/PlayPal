package com.hit.playpal.entities.chats;

import android.os.Parcel;
import android.os.Parcelable;
import android.util.Log;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.PropertyName;
import com.hit.playpal.entities.chats.enums.ChatRoomType;
import com.hit.playpal.entities.chats.group.GroupChatRoom;
import com.hit.playpal.entities.chats.o2o.OneToOneChatRoom;
import com.hit.playpal.entities.messages.Message;

import org.jetbrains.annotations.Contract;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Entity class contain all the data of a one-to-one chat room or a group chat room.
 * This class is used to display all the chat rooms of the user in the chat rooms
 * firestore paginated recycler view list, regardless of the chat room type.
 */
public class AllChatRoom implements Parcelable {

    // ---------------------------------------------ChatRoom Data----------------------------------------------------- //
    @DocumentId
    protected String mId;
    @DocumentId public String getId() { return mId; }
    @DocumentId public void setId(String iId) { mId = iId; }

    @PropertyName("type") protected ChatRoomType mType;
    @PropertyName("type") public ChatRoomType getType() { return mType; }
    @PropertyName("type") public void setType(ChatRoomType iType) { mType = iType; }

    @PropertyName("members_uid") protected List<String> mMembersUid;
    @PropertyName("members_uid") public List<String> getMembersUid() { return mMembersUid; }
    @PropertyName("members_uid") public void setMembersData(List<String> iMembersUid) { mMembersUid = iMembersUid; }

    @PropertyName("last_message") protected Message mLastMessage;
    @PropertyName("last_message") public Message getLastMessage() { return mLastMessage; }
    @PropertyName("last_message") public void setLastMessage(Message iLastMessage) { mLastMessage = iLastMessage; }
    // ---------------------------------------------ChatRoom Data----------------------------------------------------- //


    // ---------------------------------------------OneToOneChatRoom Data----------------------------------------------------- //
    @PropertyName("other_member_data") private HashMap<String, OneToOneChatRoom.OtherMemberData> mOtherMemberData;
    @PropertyName("other_member_data") public HashMap<String, OneToOneChatRoom.OtherMemberData> getOtherMemberBasicData() { return mOtherMemberData; }
    @PropertyName("other_member_data") public void setOtherMemberBasicData(HashMap<String, OneToOneChatRoom.OtherMemberData> iOtherMemberData) { mOtherMemberData = iOtherMemberData; }
    // ---------------------------------------------OneToOneChatRoom Data----------------------------------------------------- //


    // ---------------------------------------------GroupChatRoom Data----------------------------------------------------- //
    @PropertyName("name") private String mName;
    @PropertyName("name") public String getName() { return mName; }
    @PropertyName("name") public void setName(String iName) { mName = iName; }

    @PropertyName("profile_picture") private String mProfilePicture;
    @PropertyName("profile_picture") public String getProfilePicture() { return mProfilePicture; }
    @PropertyName("profile_picture") public void setProfilePicture(String iProfilePicture) { mProfilePicture = iProfilePicture; }

    @PropertyName("description") private String mDescription;
    @PropertyName("description") public String getDescription() { return mDescription; }
    @PropertyName("description") public void setDescription(String iDescription) { mDescription = iDescription; }

    @PropertyName("game") private GroupChatRoom.Game mGame;
    @PropertyName("game") public GroupChatRoom.Game getGame() { return mGame; }
    @PropertyName("game") public void setGame(GroupChatRoom.Game iGame) { mGame = iGame; }
    // ---------------------------------------------GroupChatRoom Data----------------------------------------------------- //

    public AllChatRoom() { }
    public AllChatRoom(
            String iId,
            ChatRoomType iType,
            List<String> iMembersUid,
            Message iLastMessage,
            HashMap<String, OneToOneChatRoom.OtherMemberData> iOtherMemberData,
            String iName,
            String iProfilePicture,
            String iDescription,
            GroupChatRoom.Game iGame
    ) {
        mId = iId;
        mType = iType;
        mMembersUid = iMembersUid;
        mLastMessage = iLastMessage;
        mOtherMemberData = iOtherMemberData;
        mName = iName;
        mProfilePicture = iProfilePicture;
        mDescription = iDescription;
        mGame = iGame;
    }

    protected AllChatRoom(@NonNull Parcel iIn) {
        mId = iIn.readString();
        mType = ChatRoomType.valueOf(iIn.readString());
        mLastMessage = iIn.readParcelable(Message.class.getClassLoader());

        mMembersUid = new ArrayList<>();
        iIn.readStringList(mMembersUid);

        mOtherMemberData = new HashMap<>();
        iIn.readMap(mOtherMemberData, OneToOneChatRoom.OtherMemberData.class.getClassLoader());

        mName = iIn.readString();
        mProfilePicture = iIn.readString();
        mDescription = iIn.readString();
        mGame = iIn.readParcelable(GroupChatRoom.Game.class.getClassLoader());
    }

    /**
     * Convert this AllChatRoom object to a ChatRoom object (either OneToOneChatRoom or GroupChatRoom).
     * @return A polymorphic ChatRoom object.
     */
    public ChatRoom convertToChatRoom() {
        if (mType == ChatRoomType.ONE_TO_ONE) {
            return new OneToOneChatRoom(mId, mMembersUid, mLastMessage, mOtherMemberData);
        } else if (mType == ChatRoomType.GROUP) {
            return new GroupChatRoom(mId, mMembersUid, mLastMessage, mName, mProfilePicture, mDescription, mGame);
        } else {
            Log.e("AllChatRoom", "Unknown chat room type: " + mType);
            return null;
        }
    }

    @Override
    public void writeToParcel(@NonNull Parcel iDest, int iFlags) {
        iDest.writeString(mId);
        iDest.writeString(mType.name());
        iDest.writeStringList(mMembersUid);
        iDest.writeParcelable(mLastMessage, iFlags);
        iDest.writeMap(mOtherMemberData);
        iDest.writeString(mName);
        iDest.writeString(mProfilePicture);
        iDest.writeString(mDescription);
        iDest.writeParcelable(mGame, iFlags);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Parcelable.Creator<AllChatRoom> CREATOR = new Parcelable.Creator<AllChatRoom>() {
        @NonNull
        @Contract("_ -> new")
        @Override
        public AllChatRoom createFromParcel(Parcel iIn) {
            return new AllChatRoom(iIn);
        }

        @NonNull
        @Contract(value = "_ -> new", pure = true)
        @Override
        public AllChatRoom[] newArray(int iSize) {
            return new AllChatRoom[iSize];
        }
    };
}
