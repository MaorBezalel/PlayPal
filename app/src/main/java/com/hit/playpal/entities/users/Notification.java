package com.hit.playpal.entities.users;

import com.google.firebase.firestore.PropertyName;
import com.hit.playpal.entities.users.enums.NotificationType;

import java.util.Date;

public class Notification {
    @PropertyName("type") private NotificationType mType;
    @PropertyName("happened_at") private Date mHappenedAt;
    @PropertyName("sender_profile_image") private String mNotifierImage;
    @PropertyName("sender_display_name") private String mNotifierName;
    @PropertyName("sender_uid") private String mNotifierUid;

    public Notification() {
    }

    public Notification(NotificationType iType,Date iHappenedAt, String iNotifierImage) {
        this.mType = iType;
        this.mHappenedAt = iHappenedAt;
        this.mNotifierImage = iNotifierImage;
    }

    @PropertyName("type") public NotificationType getType() {
        return mType;
    }

    @PropertyName("type") public void setType(NotificationType iType) {
        this.mType = iType;
    }


    @PropertyName("happened_at") public Date getHappenedAt() {
        return mHappenedAt;
    }

    @PropertyName("happened_at")  public void setHappenedAt(Date iHappenedAt) {
        this.mHappenedAt = iHappenedAt;
    }

    @PropertyName("sender_profile_image") public String getNotifierImage() {
        return mNotifierImage;
    }

    @PropertyName("sender_profile_image") public void setNotifierImage(String iNotifierImage) {
        this.mNotifierImage = iNotifierImage;
    }

    @PropertyName("sender_display_name") public String getNotifierName() {
        return mNotifierName;
    }

    @PropertyName("sender_display_name")  public void setNotifierName(String iNotifierName) {
        this.mNotifierName = iNotifierName;
    }

    @PropertyName("sender_uid")  public String getNotifierUid() {
        return mNotifierUid;
    }

    @PropertyName("sender_uid") public void setNotifierUid(String iNotifierUid) {
        this.mNotifierUid = iNotifierUid;
    }

}
