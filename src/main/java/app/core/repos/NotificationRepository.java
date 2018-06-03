package app.core.repos;

import app.core.DB;
import app.core.repos.intefaces.NotificationRepositoryInterface;
import app.core.repos.intefaces.UpdateRepositoryInterface;
import app.http.pojos.Page;
import app.pojo.*;
import org.springframework.asm.Type;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.sql.Types;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("unused")
@Component
public class NotificationRepository extends BaseRepository implements NotificationRepositoryInterface {

    private static final int PAGE_SIZE = 20;
    private final DB db;

    NotificationRepository(final DB db) {
        this.db = db;
    }

    @Override
    public int add(final Notification notification) {
        final String sql =
                " INSERT INTO `notifications` (`to_user_id`, `from_user_id`, `rel_id`, `rel_type`, `seen`, `created_at`) " +
                "                      VALUES (:toUserId, :fromUserId, :relId, :relType, :seen, :createdAt)";
        notification.setCreatedAt(System.currentTimeMillis() / 1000);

        final KeyHolder holder = new GeneratedKeyHolder();
        final BeanPropertySqlParameterSource map = new BeanPropertySqlParameterSource(notification);
        db.getJdbcTemplate().update(sql, map, holder);

        return Integer.parseInt(holder.getKeys().get("GENERATED_KEY").toString());
    }

    @Override
    public void see(final int notificationId, final int userId) {
        final String sql = "UPDATE `notifications` SET seen = 1 WHERE `id` = :id AND `to_user_id` = :userId";
        final Map<String, Integer> params = new HashMap<>();
        params.put("id", notificationId);
        params.put("userId", userId);

        db.getJdbcTemplate().update(sql, params);
    }

    @Override
    public int getNotSeen(final  int userId) {
        final String sql = "SELECT count(*) FROM `notifications` WHERE `to_user_id` = :userId AND `seen` = 0";
        final Map<String, Integer> params = new HashMap<>();
        params.put("userId", userId);

        try {
            final int notSeen = db.getJdbcTemplate().queryForObject(
                    sql,
                    new MapSqlParameterSource(params),
                    Integer.class
            );

            return notSeen;
        } catch (Exception e) {
            return 0;
        }
    }

    @Override
    public List<Notification> findPaged(final int userId, final Page page) {
        final String sql =
                " SELECT * " +
                " FROM `notifications` " +
                " WHERE `to_user_id` = :userId " +
                " ORDER BY `created_at` DESC " +
                " LIMIT :offset, :limit ";
        final Map<String, Integer> params = new HashMap<>();
        params.put("userId", userId);
        params.put("offset", (page.getPage() - 1) * PAGE_SIZE);
        params.put("limit", PAGE_SIZE);

        final List<Notification> notifications = db.query(
                sql,
                new MapSqlParameterSource(params),
                getMapper()
        );

        return notifications;
    }

    private RowMapper<Notification> getMapper() {
        return (ResultSet rs, int rowNum) -> {
            final Notification notification = new Notification();
            notification.setId(rs.getInt("id"));
            notification.setToUserId(rs.getInt("to_user_id"));
            notification.setCreatedAt(rs.getInt("created_at"));
            notification.setFromUserId(rs.getInt("from_user_id"));
            notification.setRelId(rs.getInt("rel_id"));
            notification.setSeen(rs.getBoolean("seen"));
            notification.setType(NotificationType.fromString(rs.getString("rel_type")));
            notification.setCreatedAt(rs.getInt("created_at"));

            return notification;
        };
    }

}
