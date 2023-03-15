package com.fjss23.jobsearch.auth.login;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LoginRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<RefreshAuth> refreshAuthRowMapper;

    public LoginRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.refreshAuthRowMapper = new BeanPropertyRowMapper<>(RefreshAuth.class);
    }

    public long create(RefreshAuth refreshAuth) {
        String sql =
            """
            INSERT INTO jobsearch.refresh_token_info(
                location,
                device,
                appuser_email)
            VALUES(
                :location,
                :device,
                :appUserEmail)
            RETURNING refresh_token_info_id;
            """;
        return jdbcTemplate.queryForObject(sql, new BeanPropertySqlParameterSource(refreshAuth), Long.class);
    }

    public void deleteById(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        String sql = """
            DELETE FROM jobsearch.refresh_token_info
            WHERE refresh_token_info_id = :id
            """;
        params.addValue("id", id);
        jdbcTemplate.update(sql, params);
    }
}
