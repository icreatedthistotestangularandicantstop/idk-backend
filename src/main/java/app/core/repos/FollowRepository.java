package app.core.repos;

import app.core.DB;
import app.core.repos.intefaces.FollowRepositoryInterface;
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

@SuppressWarnings("unused")
@Component
public class FollowRepository extends BaseRepository implements FollowRepositoryInterface {

    private final DB db;

    FollowRepository(final DB db) {
        this.db = db;
    }

    @Override
    public boolean follow(final int followerId, final int followedId) {
        final String sql =
                " INSERT INTO `followers` (`follower_id`, `followed_id`, `followed_at`) " +
                "                  VALUES (:followerId, :followedId, :followedAt)";
        final int followedAt = (int) System.currentTimeMillis() / 1000;
        final Map<String, Integer> params = new HashMap<>();
        params.put("followerId", followerId);
        params.put("followedId", followedId);
        params.put("followedAt", followedAt);

        return 0 < db.getJdbcTemplate().update(sql, new MapSqlParameterSource(params));
    }

    @Override
    public boolean unfollow(int followerId, int followedId) {
        final String sql =
                " DELETE FROM `followers` WHERE follower_id = :followerId AND followed_id = :followedId";
        final Map<String, Integer> params = new HashMap<>();
        params.put("followerId", followerId);
        params.put("followedId", followedId);

        return 0 < db.getJdbcTemplate().update(sql, new MapSqlParameterSource(params));
    }

    @Override
    public boolean isFollowd(final int followerId, final int followedId) {
        final String sql =
                " SELECT count(*) " +
                " FROM `followers` " +
                " WHERE `follower_id` = :followerId AND `followed_id` = :followedId " +
                " LIMIT 1 ";
        final Map<String, Integer> params = new HashMap<>();
        params.put("followerId", followerId);
        params.put("followedId", followedId);

        try {
            final int followed = db.getJdbcTemplate().queryForObject(
                sql,
                new MapSqlParameterSource(params),
                Integer.class
            );

            return 0 < followed;
        } catch (Exception e) {
            return false;
        }
    }

    @Override
    public List<Integer> getFollowersOf(final int userId) {
        final String sql = "SELECT `follower_id` FROM `followers` WHERE `followed_id` = :userId";
        try {
            final List<Integer> result = db.query(
                    sql,
                    new MapSqlParameterSource("userId", userId),
                    (final ResultSet rs, final int row) -> rs.getInt("follower_id")
            );

            return result;
        } catch (Exception e) {
            return getEmptyList(Integer.class);
        }
    }
}
