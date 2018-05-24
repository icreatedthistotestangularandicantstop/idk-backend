package app.pojo;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class Notification {
    private int id;
    private int toUserId;
    private int fromUserId;
    private int relId;
    private boolean seen;
    private NotificationType type;
    private long createdAt;

    public String getRelType() {
        return null != type ? type.name() : "";
    }

    public static Notification createForUpdate(final Update update, final int fromUserId) {
        final Notification notification = new Notification();
        notification.setFromUserId(fromUserId);
        notification.setToUserId(update.getUserId());
        notification.setRelId(update.getId());
        notification.setSeen(false);

        return notification;
    }

    public static Notification createForComment(final Comment comment, final int fromUserId) {
        final Notification notification = new Notification();
        notification.setFromUserId(fromUserId);
        notification.setToUserId(comment.getUserId());
        notification.setRelId(comment.getId());
        notification.setSeen(false);

        return notification;
    }

}
