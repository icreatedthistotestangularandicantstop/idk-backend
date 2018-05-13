package app.core.repos;

import app.core.DB;
import app.core.repos.intefaces.CommentRepositoryInterface;
import app.http.pojos.Page;
import app.pojo.Comment;
import app.pojo.Update;
import app.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class CommentRepository extends BaseRepository implements CommentRepositoryInterface {
    public static final int PAGE_SIZE = 7;

    @Autowired
    private DB db;

    public int add(Comment comment) {
        final String sql = "INSERT INTO `comments` (`content`, `update_id`, `user_id`, `created_at`) VALUES " +
                "(:content, :updateId, :userId, :createdAt)";
        comment.setCreatedAt(System.currentTimeMillis() / 1000);

        KeyHolder holder = new GeneratedKeyHolder();
        db.getJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(comment), holder);

        return Integer.parseInt(holder.getKeys().get("GENERATED_KEY").toString());
    }

    @Override
    public Comment findById(int id) {
        final String sql = "SELECT * FROM `comments` WHERE `id` = :id LIMIT 1";
        try {
            final Comment result = db.getJdbcTemplate().queryForObject(
                    sql,
                    new MapSqlParameterSource("id", id),
                    getMapper()
            );

            return result;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Comment> findByUpdateId(int updateId) {
        final String sql = "SELECT `id`, `content`, `user_id`, `update_id` " +
                " FROM `comments` " +
                " WHERE `update_id` = :updateId " +
                " ORDER BY `created_at` DESC ";
        final List<Comment> comments = db.query(
                sql,
                new MapSqlParameterSource("updateId", updateId),
                getMapper()
        );

        return comments;
    }

    @Override
    public List<Comment> findByUpdateIdPaged(int updateId, Page page) {
        final String sql = "SELECT * " +
                " FROM `comments` " +
                " WHERE `update_id` = :updateId " +
                " ORDER BY `created_at` DESC " +
                " LIMIT :offset, :limit";
        final Map<String, Integer> params = new HashMap<>();
        params.put("updateId", updateId);
        params.put("offset", (page.getPage() - 1) * PAGE_SIZE);
        params.put("limit", PAGE_SIZE);

        final List<Comment> comments = db.query(
                sql,
                new MapSqlParameterSource(params),
                getMapper()
        );

        return comments;
    }

    @Override
    public void incrementCommentLikes(int commentId) {
        final String sql = "UPDATE `comments` SET `likes` = `likes` + 1 WHERE `id` = :id";

        db.getJdbcTemplate().update(sql, new MapSqlParameterSource("id", commentId));
    }

    @Override
    public void decrementCommentLikes(int commentId) {
        final String sql = "UPDATE `comments` SET `likes` = `likes` - 1 WHERE `id` = :id";

        db.getJdbcTemplate().update(sql, new MapSqlParameterSource("id", commentId));
    }

    @Override
    public boolean exists(int id) {
        final String sql = "SELECT count(*) FROM `comments` WHERE `id` = :id";
        try {
            final int count = db.getJdbcTemplate().queryForObject(
                    sql,
                    new MapSqlParameterSource("id", id),
                    (rs, row) -> rs.getInt(1)
            );

            return 0 < count;
        } catch (Exception e) {
            return false;
        }
    }

    private RowMapper<Comment> getMapper() {
        return (ResultSet rs, int rowNum) -> {
            final Comment comment = new Comment();
            comment.setId(rs.getInt("id"));
            comment.setContent(rs.getString("content"));
            comment.setUpdateId(rs.getInt("update_id"));
            comment.setUserId(rs.getInt("user_id"));
            comment.setLikes(rs.getInt("likes"));

            return comment;
        };
    }

}
