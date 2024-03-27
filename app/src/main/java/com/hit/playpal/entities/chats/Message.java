package com.hit.playpal.entities.chats;

import com.hit.playpal.entities.chats.enums.ContentType;

import java.util.Date;

public class Message {
    private String mSenderUid;
    private String mSenderDisplayName;
    private String mSenderProfileName;
    private String mChatRoomId;
    private String mContent;
    private ContentType mContentType;
    private Date mSentAt;

    public Message() {
    }

    public Message(String iSenderUid, String iSenderDisplayName, String iSenderProfileName, String iChatRoomId, String iContent, ContentType iContentType, Date iSentAt) {
        this.mSenderUid = iSenderUid;
        this.mSenderDisplayName = iSenderDisplayName;
        this.mSenderProfileName = iSenderProfileName;
        this.mChatRoomId = iChatRoomId;
        this.mContent = iContent;
        this.mContentType = iContentType;
        this.mSentAt = iSentAt;
    }

    public String getSenderUid() {
        return mSenderUid;
    }

    public void setSenderUid(String iSenderUid) {
        this.mSenderUid = iSenderUid;
    }

    public String getSenderDisplayName() {
        return mSenderDisplayName;
    }

    public void setSenderDisplayName(String iSenderDisplayName) {
        this.mSenderDisplayName = iSenderDisplayName;
    }

    public String getSenderProfileName() {
        return mSenderProfileName;
    }

    public void setSenderProfileName(String iSenderProfileName) {
        this.mSenderProfileName = iSenderProfileName;
    }

    public String getChatRoomId() {
        return mChatRoomId;
    }

    public void setChatRoomId(String iChatRoomId) {
        this.mChatRoomId = iChatRoomId;
    }

    public String getContent() {
        return mContent;
    }

    public void setContent(String iContent) {
        this.mContent = iContent;
    }

    public ContentType getContentType() {
        return mContentType;
    }

    public void setContentType(ContentType iContentType) {
        this.mContentType = iContentType;
    }

    public Date getSentAt() {
        return mSentAt;
    }

    public void setSentAt(Date iSentAt) {
        this.mSentAt = iSentAt;
    }
}
