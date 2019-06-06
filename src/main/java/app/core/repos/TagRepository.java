package app.core.repos;

import app.core.DB;
import app.core.repos.intefaces.LikeRepositoryInterface;
import app.pojo.Like;
import app.pojo.Tag;
import app.pojo.UpdateTag;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Component;

import java.sql.ResultSet;
import java.util.*;

@Component
public class TagRepository extends BaseRepository {
    @Autowired
    private DB db;

    public List<Tag> addTags(final Set<String> tagNames) {
        final List<Tag> tags = new ArrayList<>();
        for (String tagName : tagNames) {
            tags.add(addTag(tagName));
        }

        return tags;
    }

    public Tag addTag(final String name) {
        final String sql = "INSERT INTO `tags` (`name`, `created_at`) VALUES " +
                "(:name, :createdAt)";
        final int createdAt = (int) System.currentTimeMillis() / 1000;
        final Map<String, Object> params = new HashMap<>();
        params.put("name", name);
        params.put("createdAt", createdAt);

        KeyHolder holder = new GeneratedKeyHolder();
        db.getJdbcTemplate().update(sql, new MapSqlParameterSource(params), holder);

        final int tagId = Integer.parseInt(holder.getKeys().get("GENERATED_KEY").toString());
        final Tag tag = new Tag();
        tag.setName(name);
        tag.setCreatedAt(createdAt);
        tag.setId(tagId);

        return tag;
    }

    public List<UpdateTag> addUpdateTags(final Set<Integer> tagIds, final int updateId) {
        final List<UpdateTag> updateTags = new ArrayList<>();
        for (int tagId : tagIds) {
            updateTags.add(addUpdateTag(tagId, updateId));
        }

        return updateTags;
    }

    public UpdateTag addUpdateTag(final int tagId, final int updateId) {
        final String sql = "INSERT INTO `update_tags` (`tag_id`, `update_id`) VALUES (:tagId, :updateId)";
        final Map<String, Integer> params = new HashMap<>();
        params.put("tagId", tagId);
        params.put("updateId", updateId);

        KeyHolder holder = new GeneratedKeyHolder();
        db.getJdbcTemplate().update(sql, new MapSqlParameterSource(params), holder);

        final UpdateTag updateTag = new UpdateTag();
        updateTag.setTagId(tagId);
        updateTag.setUpdateId(updateId);

        return updateTag;
    }

    public List<Tag> findByName(final Set<String> names) {
        if (names.isEmpty()) {
            return getEmptyList(Tag.class);
        }
        final String sql = "SELECT * FROM `tags` WHERE `name` IN (:names)";
        try {
            final List<Tag> result = db.query(
                    sql,
                    new MapSqlParameterSource("names", names),
                    getMapper()
            );

            return result;
        } catch (Exception e) {
            return getEmptyList(Tag.class);
        }
    }

    public List<Tag> findMostPopular() {
        final String sql = "SELECT t.id, t.name, t.created_at, t1.count " +
                "FROM tags t " +
                "JOIN (" +
                    "SELECT tag_id, count(*) count " +
                    "FROM update_tags " +
                    "GROUP BY tag_id " +
                    "ORDER BY count DESC" +
                ") t1 ON t.id = t1.tag_id " +
                "ORDER BY t1.count " +
                "DESC LIMIT 5";
        try {
            final List<Tag> result = db.query(sql, getMapper());
            System.out.println(sql);
            System.out.println(result.size());

            return result;
        } catch (Exception e) {
            System.out.println(e.getMessage());
            return getEmptyList(Tag.class);
        }
    }

    public List<UpdateTag> findUpdateTagsByUpdateId(final int updateId) {
        final String sql = "SELECT * FROM `update_tags` WHERE `update_id` = :updateId";
        try {
            final List<UpdateTag> result = db.query(
                    sql,
                    new MapSqlParameterSource("updateId", updateId),
                    getUpdateTagMapper()
            );

            return result;
        } catch (Exception e) {
            return getEmptyList(UpdateTag.class);
        }
    }

    public List<Tag> findUpdateTagsByUpdateIds(final Set<Integer> updateIds) {
        final String sql =
                " SELECT t.*, u.update_id " +
                " FROM `tags` t " +
                " JOIN `update_tags` u ON t.`id` = u.`tag_id` " +
                " WHERE u.`update_id` IN (:updateIds)";
        try {
            final List<Tag> result = db.query(
                    sql,
                    new MapSqlParameterSource("updateIds", updateIds),
                    getTagUpdateMapper()
            );

            return result;
        } catch (Exception e) {
            return getEmptyList(Tag.class);
        }
    }

    private RowMapper<Tag> getMapper() {
        return (ResultSet rs, int rowNum) -> {
            final Tag tag = new Tag();
            tag.setId(rs.getInt("id"));
            tag.setName(rs.getString("name"));
            tag.setCreatedAt(rs.getInt("created_at"));

            return tag;
        };
    }

    private RowMapper<Tag> getTagUpdateMapper() {
        return (ResultSet rs, int rowNum) -> {
            final Tag tag = new Tag();
            tag.setId(rs.getInt("id"));
            tag.setName(rs.getString("name"));
            tag.setCreatedAt(rs.getInt("created_at"));
            tag.setUpdateId(rs.getInt("update_id"));

            return tag;
        };
    }

    private RowMapper<UpdateTag> getUpdateTagMapper() {
        return (ResultSet rs, int rowNum) -> {
            final UpdateTag updateTag = new UpdateTag();
            updateTag.setTagId(rs.getInt("tag_id"));
            updateTag.setUpdateId(rs.getInt("update_id"));

            return updateTag;
        };
    }

}
