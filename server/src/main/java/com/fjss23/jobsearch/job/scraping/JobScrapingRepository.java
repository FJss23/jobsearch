package com.fjss23.jobsearch.job.scraping;

import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JobScrapingRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<ScrapingSource> scrapingSourceRowMapper;

    public JobScrapingRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.scrapingSourceRowMapper = new BeanPropertyRowMapper<>(ScrapingSource.class);
    }

    public Optional<ScrapingSource> getScrapingSourceActiveByName(String name) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        String sql =
                """
        SELECT
            scraping_source_id as id,
            name,
            base_url,
            url,
            active,
            created_at,
            created_by,
            updated_at,
            updated_by
        FROM
            jobsearch.scraping_source
        WHERE
            name like :name AND active = true
        LIMIT 1;
        """;
        params.addValue("name", name);
        try {
            var scrapingSource = jdbcTemplate.queryForObject(sql, params, scrapingSourceRowMapper);
            return Optional.of(scrapingSource);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    public ScrapingSource save(ScrapingSource scrapingSource) {
        String sql =
                """
         INSERT
         INTO jobsearch.scraping_source(
            name,
            base_url,
            url,
            active,
            created_by,
            updated_by)
         VALUES(
             :name,
             :baseUrl,
             :url,
             :active,
             :createdBy,
             :updatedBy)
         RETURNING scraping_source_id as id;
        """;
        return jdbcTemplate.queryForObject(
                sql, new BeanPropertySqlParameterSource(scrapingSource), scrapingSourceRowMapper);
    }

    public int setInactiveBySourceName(String name) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        String sql =
            """
        UPDATE
            jobsearch.scraping_source
        SET
            active = false
        WHERE
            name like :name;
        """;
        params.addValue("name", name);
        return jdbcTemplate.update(sql, params);
    }
}
