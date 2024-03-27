package com.hit.playpal.home.domain.repositories;

import java.util.List;

public interface INotificationRepository {
    List<Notification> getLatestUserNotifications(String iUsername);
}
