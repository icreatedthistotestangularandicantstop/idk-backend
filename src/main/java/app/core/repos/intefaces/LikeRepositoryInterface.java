package app.core.repos.intefaces;

import app.pojo.Like;

import java.util.List;
import java.util.Set;

public interface LikeRepositoryInterface {
    Like addUpdateLike(int updateId, int userId);
    Like addCommentLike(int commentId, int userId);
    void deleteUpdateLike(int updateId, int userId);
    void deleteCommentLike(int commentId, int userId);
    Like findUpdateLikeByUserId(int updateId, int userId);
    Like findCommentLikeByUserId(int commentId, int userId);
    List<Like> findUpdateLikesByIds(Set<Integer> updateIds, int userId);
    List<Like> findCommentLikesByIds(Set<Integer> commentIds, int userId);

}
