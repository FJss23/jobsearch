package com.fjss23.jobsearch.security.config;

import com.fjss23.jobsearch.user.AppUserService;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

@Configuration
public class SecurityConfig {

    private final AppUserService appUserService;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public SecurityConfig(AppUserService appUserService, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.appUserService = appUserService;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }


}
