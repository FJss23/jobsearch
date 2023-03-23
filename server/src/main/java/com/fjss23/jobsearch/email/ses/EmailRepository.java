package com.fjss23.jobsearch.email.ses;

import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EmailRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<EmailNotification> emailSentRowMapper;

    public EmailRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.emailSentRowMapper = new BeanPropertyRowMapper<>(EmailNotification.class);
    }

    void save(EmailNotification emailSent) {
        String sql =
            """
        INSERT
        INTO jobsearch.email_sent(
            source,
            destination,
            event_type,
            message_id)
        VALUES(
            :source,
            :destination,
            :eventType
            :messageId);
        """;
        jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(emailSent));
    }

}
