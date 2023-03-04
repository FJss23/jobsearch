package com.fjss23.jobsearch.authentication;

import com.fjss23.jobsearch.user.AppUserService;
import org.springframework.stereotype.Service;

@Service
public class AuthenticationService {

    private final AppUserService appUserService;

    public AuthenticationService(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    public void login(LoginRequestDto requestDto) {
        appUserService.getUser(requestDto.email(), requestDto.password());
    }
}
