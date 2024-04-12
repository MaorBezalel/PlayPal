package com.hit.playpal.entities.chats;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.PropertyName;
import com.hit.playpal.entities.chats.enums.ChatRoomType;

import org.jetbrains.annotations.Contract;

import java.util.HashMap;
import java.util.List;

public class OneToOneChatRoom extends ChatRoom {
    @PropertyName("other_member_data") private HashMap<String, OtherMemberData> mOtherMemberData;
    @PropertyName("other_member_data") public HashMap<String, OtherMemberData> getOtherMemberBasicData() { return mOtherMemberData; }
    @PropertyName("other_member_data") public void setOtherMemberBasicData(HashMap<String, OtherMemberData> iOtherMemberData) { mOtherMemberData = iOtherMemberData; }

    public OneToOneChatRoom() {
        super();
        mType = ChatRoomType.ONE_TO_ONE;
    }
    public OneToOneChatRoom(String iChatRoomId, List<String> iMembersUid,  Message iLastMessage, HashMap<String, OtherMemberData> iOtherMemberData) {
       super(iChatRoomId, ChatRoomType.ONE_TO_ONE, iMembersUid, iLastMessage);
        mOtherMemberData = iOtherMemberData;
    }

    public String getOtherUserDisplayName(String iUserUid) {
        if (mOtherMemberData == null) return null;
        return mOtherMemberData.get(iUserUid).getDisplayName();
    }
    public String getOtherUserProfilePicture(String iUserUid) {
        if (mOtherMemberData == null) return null;
        return mOtherMemberData.get(iUserUid).getProfilePicture();
    }

    protected OneToOneChatRoom(Parcel iIn) {
        super(iIn);
        mOtherMemberData = new HashMap<>();
        iIn.readMap(mOtherMemberData, OtherMemberData.class.getClassLoader());
    }

    @Override
    public void writeToParcel(@NonNull Parcel iDest, int iFlags) {
        super.writeToParcel(iDest, iFlags);
        iDest.writeMap(mOtherMemberData);
    }

    public static final Creator<OneToOneChatRoom> CREATOR = new Creator<OneToOneChatRoom>() {
        @Override
        public OneToOneChatRoom createFromParcel(Parcel iIn) {
            return new OneToOneChatRoom(iIn);
        }

        @Override
        public OneToOneChatRoom[] newArray(int iSize) {
            return new OneToOneChatRoom[iSize];
        }
    };

    public static class OtherMemberData implements Parcelable {
        @PropertyName("display_name") private String mDisplayName;
        @PropertyName("display_name") public String getDisplayName() { return mDisplayName; }
        @PropertyName("display_name") public void setDisplayName(String iDisplayName) { mDisplayName = iDisplayName; }

        @PropertyName("profile_picture") private String mProfilePicture;
        @PropertyName("profile_picture") public String getProfilePicture() { return mProfilePicture; }
        @PropertyName("profile_picture") public void setProfilePicture(String iProfilePicture) { mProfilePicture = iProfilePicture; }

        public OtherMemberData() { }
        public OtherMemberData(String iDisplayName, String iProfilePicture) {
            mDisplayName = iDisplayName;
            mProfilePicture = iProfilePicture;
        }

        protected OtherMemberData(@NonNull Parcel iIn) {
            mDisplayName = iIn.readString();
            mProfilePicture = iIn.readString();
        }

        @Override
        public void writeToParcel(@NonNull Parcel iDest, int iFlags) {
            iDest.writeString(mDisplayName);
            iDest.writeString(mProfilePicture);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<OtherMemberData> CREATOR = new Creator<OtherMemberData>() {
            @NonNull
            @Contract("_ -> new")
            @Override
            public OtherMemberData createFromParcel(Parcel iIn) {
                return new OtherMemberData(iIn);
            }

            @NonNull
            @Contract(value = "_ -> new", pure = true)
            @Override
            public OtherMemberData[] newArray(int iSize) {
                return new OtherMemberData[iSize];
            }
        };
    }
}
