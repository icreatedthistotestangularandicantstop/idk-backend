package app.core.repos.intefaces;

import app.pojo.Comment;

import java.util.List;

public interface CommentRepositoryInterface {
    int add(Comment update);
    Comment findById(int id);
    List<Comment> findByUpdateId(int updateId);
    void incrementCommentLikes(int commentId);
    void decrementCommentLikes(int commentId);
}
