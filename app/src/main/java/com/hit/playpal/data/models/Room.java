package com.hit.playpal.data.models;

import com.google.firebase.firestore.PropertyName;

import java.util.Date;

public class Room {
    @PropertyName("room_id")
    private String mRoomId;

    @PropertyName("room_name")
    private String mRoomName;

    @PropertyName("room_image")
    private String mRoomImage;

    @PropertyName("room_description")
    private String mRoomDescription;

    @PropertyName("created_at")
    private Date mCreatedAt;

    public Room() {}

    public Room(
            String iRoomId,
            String iRoomName,
            String iRoomImage,
            String iRoomDescription,
            Date iCreatedAt
    ) {
        mRoomId = iRoomId;
        mRoomName = iRoomName;
        mRoomImage = iRoomImage;
        mRoomDescription = iRoomDescription;
        mCreatedAt = iCreatedAt;
    }

    @PropertyName("room_id") public String getRoomId() {
        return mRoomId;
    }
    @PropertyName("room_id") public void setRoomId(String iRoomId) {
        mRoomId = iRoomId;
    }

    @PropertyName("room_name") public String getRoomName() {
        return mRoomName;
    }
    @PropertyName("room_name") public void setRoomName(String iRoomName) {
        mRoomName = iRoomName;
    }

    @PropertyName("room_image") public String getRoomImage() {
        return mRoomImage;
    }
    @PropertyName("room_image") public void setRoomImage(String iRoomImage) {
        mRoomImage = iRoomImage;
    }

    @PropertyName("room_description") public String getRoomDescription() {
        return mRoomDescription;
    }
    @PropertyName("room_description") public void setRoomDescription(String iRoomDescription) {
        mRoomDescription = iRoomDescription;
    }

    @PropertyName("created_at") public Date getCreatedAt() {
        return mCreatedAt;
    }
    @PropertyName("created_at") public void setCreatedAt(Date iCreatedAt) {
        mCreatedAt = iCreatedAt;
    }
}
