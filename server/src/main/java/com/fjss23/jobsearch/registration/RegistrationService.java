package com.fjss23.jobsearch.registration;

import com.fjss23.jobsearch.user.AppUserService;
import org.springframework.stereotype.Service;

@Service
public class RegistrationService {

    private final AppUserService userService;

    public RegistrationService(AppUserService userService){
        this.userService = userService;
    }

    public String register(RegistrationRequestDto request) {
        return "";
    }
}
