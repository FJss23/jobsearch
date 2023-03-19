package com.fjss23.jobsearch.joboffer;

import java.util.List;
import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class JobOfferRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<JobOffer> jobOfferRowMapper;

    public JobOfferRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.jobOfferRowMapper = new BeanPropertyRowMapper<>(JobOffer.class);
    }

    public List<JobOffer> findAll() {
        String sql =
                """
            SELECT
                joboffer_id as id,
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
                jobsearch.joboffer;
            """;
        return jdbcTemplate.query(sql, jobOfferRowMapper);
    }

    public Optional<JobOffer> findById(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        String sql =
                """
        SELECT
            joboffer_id as id,
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
            jobsearch.joboffer;
        WHERE
            joboffer_id = :id;
        """;
        params.addValue("id", id);
        try {
            var jobOffer = jdbcTemplate.queryForObject(sql, params, jobOfferRowMapper);
            return Optional.of(jobOffer);
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
            jobsearch.joboffer
        WHERE
            joboffer_id = :id;
        """;
        params.addValue("id", id);
        return jdbcTemplate.queryForObject(sql, params, Long.class);
    }

    public JobOffer save(JobOffer jobOffer) {
        throw new UnsupportedOperationException("no implemented yet");
    }
}
