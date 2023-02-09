package com.fjss23.jobsearch.user;

import org.springframework.stereotype.Repository;

import javax.swing.text.html.Option;
import java.util.Optional;

@Repository
public class AppUserRepository {

    Optional<AppUser> findByEmail(String email) {
        return null;
    }

    void save(AppUser user){

    }
}
