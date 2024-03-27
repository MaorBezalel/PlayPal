package com.hit.playpal.entities.users;

import com.hit.playpal.entities.users.enums.NotificationType;

import java.util.Date;

public class Notification {
    private NotificationType mType;
    private String mNotificationBody;
    private Date mHappenedAt;
    private String mNotifierImage;

    public Notification() {
    }

    public Notification(NotificationType iType, String iNotificationBody, Date iHappenedAt, String iNotifierImage) {
        this.mType = iType;
        this.mNotificationBody = iNotificationBody;
        this.mHappenedAt = iHappenedAt;
        this.mNotifierImage = iNotifierImage;
    }

    public NotificationType getType() {
        return mType;
    }

    public void setType(NotificationType iType) {
        this.mType = iType;
    }

    public String getNotificationBody() {
        return mNotificationBody;
    }

    public void setNotificationBody(String iNotificationBody) {
        this.mNotificationBody = iNotificationBody;
    }

    public Date getHappenedAt() {
        return mHappenedAt;
    }

    public void setHappenedAt(Date iHappenedAt) {
        this.mHappenedAt = iHappenedAt;
    }

    public String getNotifierImage() {
        return mNotifierImage;
    }

    public void setNotifierImage(String iNotifierImage) {
        this.mNotifierImage = iNotifierImage;
    }

}
