package app.core.repos;

import app.core.DB;
import app.core.repos.intefaces.UserRepositoryInterface;
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
public class UserRepository extends BaseRepository implements UserRepositoryInterface {
    @Autowired
    private DB db;

    public int add(User user) {
        final String sql = "INSERT INTO `users` (`first_name`, `last_name`, `username`, `created_at`, `password`) VALUES " +
                "(:first_name, :last_name, :username, :created_at, :password)";
        user.setCreatedAt(System.currentTimeMillis() / 1000);
        user.setPassword("top_secret");

        KeyHolder holder = new GeneratedKeyHolder();
        db.getJdbcTemplate().update(sql, new BeanPropertySqlParameterSource(user), holder);

        return Integer.parseInt(holder.getKeys().get("GENERATED_KEY").toString());
    }

    public User findByUsername(String username) {
        final String sql = "SELECT `id`, `first_name`, `last_name`, `username`, `password` FROM `users` WHERE `username` = :username LIMIT 1";
        try {
            final User result = db.getJdbcTemplate().queryForObject(
                    sql,
                    new MapSqlParameterSource("username", username),
                    getMapper()
            );

            return result;
        } catch (Exception e) {
            return null;
        }
    }

    public User findById(final int id) {
        final String sql = "SELECT `id`, `first_name`, `last_name`, `username`, `password` FROM `users` WHERE `id` = :id LIMIT 1";
        try {
            final User result = db.getJdbcTemplate().queryForObject(
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
    public List<User> findByIds(Set<Integer> ids) {
        if (ids.isEmpty()) {
            return getEmptyList(User.class);
        }
        final String sql = "SELECT `id`, `first_name`, `last_name`, `username` FROM `users` WHERE `id` IN (:ids)";
        List<User> users = db.query(sql, new MapSqlParameterSource("ids", ids), getMapperSmall());

        return users;
    }

    private RowMapper<User> getMapper() {
        return (ResultSet rs, int rowNum) -> {
            final User user = new User();
            user.setId(rs.getInt("id"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));

            return user;
        };
    }

    private RowMapper<User> getMapperSmall() {
        return (ResultSet rs, int rowNum) -> {
            final User user = new User();
            user.setId(rs.getInt("id"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setUsername(rs.getString("username"));

            return user;
        };
    }

}
