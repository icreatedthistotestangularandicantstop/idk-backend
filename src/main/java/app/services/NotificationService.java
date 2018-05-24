package app.services;

import app.core.repos.NotificationRepository;
import app.pojo.Notification;
import app.websocket.notification.NotificationHandler;
import org.springframework.stereotype.Service;

@SuppressWarnings("unused")
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationHandler notificationHandler;

    NotificationService(
            final NotificationRepository notificationRepository,
            final NotificationHandler notificationHandler
    ) {
        this.notificationRepository = notificationRepository;
        this.notificationHandler = notificationHandler;
    }

    public void sendNotification(final int toUserId, final Notification notification) {
        notificationRepository.add(notification);
        notificationHandler.sendToUser(toUserId, notification);
    }

}
