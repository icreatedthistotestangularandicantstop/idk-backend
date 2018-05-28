package app.core.repos;

import app.core.DB;
import app.core.repos.intefaces.LikeRepositoryInterface;
import app.pojo.Like;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;

@Component
public class LikeRepository extends BaseRepository implements LikeRepositoryInterface {
    @Autowired
    private DB db;

    @Override
    public Like addCommentLike(int commentId, int userId) {
        final String sql = "INSERT INTO `comment_likes` (`comment_id`, `user_id`, `liked_at`) VALUES " +
                "(:commentId, :userId, :likedAt)";
        final int likedAt = (int) System.currentTimeMillis() / 1000;
        final Map<String, Integer> params = new HashMap<>();
        params.put("commentId", commentId);
        params.put("userId", userId);
        params.put("likedAt", likedAt);

        KeyHolder holder = new GeneratedKeyHolder();
        db.getJdbcTemplate().update(sql, new MapSqlParameterSource(params), holder);

        final int likeId = Integer.parseInt(holder.getKeys().get("GENERATED_KEY").toString());
        final Like like = Like.createCommentLike(commentId, userId);
        like.setLikedAt(likedAt);
        like.setId(likeId);

        return like;
    }

    @Override
    public Like addUpdateLike(int commentId, int userId) {
        final String sql = "INSERT INTO `update_likes` (`update_id`, `user_id`, `liked_at`) VALUES " +
                "(:commentId, :userId, :likedAt)";
        final int likedAt = (int) System.currentTimeMillis() / 1000;
        final Map<String, Integer> params = new HashMap<>();
        params.put("commentId", commentId);
        params.put("userId", userId);
        params.put("likedAt", likedAt);

        KeyHolder holder = new GeneratedKeyHolder();
        db.getJdbcTemplate().update(sql, new MapSqlParameterSource(params), holder);

        final int likeId = Integer.parseInt(holder.getKeys().get("GENERATED_KEY").toString());
        final Like like = Like.createUpdateLike(commentId, userId);
        like.setLikedAt(likedAt);
        like.setId(likeId);

        return like;
    }

    @Override
    public Like findCommentLikeByUserId(int commentId, int userId) {
        final String sql = "SELECT * FROM `comment_likes` WHERE `comment_id` = :commentId AND `user_id` = :userId LIMIT 1";
        final Map<String, Integer> params = new HashMap<>();
        params.put("commentId", commentId);
        params.put("userId", userId);

        try {
            final Like result = db.getJdbcTemplate().queryForObject(
                    sql,
                    new MapSqlParameterSource(params),
                    getCommentLikeMapper()
            );

            return result;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public Like findUpdateLikeByUserId(int updateId, int userId) {
        final String sql = "SELECT * FROM `update_likes` WHERE `update_id` = :updateId AND `user_id` = :userId LIMIT 1";
        final Map<String, Integer> params = new HashMap<>();
        params.put("updateId", updateId);
        params.put("userId", userId);

        try {
            final Like result = db.getJdbcTemplate().queryForObject(
                    sql,
                    new MapSqlParameterSource(params),
                    getUpdateLikeMapper()
            );

            return result;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Like> findUpdateLikesByIds(Set<Integer> updateIds, int userId) {
        if (updateIds.isEmpty()) {
            return getEmptyList(Like.class);
        }
        final String sql = "SELECT * FROM `update_likes` WHERE `update_id` IN (:updateIds) AND `user_id` = :userId";
        try {
            final List<Like> result = db.query(
                    sql,
                    new MapSqlParameterSource("updateIds", updateIds)
                        .addValue("userId", userId),
                    getUpdateLikeMapper()
            );

            return result;
        } catch (Exception e) {
            return getEmptyList(Like.class);
        }
    }

    @Override
    public List<Like> findCommentLikesByIds(Set<Integer> commentIds, int userId) {
        if (commentIds.isEmpty()) {
            return getEmptyList(Like.class);
        }
        final String sql = "SELECT * FROM `comment_likes` WHERE `comment_id` IN (:commentIds) AND `user_id` = :userId";
        try {
            final List<Like> result = db.query(
                    sql,
                    new MapSqlParameterSource("commentIds", commentIds)
                            .addValue("userId", userId),
                    getCommentLikeMapper()
            );

            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return getEmptyList(Like.class);
        }
    }

    @Override
    public void deleteCommentLike(int commentId, int userId) {
        final String sql = "DELETE FROM `comment_likes` WHERE `comment_id` = :commentId AND `user_id` = :userId";
        final Map<String, Integer> params = new HashMap<>();
        params.put("commentId", commentId);
        params.put("userId", userId);

        db.getJdbcTemplate().update(sql, new MapSqlParameterSource(params));
    }

    @Override
    public void deleteUpdateLike(int updateId, int userId) {
        final String sql = "DELETE FROM `update_likes` WHERE `update_id` = :updateId AND `user_id` = :userId";
        final Map<String, Integer> params = new HashMap<>();
        params.put("updateId", updateId);
        params.put("userId", userId);

        db.getJdbcTemplate().update(sql, new MapSqlParameterSource(params));
    }

    private RowMapper<Like> getUpdateLikeMapper() {
        return (ResultSet rs, int rowNum) -> {
            Like like = Like.createUpdateLike(rs.getInt("update_id"), rs.getInt("user_id"));
            like.setId(rs.getInt("id"));
            like.setLikedAt(rs.getInt("liked_at"));

            return like;
        };
    }

    private RowMapper<Like> getCommentLikeMapper() {
        return (ResultSet rs, int rowNum) -> {
            Like like = Like.createCommentLike(rs.getInt("comment_id"), rs.getInt("user_id"));
            like.setId(rs.getInt("id"));
            like.setLikedAt(rs.getInt("liked_at"));

            return like;
        };
    }

}
