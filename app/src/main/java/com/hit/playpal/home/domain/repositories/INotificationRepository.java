package com.hit.playpal.home.domain.repositories;

import java.util.List;

import com.hit.playpal.entities.users.Notification;

public interface INotificationRepository {
    List<Notification> getLatestUserNotifications(String iUsername);
}
