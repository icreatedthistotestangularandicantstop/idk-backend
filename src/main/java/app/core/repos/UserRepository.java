package app.core.repos;

import app.core.DB;
import app.core.repos.intefaces.UserRepositoryInterface;
import app.pojo.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;

@Component
public class UserRepository implements UserRepositoryInterface {
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
        User result = db.getJdbcTemplate().queryForObject(
                sql,
                new MapSqlParameterSource("username", username),
                (ResultSet rs, int rowNum) -> {
                    User user = new User();
                    user.setId(rs.getInt("id"));
                    user.setFirstName(rs.getString("first_name"));
                    user.setLastName(rs.getString("last_name"));
                    user.setUsername(rs.getString("username"));
                    user.setPassword(rs.getString("password"));

                    return user;
                }
        );

        return result;
    }

}
