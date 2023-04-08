package com.fjss23.jobsearch;

import com.fjss23.jobsearch.user.AppUser;
import com.fjss23.jobsearch.user.AppUserRepository;
import com.fjss23.jobsearch.user.AppUserRole;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.ApplicationListener;
import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component
public class SetupDataLoader implements ApplicationListener<ContextRefreshedEvent> {

    @Value("${admin.username}")
    private String adminUsername;

    @Value("${admin.password}")
    private String adminPassword;

    private final AppUserRepository appUserRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private boolean isSetup = false;

    public SetupDataLoader(AppUserRepository appUserRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.appUserRepository = appUserRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public void onApplicationEvent(ContextRefreshedEvent event) {
        if (isSetup) return;

        AppUser admin = new AppUser();
        String encodedPassword = bCryptPasswordEncoder.encode(adminPassword);

        admin.setEmail(adminUsername);
        admin.setPassword(encodedPassword);
        admin.setFirstName("Fran");
        admin.setLastName("Jobsearch");
        admin.setUserRole(AppUserRole.APP_ADMIN);
        admin.setEnabled(true);

        Optional<AppUser> user = appUserRepository.findByEmail(adminUsername);
        if (user.isEmpty()) {
            appUserRepository.save(admin);
        }

        isSetup = true;
    }
}
