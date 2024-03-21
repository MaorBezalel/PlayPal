package com.hit.playpal.data.models;

import com.google.firebase.firestore.PropertyName;

import java.util.Date;

public class Message {

    @PropertyName("sender_uid")
    private String mSenderUid;

    @PropertyName("send_username")
    private String mSendUsername;

    @PropertyName("sender_profile_image_url")
    private String mSenderProfileImageUrl;

    @PropertyName("message_text")
    private String mMessageText;

    @PropertyName("sent_at")
    private Date mSentAt;

    public Message() {}

    public Message(String iSenderUid, String iSendUsername, String iSenderProfileImageUrl, String iMessageText, Date iSentAt) {
        mSenderUid = iSenderUid;
        mSendUsername = iSendUsername;
        mSenderProfileImageUrl = iSenderProfileImageUrl;
        mMessageText = iMessageText;
        mSentAt = iSentAt;
    }

    @PropertyName("sender_uid") public String getSenderUid() {
        return mSenderUid;
    }
    @PropertyName("sender_uid") public void setSenderUid(String iSenderUid) {
        mSenderUid = iSenderUid;
    }

    @PropertyName("send_username") public String getSendUsername() {
        return mSendUsername;
    }
    @PropertyName("send_username") public void setSendUsername(String iSendUsername) {
        mSendUsername = iSendUsername;
    }

    @PropertyName("sender_profile_image_url") public String getSenderProfileImageUrl() {
        return mSenderProfileImageUrl;
    }
    @PropertyName("sender_profile_image_url") public void setSenderProfileImageUrl(String iSenderProfileImageUrl) {
        mSenderProfileImageUrl = iSenderProfileImageUrl;
    }

    @PropertyName("message_text") public String getMessageText() {
        return mMessageText;
    }
    @PropertyName("message_text") public void setMessageText(String iMessageText) {
        mMessageText = iMessageText;
    }

    @PropertyName("sent_at") public Date getSentAt() {
        return mSentAt;
    }
    @PropertyName("sent_at") public void setSentAt(Date iSentAt) {
        mSentAt = iSentAt;
    }
}
