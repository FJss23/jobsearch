package com.fjss23.jobsearch.job;

import jakarta.validation.constraints.NotNull;
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

    record TextSearch(String query, MapSqlParameterSource params) {}

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
                company_logo_url,
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
                company_logo_url,
                created_at,
                created_by,
                updated_at,
                updated_by
            FROM
                jobsearch.job
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
                company_logo_url,
                created_by,
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
                 :companyLogoUrl,
                 :createdBy,
                 :updatedBy)
             RETURNING job_id as id;
            """;
        return jdbcTemplate.queryForObject(sql, new BeanPropertySqlParameterSource(job), jobRowMapper);
    }

    public List<Job> findPaginated(Filter filter) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sql = new StringBuilder(
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
                company_logo_url,
                created_at,
                created_by,
                updated_at,
                updated_by
            FROM
                jobsearch.job
        """);

        if (filter.search() == null) {
            sql.append("WHERE job_id < :from ORDER BY job_id DESC FETCH FIRST :size ROWS ONLY;");
            params.addValue("from", filter.from());
            params.addValue("size", filter.size());
            return jdbcTemplate.query(sql.toString(), params, jobRowMapper);
        }
        String symbol = "<"; // Next page
        if ("prev".equals(filter.page())) symbol = ">"; // Previous page

        TextSearch ts = getTextSearchSql(filter.getTerms());
        sql.append(" WHERE job_id ")
                .append(symbol)
                .append(" :from")
                .append(ts.query())
                .append("ORDER BY job_id DESC FETCH FIRST :size ROWS ONLY;");

        params = ts.params();
        params.addValue("from", filter.from());
        params.addValue("size", filter.size());
        return jdbcTemplate.query(sql.toString(), params, jobRowMapper);
    }

    public Integer getTotalJobs(Filter filter) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sql = new StringBuilder(
                """
            SELECT
                COUNT(*)
            FROM
                jobsearch.job
            """);

        if (filter.search() == null) {
            sql.append(";");
            return jdbcTemplate.queryForObject(sql.toString(), params, Integer.class);
        }

        // If search terms are present, we create a query following the pattern:
        // SELECT * COUNT(*) FROM jobsearch.job WHERE ts @@ TO_TSQUERY('english', ':param_0 & :param_1 & ... :paramN');
        TextSearch ts = getTextSearchSql(filter.getTerms());

        sql.append(" WHERE ").append(ts.query()).append(";");
        return jdbcTemplate.queryForObject(sql.toString(), ts.params(), Integer.class);
    }

    public List<Job> findFirstPage(Filter filter) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sql = new StringBuilder(
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
                company_logo_url,
                created_at,
                created_by,
                updated_at,
                updated_by
            FROM
                jobsearch.job
            """);

        if (filter.search() == null) {
            sql.append("ORDER BY job_id DESC FETCH FIRST :size ROWS ONLY;");

            params.addValue("size", filter.size());
            return jdbcTemplate.query(sql.toString(), params, jobRowMapper);
        }

        // If search terms are present, we create a query following the pattern:
        // SELECT ... FROM jobsearch.job WHERE ts @@ TO_TSQUERY('english', CONCAT(':param_0', '&', ':param_1', '&', ...
        // ':paramN') ...
        TextSearch ts = getTextSearchSql(filter.getTerms());

        sql.append(" WHERE ").append(ts.query()).append("ORDER BY job_id DESC FETCH FIRST :size ROWS ONLY;");

        params = ts.params();
        params.addValue("size", filter.size());
        return jdbcTemplate.query(sql.toString(), params, jobRowMapper);
    }

    /**
     * Helper function that gets the query (part of it) and params (MapSqlParameterSource) for functions that make use
     * of text search capabilities.
     *
     * @param terms e.g. ["ruby", "developer"]
     * @return a TextSearch record with the query
     * (e.g. "ts @@ TO_TSQUERY('english', CONCAT(':param_0', '&', ':param_1')" and
     * and the appropiate list of params (e.g. "{ "param_0": "ruby", "param_1": "developer"})
     */
    private TextSearch getTextSearchSql(@NotNull String[] terms) {
        MapSqlParameterSource params = new MapSqlParameterSource();
        StringBuilder sql = new StringBuilder("ts @@ TO_TSQUERY('english', CONCAT(");

        for (int i = 0; i < terms.length; i++) {
            var termPlaceHolder = "param" + i;
            sql.append(":").append(termPlaceHolder);

            if (i < terms.length - 1) {
                sql.append(", '&', ");
            }

            params.addValue(termPlaceHolder, terms[i]);
        }
        sql.append("))");
        return new TextSearch(sql.toString(), params);
    }
}
