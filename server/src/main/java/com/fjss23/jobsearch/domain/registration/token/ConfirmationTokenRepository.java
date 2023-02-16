package com.fjss23.jobsearch.domain.registration.token;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class ConfirmationTokenRepository {

    public void save(ConfirmationToken token) {}

    public int updateConfirmedAt(String token, LocalDateTime now) {
        return 1;
    }

    public Optional<ConfirmationToken> findByToken(String token) {
        return null;
    }
}
