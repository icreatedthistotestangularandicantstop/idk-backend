package app.core.repos;

import app.core.DB;
import app.core.repos.intefaces.UpdateRepositoryInterface;
import app.http.pojos.Page;
import app.pojo.Comment;
import app.pojo.Favorite;
import app.pojo.Update;
import app.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class UpdateRepository extends BaseRepository implements UpdateRepositoryInterface {
    public static final int PAGE_SIZE = 10;

    @Autowired
    private DB db;

    @Override
    public int add(Update update) {
        final String sql = "INSERT INTO `updates` (`content`, `user_id`, `created_at`) VALUES " +
                "(:content, :userId, :createdAt)";
        update.setCreatedAt(System.currentTimeMillis() / 1000);

        KeyHolder holder = new GeneratedKeyHolder();
        db.getJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(update), holder);

        return Integer.parseInt(holder.getKeys().get("GENERATED_KEY").toString());
    }

    @Override
    public void addFavorite(int updateId, int userId) {
        final String sql = "INSERT INTO `favorites` (`update_id`, `user_id`, `favorited_at`) VALUES " +
                "(:updateId, :userId, :favoritedAt)";
        final Map<String, Integer> params = new HashMap<>();
        params.put("userId", userId);
        params.put("updateId", updateId);
        params.put("favoritedAt", (int) System.currentTimeMillis() / 1000);

        db.getJdbcTemplate().update(
                sql,
                new MapSqlParameterSource(params)
        );
    }

    @Override
    public Favorite findFavoriteByUpdateIdAndUserId(int updateId, int userId) {
        final String sql = "SELECT * FROM `favorites` WHERE `update_id` = :updateId AND `user_id` = :userId LIMIT 1";
        final Map<String, Integer> params = new HashMap<>();
        params.put("updateId", updateId);
        params.put("userId", userId);

        try {
            final Favorite result = db.getJdbcTemplate().queryForObject(
                    sql,
                    new MapSqlParameterSource(params),
                    getFavoriteMapper()
            );

            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public int updateFavorites(int updateId) {
        final String sql = "UPDATE `updates` SET `favorites` = `favorites` + 1 WHERE `id` = :id";

        KeyHolder holder = new GeneratedKeyHolder();
        int updated = db.getJdbcTemplate().update(sql, new MapSqlParameterSource("id", updateId));

        return updated;
    }

    @Override
    public List<Update> findPaged(Page page) {
        final String sql = "SELECT * " +
                " FROM `updates` " +
                " ORDER BY `created_at` DESC " +
                " LIMIT :offset, :limit"
                ;
        final Map<String, Integer> params = new HashMap<>();
        params.put("offset", (page.getPage() - 1) * PAGE_SIZE);
        params.put("limit", PAGE_SIZE);

        final List<Update> updates = db.query(
                sql,
                new MapSqlParameterSource(params),
                getMapper()
        );

        return updates;
    }

    @Override
    public Update findById(int id) {
        final String sql = "SELECT * FROM `updates` WHERE `id` = :id LIMIT 1";
        try {
            final Update result = db.getJdbcTemplate().queryForObject(
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
    public Update findByIdAndUserId(int id, int userId) {
        final String sql = "SELECT * FROM `updates` WHERE `id` = :id AND `user_id` = :userId LIMIT 1";
        final Map<String, Integer> params = new HashMap<>();
        params.put("id", id);
        params.put("userId", userId);

        try {
            final Update result = db.getJdbcTemplate().queryForObject(
                    sql,
                    new MapSqlParameterSource(params),
                    getMapper()
            );

            return result;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<Update> findByUserId(int userId) {
        final String sql = "SELECT * FROM `updates` WHERE `user_id` = :userId ORDER BY `created_at` DESC";
        final List<Update> updates = db.query(
                sql,
                new MapSqlParameterSource("userId", userId),
                getMapper()
        );

        return updates;
    }

    @Override
    public void incrementUpdateComments(int updateId) {
        final String sql = "UPDATE `updates` SET `comments` = `comments` + 1 WHERE `id` = :id";

        db.getJdbcTemplate().update(sql, new MapSqlParameterSource("id", updateId));
    }

    @Override
    public void incrementUpdateLikes(int updateId) {
        final String sql = "UPDATE `updates` SET `likes` = `likes` + 1 WHERE `id` = :id";

        db.getJdbcTemplate().update(sql, new MapSqlParameterSource("id", updateId));
    }

    @Override
    public void decrementUpdateLikes(int updateId) {
        final String sql = "UPDATE `updates` SET `likes` = `likes` - 1 WHERE `id` = :id";

        db.getJdbcTemplate().update(sql, new MapSqlParameterSource("id", updateId));
    }

    @Override
    public boolean exists(int id) {
        final String sql = "SELECT count(*) FROM `updates` WHERE `id` = :id";
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

    private RowMapper<Update> getMapper() {
        return (ResultSet rs, int rowNum) -> {
            final Update update = new Update();
            update.setId(rs.getInt("id"));
            update.setContent(rs.getString("content"));
            update.setLikes(rs.getInt("likes"));
            update.setCommentsCount(rs.getInt("comments"));

            return update;
        };
    }

    private RowMapper<Favorite> getFavoriteMapper() {
        return (ResultSet rs, int rowNum) -> {
            final Favorite favorite = new Favorite();
            favorite.setId(rs.getInt("id"));
            favorite.setUpdateId(rs.getInt("update_id"));
            favorite.setUserId(rs.getInt("user_id"));

            return favorite;
        };
    }

}
