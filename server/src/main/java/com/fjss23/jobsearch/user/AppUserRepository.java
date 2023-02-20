package com.fjss23.jobsearch.user;

import java.util.Optional;

import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
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
                role,
                locked,
                enabled,
                logged_at,
                company_id
            FROM jobsearch.appuser
            WHERE email = :email;
            """;
        params.addValue("email", email);
        try {
            var appUsers = jdbcTemplate.queryForObject(sql, params, appUserRowMapper);
            return Optional.of(appUsers);
        } catch(EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    void save(AppUser appUser) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        String sql =
            """
            INSERT INTO jobsearch.appuser(
                first_name,
                last_name,
                email,
                password,
                role,
                locked,
                enabled,
                created_by)
            VALUES(
                :first_name,
                :last_name,
                :email,
                :password,
                :role,
                :locked,
                :enabled,
                :created_by);
            """;
        params.addValue("first_name", appUser.getFirstName());
        params.addValue("last_name", appUser.getLastName());
        params.addValue("email", appUser.getEmail());
        params.addValue("password", appUser.getPassword());
        params.addValue("role", appUser.getUserRole().toString());
        params.addValue("locked", appUser.getLocked());
        params.addValue("enabled", appUser.isEnabled());
        params.addValue("created_by", appUser.getEmail());
        jdbcTemplate.update(sql, params);
    }

    public int enableAppUser(String email) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        String sql =
            """
            UPDATE jobsearch.appuser
            SET enabled = :enabled
            WHERE email = :email;
            """;
        params.addValue("email", email);
        return jdbcTemplate.update(sql, params);
    }
}
