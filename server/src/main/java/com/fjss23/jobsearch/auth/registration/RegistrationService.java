package com.fjss23.jobsearch.auth.registration;

import com.fjss23.jobsearch.auth.registration.payload.RegistrationRequest;
import com.fjss23.jobsearch.email.EmailService;
import com.fjss23.jobsearch.user.AppUser;
import com.fjss23.jobsearch.user.AppUserRole;
import com.fjss23.jobsearch.user.AppUserService;
import java.time.OffsetDateTime;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class RegistrationService {

    private final AppUserService appUserService;
    private final EmailService emailService;
    private final ConfirmationTokenService confirmationTokenService;

    @Value("${project.domain}")
    private String projectDomain;

    public RegistrationService(
            AppUserService appUserService,
            EmailService emailService,
            ConfirmationTokenService confirmationTokenService) {
        this.appUserService = appUserService;
        this.emailService = emailService;
        this.confirmationTokenService = confirmationTokenService;
    }

    public String register(RegistrationRequest request) {
        var appUser = new AppUser();
        appUser.setFirstName(request.firstName());
        appUser.setLastName(request.lastName());
        appUser.setEmail(request.email());
        appUser.setPassword(request.password());
        appUser.setUserRole(AppUserRole.CANDIDATE);
        appUser.setCreatedBy(request.email());

        var token = appUserService.signUpUser(appUser);
        var confirmationLink = projectDomain + "/api/v1/auth/registration/confirm?token=" + token;

        emailService.sendToken(appUser.getFirstName(), appUser.getEmail(), confirmationLink);
        return token;
    }

    @Transactional
    public String confirmToken(String token) {
        ConfirmationToken confirmationToken = confirmationTokenService
                .getToken(token)
                .orElseThrow(() -> new IllegalStateException("Token not found"));

        if (confirmationToken.getConfirmedAt() != null) throw new IllegalStateException("Email already confirmed");

        OffsetDateTime expiredAt = confirmationToken.getExpiresAt();
        if (expiredAt.isBefore(OffsetDateTime.now())) throw new IllegalStateException("Token expired");

        confirmationTokenService.setConfirmedAt(token);
        appUserService.enableAppUser(confirmationToken.getAppUserEmail());

        return "confirmed";
    }
}
