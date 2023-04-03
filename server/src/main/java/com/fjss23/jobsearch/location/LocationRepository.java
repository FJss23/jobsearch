package com.fjss23.jobsearch.location;

import java.util.List;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LocationRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<Location> locationRowMapper;

    public LocationRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.locationRowMapper = new BeanPropertyRowMapper<>(Location.class);
    }

    public List<Location> findAll() {
        String sql =
                """
        SELECT
            location_id as id,
            name,
            created_at,
            created_by,
            updated_at,
            updated_by
        FROM
            jobsearch.keyword_location;
        """;
        return jdbcTemplate.query(sql, locationRowMapper);
    }
}
