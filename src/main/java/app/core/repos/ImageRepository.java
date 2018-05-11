package app.core.repos;

import app.core.DB;
import app.core.repos.intefaces.ImageRepositoryInterface;
import app.core.repos.intefaces.UserRepositoryInterface;
import app.pojo.Image;
import app.pojo.Tag;
import app.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.List;
import java.util.Set;

@Component
public class ImageRepository extends BaseRepository implements ImageRepositoryInterface {
    @Autowired
    private DB db;

    public int add(final Image image) {
        final String sql = "INSERT INTO `images` (`mime_type`, `user_id`, created_at) VALUES " +
                "(:mimeType, :userId, :createdAt)";
        image.setCreatedAt(System.currentTimeMillis() / 1000);

        KeyHolder holder = new GeneratedKeyHolder();
        db.getJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(image), holder);

        return Integer.parseInt(holder.getKeys().get("GENERATED_KEY").toString());
    }

    public Image findById(final int id) {
        final String sql = "SELECT * FROM `images` WHERE `id` = :id LIMIT 1";
        try {
            final Image result = db.getJdbcTemplate().queryForObject(
                    sql,
                    new MapSqlParameterSource("id", id),
                    getMapper()
            );

            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public Image findForUser(final int userId) {
        final String sql = "SELECT * FROM `images` WHERE `user_id` = :userId ORDER BY created_at DESC LIMIT 1";
        try {
            final Image result = db.getJdbcTemplate().queryForObject(
                    sql,
                    new MapSqlParameterSource("userId", userId),
                    getMapper()
            );

            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public List<Image> findForUsers(final Set<Integer> userIds) {
        if (userIds.isEmpty()) {
            return getEmptyList(Image.class);
        }
        final String sql = "SELECT * FROM `images` WHERE `user_id` IN (:userIds)";
        try {
            final List<Image> result = db.query(
                    sql,
                    new MapSqlParameterSource("userIds", userIds),
                    getMapper()
            );

            return result;
        } catch (Exception e) {
            return getEmptyList(Image.class);
        }
    }

    private RowMapper<Image> getMapper() {
        return (final ResultSet rs, final int rowNum) -> {
            final Image image = new Image();
            image.setId(rs.getInt("id"));
            image.setMimeType(rs.getString("mime_type"));
            image.setUserId(rs.getInt("user_id"));
            image.setCreatedAt(rs.getInt("created_at"));

            return image;
        };
    }

}
