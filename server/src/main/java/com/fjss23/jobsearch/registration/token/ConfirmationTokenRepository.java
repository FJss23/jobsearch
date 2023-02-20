package com.fjss23.jobsearch.registration.token;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class ConfirmationTokenRepository {

    private NamedParameterJdbcTemplate jdbcTemplate;
    private BeanPropertyRowMapper<ConfirmationToken> confirmationTokenRowMapper;

    public ConfirmationTokenRepository(
        NamedParameterJdbcTemplate jdbcTemplate
    ) {
        this.jdbcTemplate = jdbcTemplate;
        this.confirmationTokenRowMapper =
            new BeanPropertyRowMapper<>(ConfirmationToken.class);
    }

    public void save(ConfirmationToken token) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        String sql =
            """
            INSERT INTO jobsearch.confirmation_token(
                token,
                expires_at,
                appuser_email)
            VALUES(
                :token,
                :expires_at,
                :appuser_email);
            """;
        params.addValue("token", token.getToken());
        params.addValue("expires_at", token.getExpiresAt());
        params.addValue("appuser_email", token.getAppUserEmail());
        jdbcTemplate.update(sql, params);
    }

    public int updateConfirmedAt(String token, LocalDateTime now) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        String sql =
            """
            UPDATE jobsearch.confirmation_token
            SET confirmed_at = :confirmed_at
            WHERE token = :token;
            """;
        params.addValue(":confirmed_at", now);
        params.addValue(":token", token);
        return jdbcTemplate.update(sql, params);
    }

    public Optional<ConfirmationToken> findByToken(String token) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        String sql =
            """
            SELECT
                confirmation_token_id as id,
                token,
                expires_at,
                confirmed_at,
                appuser_email,
                created_at
            FROM jobsearch.confirmation_token
            WHERE token = :token;
            """;
        params.addValue("token", token);
        var confirmationTokens = jdbcTemplate.queryForObject(
            sql,
            params,
            confirmationTokenRowMapper
        );
        return Optional.of(confirmationTokens);
    }
}
