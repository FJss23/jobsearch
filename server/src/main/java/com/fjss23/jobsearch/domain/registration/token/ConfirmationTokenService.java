package com.fjss23.jobsearch.domain.registration.token;

import java.time.LocalDateTime;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class ConfirmationTokenService {

    private final ConfirmationTokenRepository confirmationTokenRepository;

    public ConfirmationTokenService(
        ConfirmationTokenRepository confirmationTokenRepository
    ) {
        this.confirmationTokenRepository = confirmationTokenRepository;
    }

    public void saveConfirmationToken(ConfirmationToken token) {
        confirmationTokenRepository.save(token);
    }

    public int setConfirmedAt(String token) {
        return confirmationTokenRepository.updateConfirmedAt(
            token,
            LocalDateTime.now()
        );
    }

    public Optional<ConfirmationToken> getToken(String token) {
        return confirmationTokenRepository.findByToken(token);
    }
}
