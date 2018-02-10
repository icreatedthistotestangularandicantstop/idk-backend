package app.core.repos.intefaces;

import app.http.pojos.Page;
import app.pojo.Comment;

import java.util.List;

public interface CommentRepositoryInterface {
    int add(Comment update);
    Comment findById(int id);
    List<Comment> findByUpdateId(int updateId);
    List<Comment> findByUpdateIdPaged(int updateId, Page page);
    void incrementCommentLikes(int commentId);
    void decrementCommentLikes(int commentId);
    boolean exists(int id);
}
