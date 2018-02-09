package app.services;

import app.core.repos.CommentRepository;
import app.core.repos.LikeRepository;
import app.core.repos.UpdateRepository;
import app.http.pojos.UpdateResource;
import app.pojo.Favorite;
import app.pojo.Like;
import app.pojo.Update;
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

    @Transactional
    public boolean likeUpdate(int updateId, int userId) {
        final boolean liked = addUpdateLike(updateId, userId);
        if (liked) {
            incrementUpdateLikes(updateId);

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

            return true;
        } else {
            return false;
        }
    }

    @Transactional
    public boolean unlikeComment(int commentId, int userId) {
        final boolean unliked = removeUpdateLike(commentId, userId);
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
        if (hasUserLikedUpdate(updateId, userId)) {
            return false;
        }
        addUpdateLikeLink(updateId, userId);

        return true;
    }

    private boolean addCommentLike(int commentId, int userId) {
        if (hasUserLikedComment(commentId, userId)) {
            return false;
        }
        addCommentLikeLink(commentId, userId);

        return true;
    }

    private boolean removeUpdateLike(int updateId, int userId) {
        if (!hasUserLikedUpdate(updateId, userId)) {
            return false;
        }
        likeRepository.deleteUpdateLike(updateId, userId);

        return true;
    }

    private boolean removeCommentLike(int commentId, int userId) {
        if (!hasUserLikedComment(commentId, userId)) {
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
}
