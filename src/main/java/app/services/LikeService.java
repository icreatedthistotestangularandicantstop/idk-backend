package app.services;

import app.core.repos.CommentRepository;
import app.core.repos.LikeRepository;
import app.core.repos.UpdateRepository;
import app.http.pojos.UpdateResource;
import app.pojo.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

@Component
public class LikeService {
    @Autowired
    private LikeRepository likeRepository;

    @Autowired
    private UpdateRepository updateRepository;

    @Autowired
    private CommentRepository commentRepository;

    private final NotificationService notificationService;

    LikeService(final NotificationService notificationService) {
        this.notificationService = notificationService;
    }

    @Transactional
    public boolean likeUpdate(final int updateId, final int userId) {
        final boolean liked = addUpdateLike(updateId, userId);
        if (liked) {
            incrementUpdateLikes(updateId);
            this.sendUpdateNotification(updateId, userId);

            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public boolean unlikeUpdate(int updateId, int userId) {
        final boolean unliked = removeUpdateLike(updateId, userId);
        if (unliked) {
            decrementUpdateLikes(updateId);

            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public boolean likeComment(int commentId, int userId) {
        final boolean liked = addCommentLike(commentId, userId);
        if (liked) {
            incrementCommentLikes(commentId);
            sendCommentNotification(commentId, userId);

            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public boolean unlikeComment(int commentId, int userId) {
        final boolean unliked = removeCommentLike(commentId, userId);
        if (unliked) {
            decrementCommentLikes(commentId);

            return true;
        } else {
            return false;
        }
    }

    private void incrementUpdateLikes(int updateId) {
        updateRepository.incrementUpdateLikes(updateId);
    }

    private void decrementUpdateLikes(int updateId) {
        updateRepository.decrementUpdateLikes(updateId);
    }


    private void incrementCommentLikes(int commentId) {
        commentRepository.incrementCommentLikes(commentId);
    }

    private void decrementCommentLikes(int commentId) {
        commentRepository.decrementCommentLikes(commentId);
    }

    private boolean addUpdateLike(int updateId, int userId) {
        if (!updateRepository.exists(updateId) || hasUserLikedUpdate(updateId, userId)) {
            return false;
        }
        addUpdateLikeLink(updateId, userId);

        return true;
    }

    private boolean addCommentLike(int commentId, int userId) {
        if (!commentRepository.exists(commentId) || hasUserLikedComment(commentId, userId)) {
            return false;
        }
        addCommentLikeLink(commentId, userId);

        return true;
    }

    private boolean removeUpdateLike(int updateId, int userId) {
        if (!updateRepository.exists(updateId) || !hasUserLikedUpdate(updateId, userId)) {
            return false;
        }
        likeRepository.deleteUpdateLike(updateId, userId);

        return true;
    }

    private boolean removeCommentLike(int commentId, int userId) {
        if (!commentRepository.exists(commentId) || !hasUserLikedComment(commentId, userId)) {
            return false;
        }
        likeRepository.deleteCommentLike(commentId, userId);

        return true;
    }

    public boolean hasUserLikedUpdate(int updateId, int userId) {
        final Like like = likeRepository.findUpdateLikeByUserId(updateId, userId);
        if (like == null) {
            return false;
        }

        return true;
    }

    public boolean hasUserLikedComment(int commentId, int userId) {
        final Like like = likeRepository.findCommentLikeByUserId(commentId, userId);
        if (like == null) {
            return false;
        }

        return true;
    }

    private void addUpdateLikeLink(int updateId, int userId) {
        likeRepository.addUpdateLike(updateId, userId);
    }

    private void addCommentLikeLink(int updateId, int userId) {
        likeRepository.addCommentLike(updateId, userId);
    }

    private void sendUpdateNotification(final int updateId, final int userId) {
        final Update update = this.updateRepository.findById(updateId);
        final int updateOwnerId = update.getUserId();

        final Notification notification = Notification.createForUpdate(update, userId);
        notification.setType(NotificationType.UPDATE_LIKE);

        if (updateOwnerId != userId) {
            this.notificationService.sendNotification(updateOwnerId, notification);
        }
    }

    private void sendCommentNotification(final int commentId, final int userId) {
        final Comment comment = this.commentRepository.findById(commentId);
        final int commentOwnerId = comment.getUserId();

        final Notification notification = Notification.createForComment(comment, userId);
        notification.setType(NotificationType.COMMENT_LIKE);

        if (commentOwnerId != userId) {
            this.notificationService.sendNotification(commentOwnerId, notification);
        }
    }

}
