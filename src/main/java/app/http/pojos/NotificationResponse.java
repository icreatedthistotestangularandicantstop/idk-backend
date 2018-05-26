package app.http.pojos;

import app.pojo.Notification;
import app.pojo.NotificationType;
import app.pojo.User;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class NotificationResponse {
    private int id;
    private int toUserId;
    private int fromUserId;
    private int relId;
    private boolean seen;
    private NotificationType type;
    private long createdAt;
    private User from;

    public static NotificationResponse createFromNotification(final Notification target) {
        final NotificationResponse notification = new NotificationResponse();
        notification.setId(target.getId());
        notification.setToUserId(target.getToUserId());
        notification.setFromUserId(target.getFromUserId());
        notification.setType(target.getType());
        notification.setRelId(target.getRelId());
        notification.setSeen(target.isSeen());

        return notification;
    }

}
