package com.hit.playpal.entities.chats;

import android.os.Parcel;
import android.os.Parcelable;

import androidx.annotation.NonNull;

import com.google.firebase.firestore.DocumentId;
import com.google.firebase.firestore.PropertyName;
import com.hit.playpal.entities.chats.enums.ContentType;
import com.hit.playpal.entities.users.User;

import org.jetbrains.annotations.Contract;

import java.time.LocalDateTime;
import java.util.Date;
import java.util.Objects;

public class Message implements Parcelable {
    @DocumentId private String mId;
    @DocumentId public String getId() { return mId; }
    @DocumentId public void setId(String iMessageId) { mId = iMessageId; }

    @PropertyName("chat_room_id") private String mChatRoomId;
    @PropertyName("chat_room_id") public String getChatRoomId() { return mChatRoomId; }
    @PropertyName("chat_room_id") public void setChatRoomId(String iChatRoomId) { mChatRoomId = iChatRoomId; }

    @PropertyName("sender") private Sender mSender;
    @PropertyName("sender") public Sender getSender() { return mSender; }
    @PropertyName("sender") public void setSender(Sender iSender) { mSender = iSender; }

    @PropertyName("content") private Content mContent;
    @PropertyName("content") public Content getContent() { return mContent; }
    @PropertyName("content") public void setContent(Content iContent) { mContent = iContent; }

    @PropertyName("sent_at") private Date mSentAt;
    @PropertyName("sent_at") public Date getSentAt() { return mSentAt; }
    @PropertyName("sent_at") public void setSentAt(Date iSentAt) { mSentAt = iSentAt; }

    public Message() { }
    public Message(String iMessageId, String iChatRoomId, Sender iSender, Content iContent, Date iSentAt) {
        mId = iMessageId;
        mChatRoomId = iChatRoomId;
        mSender = iSender;
        mContent = iContent;
        mSentAt = iSentAt;
    }
    public Message(String iChatRoomId, @NonNull User iSender, String iTextMessage) {
        mChatRoomId = iChatRoomId;
        mSender = new Sender(iSender.getUid(), iSender.getDisplayName(), iSender.getProfilePicture());
        mContent = new Content(iTextMessage, ContentType.TEXT);
        mSentAt = new Date();
        //mSentAt = Date.from(LocalDateTime.now().toInstant(java.time.ZoneOffset.UTC)); ???
    }

    protected Message(@NonNull Parcel iIn) {
        mId = iIn.readString();
        mChatRoomId = iIn.readString();
        mSender = iIn.readParcelable(Sender.class.getClassLoader());
        mContent = iIn.readParcelable(Content.class.getClassLoader());
        mSentAt = new Date(iIn.readLong());
    }

    @Override
    public void writeToParcel(@NonNull Parcel iDest, int iFlags) {
        iDest.writeString(mId);
        iDest.writeString(mChatRoomId);
        iDest.writeParcelable(mSender, iFlags);
        iDest.writeParcelable(mContent, iFlags);
        iDest.writeLong(mSentAt.getTime());
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public static final Creator<Message> CREATOR = new Creator<Message>() {
        @NonNull
        @Contract("_ -> new")
        @Override
        public Message createFromParcel(Parcel in) {
            return new Message(in);
        }

        @NonNull
        @Contract(value = "_ -> new", pure = true)
        @Override
        public Message[] newArray(int size) {
            return new Message[size];
        }
    };

    @Override
    public boolean equals(Object iObject) {
        if (this == iObject) return true;
        if (iObject == null || getClass() != iObject.getClass()) return false;

        Message message = (Message) iObject;

        if (!Objects.equals(mId, message.mId)) return false;
        if (!Objects.equals(mChatRoomId, message.mChatRoomId)) return false;
        if (!Objects.equals(mSender, message.mSender)) return false;
        if (!Objects.equals(mContent, message.mContent)) return false;
        return Objects.equals(mSentAt, message.mSentAt);
    }

    public static class Sender implements Parcelable {
        @PropertyName("uid") private String mUid;
        @PropertyName("uid") public String getUid() { return mUid; }
        @PropertyName("uid") public void setUid(String iUid) { mUid = iUid; }

        @PropertyName("display_name") private String mDisplayName;
        @PropertyName("display_name") public String getDisplayName() { return mDisplayName; }
        @PropertyName("display_name") public void setDisplayName(String iDisplayName) { mDisplayName = iDisplayName; }

        @PropertyName("profile_picture") private String mProfilePicture;
        @PropertyName("profile_picture") public String getProfilePicture() { return mProfilePicture; }
        @PropertyName("profile_picture") public void setProfilePicture(String iProfilePicture) { mProfilePicture = iProfilePicture; }

        public Sender() { }
        public Sender(String iUid, String iDisplayName, String iProfilePicture) {
            mUid = iUid;
            mDisplayName = iDisplayName;
            mProfilePicture = iProfilePicture;
        }

        protected Sender(@NonNull Parcel iIn) {
            mUid = iIn.readString();
            mDisplayName = iIn.readString();
            mProfilePicture = iIn.readString();
        }

        @Override
        public void writeToParcel(@NonNull Parcel iDest, int iFlags) {
            iDest.writeString(mUid);
            iDest.writeString(mDisplayName);
            iDest.writeString(mProfilePicture);
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Sender> CREATOR = new Creator<Sender>() {
            @NonNull
            @Contract("_ -> new")
            @Override
            public Sender createFromParcel(Parcel in) {
                return new Sender(in);
            }

            @NonNull
            @Contract(value = "_ -> new", pure = true)
            @Override
            public Sender[] newArray(int size) {
                return new Sender[size];
            }
        };

        @Override
        public boolean equals(Object iObject) {
            if (this == iObject) return true;
            if (iObject == null || getClass() != iObject.getClass()) return false;

            Sender sender = (Sender) iObject;

            if (!Objects.equals(mUid, sender.mUid)) return false;
            if (!Objects.equals(mDisplayName, sender.mDisplayName)) return false;
            return Objects.equals(mProfilePicture, sender.mProfilePicture);
        }
    }

    public static class Content implements Parcelable {
        @PropertyName("data") private String mData;
        @PropertyName("data") public String getData() { return mData; }
        @PropertyName("data") public void setData(String iData) { mData = iData; }

        @PropertyName("type") private ContentType mType;
        @PropertyName("type") public ContentType getType() { return mType; }
        @PropertyName("type") public void setType(ContentType iType) { mType = iType; }

        public Content() { }
        public Content(String iData, ContentType iType) {
            mData = iData;
            mType = iType;
        }

        protected Content(@NonNull Parcel iIn) {
            mData = iIn.readString();
            mType = ContentType.valueOf(iIn.readString());
        }

        @Override
        public void writeToParcel(@NonNull Parcel iDest, int iFlags) {
            iDest.writeString(mData);
            iDest.writeString(mType.name());
        }

        @Override
        public int describeContents() {
            return 0;
        }

        public static final Creator<Content> CREATOR = new Creator<Content>() {
            @NonNull
            @Contract("_ -> new")
            @Override
            public Content createFromParcel(Parcel in) {
                return new Content(in);
            }

            @NonNull
            @Contract(value = "_ -> new", pure = true)
            @Override
            public Content[] newArray(int size) {
                return new Content[size];
            }
        };

        @Override
        public boolean equals(Object iObject) {
            if (this == iObject) return true;
            if (iObject == null || getClass() != iObject.getClass()) return false;

            Content content = (Content) iObject;

            if (!Objects.equals(mData, content.mData)) return false;
            return mType == content.mType;
        }
    }
}
