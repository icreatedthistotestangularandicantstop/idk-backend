package app.services;

import app.core.repos.CommentRepository;
import app.core.repos.LikeRepository;
import app.core.repos.UpdateRepository;
import app.http.pojos.*;
import app.pojo.Comment;
import app.pojo.Like;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;

@Component
public class CommentService {
    @Autowired
    private CommentRepository commentRepository;

    @Autowired
    private UpdateRepository updateRepository;

    @Autowired
    private LikeRepository likeRepository;

    public List<CommentResponse> findByUpdateIdPaged(Page page, int updateId, Integer userId) {
        List<Comment> comments = commentRepository.findByUpdateIdPaged(updateId, page);
        Set<Integer> likedUpdateIds = getLikedComments(comments, userId);

        List<CommentResponse> response = new ArrayList<>();
        for (Comment comment: comments) {
            final CommentResponse item = CommentResponse.createFromComment(comment);
            item.setLiked(likedUpdateIds.contains(comment.getId()));
            response.add(item);
        }

        return response;
    }

    private Set<Integer> getLikedComments(List<Comment> comments, Integer userId) {
        List<Like> likes = Collections.EMPTY_LIST;
        if (userId != null) {
            likes = likeRepository.findCommentLikesByIds(getCommentIds(comments), userId);
        }
        Set<Integer> likedUpdateIds = new HashSet<>();
        for (Like like : likes) {
            likedUpdateIds.add(like.getItemId());
        }

        return likedUpdateIds;
    }

    private Set<Integer> getCommentIds(List<Comment> updates) {
        Set<Integer> result = new HashSet<>();
        for (Comment update : updates) {
            result.add(update.getId());
        }

        return result;
    }

    @Transactional
    public Comment addNew(CommentResource commentResource, int userId) {
        final Comment comment = new Comment();
        comment.setContent(commentResource.getContent());
        comment.setUpdateId(commentResource.getUpdateId());
        comment.setUserId(userId);

        int newCommentId = commentRepository.add(comment);
        comment.setId(newCommentId);

        updateRepository.incrementUpdateComments(commentResource.getUpdateId());

        return comment;
    }
}
