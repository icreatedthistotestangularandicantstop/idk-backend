package app.core.repos;

import app.core.DB;
import app.core.repos.intefaces.NotificationRepositoryInterface;
import app.core.repos.intefaces.UpdateRepositoryInterface;
import app.http.pojos.Page;
import app.pojo.Favorite;
import app.pojo.Like;
import app.pojo.Notification;
import app.pojo.Update;
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
    public void see(final int notificationId) {
        final String sql = "UPDATE `notifications` SET seen = 1 WHERE `id` = :id";
        final Map<String, Integer> params = new HashMap<>();
        params.put("id", notificationId);

        db.getJdbcTemplate().update(sql, params);
    }

}
