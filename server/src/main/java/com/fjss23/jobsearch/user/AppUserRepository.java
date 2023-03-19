package com.fjss23.jobsearch.user;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class AppUserRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<AppUser> appUserRowMapper;

    public AppUserRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.appUserRowMapper = new BeanPropertyRowMapper<>(AppUser.class);
    }

    Optional<AppUser> findByEmail(String email) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        String sql =
                """
            SELECT
                appuser_id as id,
                first_name,
                last_name,
                email,
                password,
                role as userRole,
                locked,
                enabled,
                logged_at,
                company_id
            FROM
                jobsearch.appuser
            WHERE
                email = :email;
            """;
        params.addValue("email", email);
        try {
            var appUsers = jdbcTemplate.queryForObject(sql, params, appUserRowMapper);
            return Optional.of(appUsers);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    public List<AppUser> findAll() {
        String sql =
                """
            SELECT
                appuser_id as id,
                first_name,
                last_name,
                email,
                password,
                role as userRole,
                locked,
                enabled,
                logged_at,
                company_id,
                created_by
            FROM
                jobsearch.appuser;
            """;
        return jdbcTemplate.query(sql, appUserRowMapper);
    }

    public void create(AppUser appUser) {
        String sql =
                """
            INSERT
            INTO jobsearch.appuser(
                first_name,
                last_name,
                email,
                password,
                role,
                locked,
                enabled,
                created_by)
            VALUES(
                :firstName,
                :lastName,
                :email,
                :password,
                cast(:userRole.name as appuser_role),
                :locked,
                :enabled,
                :createdBy);
            """;
        jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(appUser));
    }

    public int enableAppUser(String email) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        String sql =
                """
            UPDATE
                jobsearch.appuser
            SET
                enabled = true
            WHERE
                email = :email;
            """;
        params.addValue("email", email);
        return jdbcTemplate.update(sql, params);
    }
}
