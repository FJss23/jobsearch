package com.fjss23.jobsearch.user;

import java.util.Optional;
import javax.swing.text.html.Option;
import org.springframework.stereotype.Repository;

@Repository
public class AppUserRepository {

    Optional<AppUser> findByEmail(String email) {
        return null;
    }

    void save(AppUser user) {}
}
