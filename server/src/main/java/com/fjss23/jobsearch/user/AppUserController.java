package com.fjss23.jobsearch.user;

import com.fjss23.jobsearch.ApiV1PrefixController;
import java.util.List;
import java.util.stream.Collectors;

import com.fjss23.jobsearch.user.payload.AppUserResponse;
import com.fjss23.jobsearch.user.payload.AppUserResponseMapper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.GetMapping;

@ApiV1PrefixController
public class AppUserController {

    private final AppUserService appUserService;
    private final AppUserResponseMapper appUserResponseMapper;

    private static final Logger logger = LoggerFactory.getLogger(AppUserController.class);

    public AppUserController(AppUserService appUserService, AppUserResponseMapper appUserResponseMapper) {
        this.appUserService = appUserService;
        this.appUserResponseMapper = appUserResponseMapper;
    }

    @PreAuthorize("hasAuthority('APP_ADMIN')")
    @GetMapping("/users")
    public List<AppUserResponse> getAllUsers() {
        return appUserService.getAllUsers().stream().map(appUserResponseMapper).collect(Collectors.toList());
    }
}
