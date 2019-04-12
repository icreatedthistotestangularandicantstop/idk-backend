package app.core;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.stereotype.Component;
import javax.sql.DataSource;
import java.util.LinkedList;
import java.util.List;

@Component
public class DB {
    private NamedParameterJdbcTemplate jdbcTemplate;

    @Autowired
    public DB(DataSource dataSource) {
        this.jdbcTemplate = new NamedParameterJdbcTemplate(dataSource);
    }

    public NamedParameterJdbcTemplate getJdbcTemplate() {
        return jdbcTemplate;
    }

    public <T> List<T> query(String sql, SqlParameterSource paramSource, RowMapper<T> rowMapper) {
        List<T> result = jdbcTemplate.query(sql, paramSource, rowMapper);

        return result == null ? new LinkedList<>() : result;
    }
}
