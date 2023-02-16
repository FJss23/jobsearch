package com.fjss23.jobsearch.domain.user;

import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class AppUserRepository {

    Optional<AppUser> findByEmail(String email) {
        String sql = """
            SELECT
                appuser_id as id,
                first_name,
                last_name,
                email,
                password,
                role,
                locked,
                enabled,
                logged_at,
                company_id
            FROM jobsearch.appuser
            WHERE email = $1
            """;
        return null;
    }

    void save(AppUser user) {

    }

    public int enableAppUser(String email) {
        return 1;
    }
}
