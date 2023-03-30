package com.fjss23.jobsearch.job;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JobRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<Job> jobRowMapper;

    public JobRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jobRowMapper = new BeanPropertyRowMapper<>(Job.class);
    }

    public List<Job> findAll() {
        String sql =
                """
            SELECT
                job_id as id,
                title,
                industry,
                salary_from,
                salary_up_to,
                salary_coin as coin,
                location,
                workday_code as workday,
                description,
                state,
                company_id,
                workplace_system,
                how_to_apply,
                scrapped,
                created_at,
                created_by,
                updated_at,
                updated_by
            FROM
                jobsearch.job;
            """;
        return jdbcTemplate.query(sql, jobRowMapper);
    }

    public Optional<Job> findById(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        String sql =
                """
        SELECT
            job_id as id,
            title,
            industry,
            salary_from,
            salary_up_to,
            salary_coin as coin,
            location,
            workday_code as workday,
            description,
            state,
            company_id,
            workplace_system,
            how_to_apply,
            scrapped,
            created_at,
            created_by,
            updated_at,
            updated_by
        FROM
            jobsearch.job;
        WHERE
            job_id = :id;
        """;
        params.addValue("id", id);
        try {
            var job = jdbcTemplate.queryForObject(sql, params, jobRowMapper);
            return Optional.of(job);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    public Long deleteById(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        String sql =
                """
        DELETE
        FROM
            jobsearch.job
        WHERE
            job_id = :id;
        """;
        params.addValue("id", id);
        return jdbcTemplate.queryForObject(sql, params, Long.class);
    }

    public Job save(Job job) {
        String sql =
                """
        INSERT
        INTO jobsearch.job(
            title,
            industry,
            salary_from,
            salary_up_to,
            salary_currency,
            location,
            workday_code,
            description,
            state,
            company_id,
            workplace_system,
            how_to_apply,
            scrapped)
        VALUES(
            :title,
            :industry,
            :salaryFrom,
            :salaryUpTo,
            :coin,
            :location,
            :workday,
            :description,
            :state,
            :workplaceSystem,
            :howToApply,
            :scrapped)
        RETURNING *;
        """;
        return jdbcTemplate.queryForObject(sql, new BeanPropertySqlParameterSource(job), jobRowMapper);
    }
}
