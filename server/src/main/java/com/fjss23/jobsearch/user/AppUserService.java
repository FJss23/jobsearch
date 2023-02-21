package com.fjss23.jobsearch.user;

import com.fjss23.jobsearch.registration.token.ConfirmationToken;
import com.fjss23.jobsearch.registration.token.ConfirmationTokenService;
import java.time.LocalDateTime;
import java.time.OffsetDateTime;
import java.util.UUID;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AppUserService implements UserDetailsService {

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;
    private final ConfirmationTokenService confirmationTokenService;

    private static final int VALID_TOKEN_TIME_IN_MINUTES = 20;

    public AppUserService(
        AppUserRepository appUserRepository,
        BCryptPasswordEncoder bCryptPasswordEncoder,
        ConfirmationTokenService confirmationTokenService
    ) {
        this.appUserRepository = appUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.confirmationTokenService = confirmationTokenService;
    }

    @Override
    public UserDetails loadUserByUsername(String email)
        throws UsernameNotFoundException {
        return appUserRepository
            .findByEmail(email)
            .orElseThrow(() ->
                new UsernameNotFoundException(
                    "User with email " + email + " not found"
                )
            );
    }

    public String signUpUser(AppUser user) {
        boolean exists = appUserRepository
            .findByEmail(user.getUsername())
            .isPresent();

        if (exists) {
            // TODO: Check if the same person is trying to register again
            throw new IllegalArgumentException("error.email.taken");
        }

        String encodedPassword = bCryptPasswordEncoder.encode(
            user.getPassword()
        );
        user.setPassword(encodedPassword);
        appUserRepository.save(user);

        String token = UUID.randomUUID().toString();
        var confirmationToken = new ConfirmationToken(
            token,
            user.getEmail(),
            OffsetDateTime.now(),
            OffsetDateTime.now().plusMinutes(VALID_TOKEN_TIME_IN_MINUTES)
        );

        confirmationTokenService.saveConfirmationToken(confirmationToken);

        return token;
    }

    public int enableAppUser(String email) {
        return appUserRepository.enableAppUser(email);
    }
}
