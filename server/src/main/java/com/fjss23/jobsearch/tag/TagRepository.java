package com.fjss23.jobsearch.tag;

import java.util.List;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class TagRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<Tag> tagRowMapper;

    public TagRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.tagRowMapper = new BeanPropertyRowMapper<>(Tag.class);
    }

    public List<Tag> getTagsOfJob(Long jobId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        String sql =
                """
        SELECT
            tag.tag_id as id,
            tag.default_name,
            tag.tag_code as code,
            tag.created_at,
            tag.created_by,
            tag.updated_at,
            tag.updated_by
        FROM
            jobsearch.tag
        INNER JOIN
            jobsearch.job_tag ON
                tag.tag_id = job_tag.tag_id
        INNER JOIN
            jobsearch.job ON
                job_tag.job_id = job.job_id
        WHERE
            job.job_id = :id;
        """;
        params.addValue("id", jobId);
        return jdbcTemplate.query(sql, params, tagRowMapper);
    }

    public void deleteTagsOfJob(Long jobId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        String sql =
                """
        DELETE
        FROM
            jobsearch.job_tag
        WHERE
            job_id IN (
                SELECT
                    tag_id
                FROM
                    jobsearch.job_tag
                WHERE
                    job_id = :id);
        """;
        params.addValue("id", jobId);
        jdbcTemplate.update(sql, params);
    }

    public void createTagsOfJob(Long tagId, Long jobId) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        String sql =
                """
        INSERT
        INTO jobsearch.job_tag(
            tag_id,
            job_id)
        VALUES(
            :tagId,
            :jobId);
        """;
        params.addValue("tagId", tagId);
        params.addValue("jobId", jobId);
        jdbcTemplate.update(sql, params);
    }
}
