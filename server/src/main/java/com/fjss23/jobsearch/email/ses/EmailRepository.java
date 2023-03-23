package com.fjss23.jobsearch.email.ses;

import com.fjss23.jobsearch.user.AppUser;
import org.springframework.jdbc.core.BeanPropertyRowMapper;
import org.springframework.jdbc.core.namedparam.BeanPropertySqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;

@Repository
public class EmailRepository {

    private final NamedParameterJdbcTemplate jdbcTemplate;
    private final BeanPropertyRowMapper<EmailSent> emailSentRowMapper;

    public EmailRepository(NamedParameterJdbcTemplate jdbcTemplate) {
        this.jdbcTemplate = jdbcTemplate;
        this.emailSentRowMapper = new BeanPropertyRowMapper<>(EmailSent.class);
    }

    void save(EmailSent emailSent) {
        String sql =
            """
        INSERT
        INTO jobsearch.email_sent(
            email,
            status)
        VALUES(
            :email,
            cast(:status.name as status));
        """;
        jdbcTemplate.update(sql, new BeanPropertySqlParameterSource(emailSent));
    }

}
