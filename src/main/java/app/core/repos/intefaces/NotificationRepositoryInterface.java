package app.core.repos.intefaces;

import app.pojo.Notification;

public interface NotificationRepositoryInterface {
    int add(final Notification notification);
    void see(final int notificationId);
    int getNotSeen(final int userId);

}
