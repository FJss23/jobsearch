package com.fjss23.jobsearch.joboffer;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public class JobOfferRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<JobOffer> jobOfferRowMapper;

    public JobOfferRepository(NamedParameterJdbcTemplate jdbcTemplate, BeanPropertyRowMapper<JobOffer> jobOfferRowMapper) {
        this.jdbcTemplate = jdbcTemplate;
        this.jobOfferRowMapper = jobOfferRowMapper;
    }

    public List<JobOffer> findAll() {
        String sql = """
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
}
