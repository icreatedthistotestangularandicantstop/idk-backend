package app.core.repos;

import app.core.DB;
import app.core.repos.intefaces.UpdateRepositoryInterface;
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
import java.util.List;

@Component
public class UpdateRepository implements UpdateRepositoryInterface {
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
    public Update findById(int id) {
        final String sql = "SELECT `id`, `content` FROM `updates` WHERE `id` = :id LIMIT 1";
        Update result = db.getJdbcTemplate().queryForObject(
                sql,
                new MapSqlParameterSource("id", id),
                getMapper()
        );

        return result;
    }

    @Override
    public List<Update> findByUserId(int userId) {
        final String sql = "SELECT `id`, `content` FROM `updates` WHERE `user_id` = :userId ORDER BY `created_at` DESC";
        List<Update> updates = db.getJdbcTemplate().query(
                sql,
                new MapSqlParameterSource("userId", userId),
                getMapper()
        );

        return updates;
    }

    private RowMapper<Update> getMapper() {
        return (ResultSet rs, int rowNum) -> {
            Update update = new Update();
            update.setId(rs.getInt("id"));
            update.setContent(rs.getString("content"));

            return update;
        };
    }

}
