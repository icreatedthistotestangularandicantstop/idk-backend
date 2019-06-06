package app.core.repos;

import app.core.DB;
import app.core.repos.intefaces.UserRepositoryInterface;
import app.http.pojos.UserCreateResource;
import app.http.pojos.UserUpdateResource;
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
import java.util.Set;

@Component
public class UserRepository extends BaseRepository implements UserRepositoryInterface {

    private final DB db;

    @Autowired
    UserRepository(final DB db) {
        this.db = db;
    }

    public int add(final UserCreateResource user) {
        final String sql = "INSERT INTO `users` (`first_name`, `last_name`, `username`, `created_at`, `password`) VALUES " +
                "(:firstName, :lastName, :username, :createdAt, :password)";

        final Map<String, Object> params = new HashMap<>();
        params.put("firstName", user.getFirstName());
        params.put("lastName", user.getLastName());
        params.put("username", user.getUsername());
        params.put("createdAt", System.currentTimeMillis() / 1000);
        params.put("password", user.getPassword());

        KeyHolder holder = new GeneratedKeyHolder();
        db.getJdbcTemplate().update(sql, new MapSqlParameterSource(params), holder);

        return Integer.parseInt(holder.getKeys().get("GENERATED_KEY").toString());
    }

    @Override
    public void update(final UserUpdateResource user, final int userId) {
        final String sql =
                " UPDATE `users` SET `first_name` = :firstName, `last_name` = :lastName, `username` = :username " +
                " WHERE `id` = :id ";
        final Map<String, Object> params = new HashMap<>();
        params.put("firstName", user.getFirstName());
        params.put("lastName", user.getLastName());
        params.put("username", user.getUsername());
        params.put("id", userId);

        db.getJdbcTemplate().update(sql, params);
    }

    public User findByUsername(final String username) {
        final String sql = "SELECT `id`, `first_name`, `last_name`, `username`, `password`, `created_at` FROM `users` WHERE `username` = :username LIMIT 1";
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
        final String sql =
                " SELECT " +
                "   `id`, " +
                "   `first_name`, " +
                "   `last_name`, " +
                "   `username`, " +
                "   `password`, " +
                "   `followers`, " +
                "   `following`, " +
                "   `updates`, " +
                "   `created_at` " +
                " FROM `users` " +
                " WHERE `id` = :id LIMIT 1 ";
        try {
            final User result = db.getJdbcTemplate().queryForObject(
                    sql,
                    new MapSqlParameterSource("id", id),
                    getMapperInfo()
            );

            return result;
        } catch (Exception e) {
            return null;
        }
    }

    @Override
    public List<User> findByIds(final Set<Integer> ids) {
        if (ids.isEmpty()) {
            return getEmptyList(User.class);
        }
        final String sql = "SELECT `id`, `first_name`, `last_name`, `username`, `created_at` FROM `users` WHERE `id` IN (:ids)";
        final List<User> users = db.query(sql, new MapSqlParameterSource("ids", ids), getMapperSmall());

        return users;
    }

    @Override
    public List<User> findMostPopular() {
        final String sql =
                "SELECT `id`, `first_name`, `last_name`, `username`, `created_at` " +
                "FROM `users` " +
                "WHERE followers > 0 " +
                "ORDER BY followers " +
                "DESC LIMIT 5";
        final List<User> users = db.query(sql, getMapperSmall());

        return users;
    }

    @Override
    public void incrementFollowersFor(final int userId) {
        final String sql = "UPDATE `users` SET `followers` = `followers` + 1 WHERE `id` = :id";

        db.getJdbcTemplate().update(sql, new MapSqlParameterSource("id", userId));
    }

    @Override
    public void decrementFollowersFor(final int userId) {
        final String sql = "UPDATE `users` SET `followers` = `followers` - 1 WHERE `id` = :id";

        db.getJdbcTemplate().update(sql, new MapSqlParameterSource("id", userId));
    }

    @Override
    public void incrementFollowedFor(final int userId) {
        final String sql = "UPDATE `users` SET `following` = `following` + 1 WHERE `id` = :id";

        db.getJdbcTemplate().update(sql, new MapSqlParameterSource("id", userId));
    }

    @Override
    public void decrementFollowedFor(final int userId) {
        final String sql = "UPDATE `users` SET `following` = `following` - 1 WHERE `id` = :id";

        db.getJdbcTemplate().update(sql, new MapSqlParameterSource("id", userId));
    }

    @Override
    public void incrementUpdates(final int userId) {
        final String sql = "UPDATE `users` SET `updates` = `updates` + 1 WHERE `id` = :id";

        db.getJdbcTemplate().update(sql, new MapSqlParameterSource("id", userId));
    }

    @Override
    public void decrementUpdates(final int userId) {
        final String sql = "UPDATE `users` SET `updates` = `updates` - 1 WHERE `id` = :id";

        db.getJdbcTemplate().update(sql, new MapSqlParameterSource("id", userId));
    }

    private RowMapper<User> getMapper() {
        return (final ResultSet rs, final int rowNum) -> {
            final User user = new User();
            user.setId(rs.getInt("id"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setUsername(rs.getString("username"));
            user.setPassword(rs.getString("password"));
            user.setCreatedAt(rs.getInt("created_at"));

            return user;
        };
    }

    private RowMapper<User> getMapperSmall() {
        return (final ResultSet rs, final int rowNum) -> {
            final User user = new User();
            user.setId(rs.getInt("id"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setUsername(rs.getString("username"));
            user.setCreatedAt(rs.getInt("created_at"));

            return user;
        };
    }

    private RowMapper<User> getMapperInfo() {
        return (final ResultSet rs, final int rowNum) -> {
            final User user = new User();
            user.setId(rs.getInt("id"));
            user.setFirstName(rs.getString("first_name"));
            user.setLastName(rs.getString("last_name"));
            user.setUsername(rs.getString("username"));
            user.setFollowers(rs.getInt("followers"));
            user.setFollowing(rs.getInt("following"));
            user.setUpdates(rs.getInt("updates"));
            user.setCreatedAt(rs.getInt("created_at"));


            return user;
        };
    }

}
