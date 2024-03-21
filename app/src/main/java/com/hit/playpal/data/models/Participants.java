package com.hit.playpal.data.models;

import com.google.firebase.firestore.PropertyName;
import com.hit.playpal.data.enums.RoomRole;

import java.util.Date;

public class Participants {
    @PropertyName("participant_uid")
    private String mParticipantUid;

    @PropertyName("participant_name")
    private String mParticipantName;

    @PropertyName("participant_image")
    private String mParticipantImage;

    @PropertyName("joined_at")
    private Date mJoinedAt;

    @PropertyName("role")
    private RoomRole mRole;

    public Participants() {}

    public Participants(
            String iParticipantUid,
            String iParticipantName,
            String iParticipantImage,
            Date iJoinedAt,
            RoomRole iRole
    ) {
        mParticipantUid = iParticipantUid;
        mParticipantName = iParticipantName;
        mParticipantImage = iParticipantImage;
        mJoinedAt = iJoinedAt;
        mRole = iRole;
    }

    @PropertyName("participant_uid") public String getParticipantUid() {
        return mParticipantUid;
    }
    @PropertyName("participant_uid") public void setParticipantUid(String iParticipantUid) {
        mParticipantUid = iParticipantUid;
    }

    @PropertyName("participant_name") public String getParticipantName() {
        return mParticipantName;
    }
    @PropertyName("participant_name") public void setParticipantName(String iParticipantName) {
        mParticipantName = iParticipantName;
    }

    @PropertyName("participant_image") public String getParticipantImage() {
        return mParticipantImage;
    }
    @PropertyName("participant_image") public void setParticipantImage(String iParticipantImage) {
        mParticipantImage = iParticipantImage;
    }

    @PropertyName("joined_at") public Date getJoinedAt() {
        return mJoinedAt;
    }
    @PropertyName("joined_at") public void setJoinedAt(Date iJoinedAt) {
        mJoinedAt = iJoinedAt;
    }

    @PropertyName("role") public RoomRole getRole() {
        return mRole;
    }
    @PropertyName("role") public void setRole(RoomRole iRole) {
        mRole = iRole;
    }
}
