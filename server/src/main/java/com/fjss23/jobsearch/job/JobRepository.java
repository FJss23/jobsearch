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
                role,
                salary_from,
                salary_up_to,
                salary_currency,
                location,
                workday,
                description,
                state,
                work_model,
                company_name,
                scrapped_from_url,
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
            role,
            salary_from,
            salary_up_to,
            salary_currency,
            location,
            workday,
            description,
            state,
            work_model,
            company_name,
            scrapped_from_url,
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
            role,
            salary_from,
            salary_up_to,
            salary_currency,
            location,
            workday,
            description,
            state,
            work_model,
            company_name,
            scrapped_from_url,
            created_at,
            created_by,
            updated_at,
            updated_by)
         VALUES(
             :title,
             :role,
             :salaryFrom,
             :salaryUpTo,
             :salaryCurrency,
             :location,
             :workday,
             :description,
             cast(:state.name as job_state),
             :workModel,
             :companyName,
             :scrappedFromUrl,
             :createdAt,
             :createdBy,
             :updatedAt,
             :updatedBy)
         RETURNING job_id as id;
        """;
        return jdbcTemplate.queryForObject(sql, new BeanPropertySqlParameterSource(job), jobRowMapper);
    }

    public int[] saveAll(List<Job> jobs) {
        // TODO: try passing a map with the batchUpdate, instead of a BeanPropertySqlParameterSource.
        //       The idea is to also insert the tags in the same sql statement using
        //       `INSERT INTO teams VALUES (...) RETURNING id INTO last_id;`
        BeanPropertySqlParameterSource[] paramSourceJobs = jobs.stream()
                .map(BeanPropertySqlParameterSource::new)
                .toArray(BeanPropertySqlParameterSource[]::new);
        String sql =
                """
         INSERT
         INTO jobsearch.job(
            title,
            role,
            salary_from,
            salary_up_to,
            salary_currency,
            location,
            workday,
            description,
            state,
            work_model,
            company_name,
            scrapped_from_url,
            created_at,
            created_by,
            updated_at,
            updated_by)
         VALUES(
             :title,
             :role,
             :salaryFrom,
             :salaryUpTo,
             :salaryCurrency,
             :location,
             :workday,
             :description,
             cast(:state.name as job_state),
             :workModel,
             :companyName,
             :scrappedFromUrl,
             :createdAt,
             :createdBy,
             :updatedAt,
             :updatedBy)
         RETURNING job_id;
        """;
        return jdbcTemplate.batchUpdate(sql, paramSourceJobs);
    }
}
