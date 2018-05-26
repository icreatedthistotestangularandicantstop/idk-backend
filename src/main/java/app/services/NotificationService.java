package app.services;

import app.core.repos.NotificationRepository;
import app.core.repos.UserRepository;
import app.http.pojos.NotificationInfoResponse;
import app.http.pojos.NotificationResponse;
import app.http.pojos.Page;
import app.pojo.Notification;
import app.pojo.NotificationType;
import app.pojo.User;
import app.websocket.notification.NotificationHandler;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

@SuppressWarnings("unused")
@Service
public class NotificationService {

    private final NotificationRepository notificationRepository;
    private final NotificationHandler notificationHandler;
    private final UserRepository userRepository;

    NotificationService(
            final NotificationRepository notificationRepository,
            final NotificationHandler notificationHandler,
            final UserRepository userRepository
    ) {
        this.notificationRepository = notificationRepository;
        this.notificationHandler = notificationHandler;
        this.userRepository = userRepository;
    }

    public void sendNotification(final int toUserId, final Notification notification) {
        notificationRepository.add(notification);
        notificationHandler.sendToUser(toUserId, notification);
    }

    public NotificationInfoResponse getInfo(final int userId) {
        return new NotificationInfoResponse(notificationRepository.getNotSeen(userId));
    }

    public void see(final int notificationId, final int userId) {
        notificationRepository.see(notificationId, userId);
    }

    public List<NotificationResponse> getPaged(final int userId, final Page page) {
        final List<Notification> notifications = notificationRepository.findPaged(userId, page);

        return buildResponse(notifications);
    }

    private List<NotificationResponse> buildResponse(final List<Notification> notifications) {
        final List<NotificationResponse> response = new ArrayList<>();
        final Map<Integer, User> users = getUsers(getUserIds(notifications));

        for (final Notification notification : notifications) {
            final NotificationResponse item = NotificationResponse.createFromNotification(notification);
            item.setFrom(users.get(item.getFromUserId()));

            response.add(item);
        }

        return response;
    }

    private Map<Integer, User> getUsers(final List<Integer> userIds) {
        final Map<Integer, User> response = new HashMap<>();
        final List<User> users = userRepository.findByIds(new HashSet<>(userIds));

        for (final User user : users) {
            response.put(user.getId(), user);
        }

        return response;
    }

    private List<Integer> getUserIds(final List<Notification> notifications) {
        final List<Integer> userIds = notifications.stream().map(notification -> notification.getFromUserId()).collect(Collectors.toList());

        return userIds;
    }

    private List<Integer> getUpdateIds(final List<Notification> notifications) {
        final List<Integer> updateIds = notifications.stream()
                .filter(notification -> notification.getType().equals(NotificationType.UPDATE_LIKE) || notification.getRelType().equals(NotificationType.UPDATE_COMMENT))
                .map(notification -> notification.getRelId()).collect(Collectors.toList());

        return updateIds;
    }

    private List<Integer> getCommentIds(final List<Notification> notifications) {
        final List<Integer> commentIds = notifications.stream()
                .filter(notification -> notification.getType().equals(NotificationType.COMMENT_LIKE))
                .map(notification -> notification.getRelId()).collect(Collectors.toList());

        return commentIds;
    }

}
