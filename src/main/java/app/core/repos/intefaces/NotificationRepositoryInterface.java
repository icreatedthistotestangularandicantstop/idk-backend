package app.core.repos.intefaces;

import app.http.pojos.Page;
import app.pojo.Notification;
import java.util.List;

public interface NotificationRepositoryInterface {
    int add(final Notification notification);
    void see(final int notificationId, final int userId);
    int getNotSeen(final int userId);
    List<Notification> findPaged(final int userId, final Page page);

}
