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

    public List<Tag> getTagsOfJobOffer(Long jobOfferId) {
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
        INNER JOIN jobsearch.joboffer_tag ON
            tag.tag_id = joboffer_tag.tag_id
        INNER JOIN jobsearch.joboffer ON
            joboffer_tag.joboffer_id = joboffer.joboffer_id
        WHERE joboffer.joboffer_id = :id;
        """;
        params.addValue("id", jobOfferId);
        return jdbcTemplate.query(sql, params, tagRowMapper);
    }
}
