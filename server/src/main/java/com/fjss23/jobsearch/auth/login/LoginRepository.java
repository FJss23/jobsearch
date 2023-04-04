package com.fjss23.jobsearch.auth.login;

import java.util.Optional;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class LoginRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<RefreshTokenInfo> refreshAuthRowMapper;

    public LoginRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.refreshAuthRowMapper = new BeanPropertyRowMapper<>(RefreshTokenInfo.class);
    }

    public Long create(RefreshTokenInfo refreshTokenInfo) {
        String sql =
                """
            INSERT
            INTO jobsearch.refresh_token_info(
                location,
                device,
                appuser_email)
            VALUES(
                :location,
                :device,
                :appUserEmail)

                refresh_token_info_id;
            """;
        return jdbcTemplate.queryForObject(sql, new BeanPropertySqlParameterSource(refreshTokenInfo), Long.class);
    }

    public void deleteById(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        String sql =
                """
            DELETE
            FROM
                jobsearch.refresh_token_info
            WHERE
                refresh_token_info_id = :id
            """;
        params.addValue("id", id);
        jdbcTemplate.update(sql, params);
    }

    public void deleteByAppUserEmail(String email) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        String sql =
                """
            DELETE
            FROM
                jobsearch.refresh_token_info
            WHERE
                appuser_email = :email
            """;
        params.addValue("email", email);
        jdbcTemplate.update(sql, params);
    }

    public Optional<RefreshTokenInfo> findById(Long refreshId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        String sql =
                """
            SELECT
                refresh_token_info_id as id,
                location,
                device,
                appuser_email as appUserEmail
            FROM
                jobsearch.refresh_token_info
            WHERE
                refresh_token_info_id = :id;
            """;
        params.addValue("id", refreshId);
        var refreshAuth = jdbcTemplate.queryForObject(sql, params, refreshAuthRowMapper);
        return Optional.of(refreshAuth);
    }
}
