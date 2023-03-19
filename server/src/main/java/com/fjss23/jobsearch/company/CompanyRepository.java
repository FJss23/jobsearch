package com.fjss23.jobsearch.company;

import java.util.Optional;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class CompanyRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<Company> companyRowMapper;

    public CompanyRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.companyRowMapper = new BeanPropertyRowMapper<>(Company.class);
    }

    public Optional<Company> getCompanyById(Long id) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        String sql =
                """
        SELECT
            company_id as id,
            name,
            description,
            logo_url,
            twitter,
            facebook,
            instagram,
            website,
            created_at,
            created_by,
            updated_at,
            updated_by
        FROM
            jobsearch.company
        WHERE
            company_id = :id;
        """;
        params.addValue("id", id);
        try {
            Company company = jdbcTemplate.queryForObject(sql, params, companyRowMapper);
            return Optional.of(company);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }

    public Optional<Company> getCompanyFromUser(String email) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        String sql =
                """
        SELECT
            company_id as id,
            name,
            description,
            logo_url,
            twitter,
            facebook,
            instagram,
            website,
            created_at,
            created_by,
            updated_at,
            updated_by
        FROM
            jobsearch.company
        INNER JOIN jobsearch.appuser ON
            appuser.email = :email;
        """;
        params.addValue("email", email);
        try {
            Company company = jdbcTemplate.queryForObject(sql, params, companyRowMapper);
            return Optional.of(company);
        } catch (EmptyResultDataAccessException ex) {
            return Optional.empty();
        }
    }
}
