package com.hit.playpal.entities.notifications;

import com.google.firebase.firestore.PropertyName;
import com.hit.playpal.entities.notifications.enums.NotificationType;

import java.util.Date;

/**
 * Entity class that represents a notification.
 */
public class Notification {
    @PropertyName("type") private NotificationType mType;
    @PropertyName("type") public NotificationType getType() {
        return mType;
    }
    @PropertyName("type") public void setType(NotificationType iType) {
        mType = iType;
    }

    @PropertyName("happened_at") private Date mHappenedAt;
    @PropertyName("happened_at") public Date getHappenedAt() {
        return mHappenedAt;
    }
    @PropertyName("happened_at")  public void setHappenedAt(Date iHappenedAt) {
        mHappenedAt = iHappenedAt;
    }

    @PropertyName("sender_profile_image") private String mNotifierImage;
    @PropertyName("sender_profile_image") public String getNotifierImage() {
        return mNotifierImage;
    }
    @PropertyName("sender_profile_image") public void setNotifierImage(String iNotifierImage) {
        mNotifierImage = iNotifierImage;
    }
    
    @PropertyName("sender_display_name") private String mNotifierName;
    @PropertyName("sender_display_name") public String getNotifierName() {
        return mNotifierName;
    }
    @PropertyName("sender_display_name")  public void setNotifierName(String iNotifierName) {
        mNotifierName = iNotifierName;
    }

    @PropertyName("sender_uid") private String mNotifierUid;
    @PropertyName("sender_uid")  public String getNotifierUid() {
        return mNotifierUid;
    }
    @PropertyName("sender_uid") public void setNotifierUid(String iNotifierUid) {
        mNotifierUid = iNotifierUid;
    }

    public Notification() {
    }
    public Notification(NotificationType iType,Date iHappenedAt, String iNotifierImage) {
        mType = iType;
        mHappenedAt = iHappenedAt;
        mNotifierImage = iNotifierImage;
    }
}
