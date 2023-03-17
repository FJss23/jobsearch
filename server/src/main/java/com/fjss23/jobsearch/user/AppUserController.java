package com.fjss23.jobsearch.user;

import com.fjss23.jobsearch.ApiV1PrefixController;
import java.util.List;
import java.util.stream.Collectors;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;

@ApiV1PrefixController
public class AppUserController {

    private static final Logger logger = LoggerFactory.getLogger(AppUserController.class);

    private final AppUserService appUserService;

    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @GetMapping("/users")
    public List<AppUserResponseDto> getAllUsers() {
        return appUserService.getAllUsers().stream()
                .map(user -> new AppUserResponseDto(
                        user.getFirstName(),
                        user.getLastName(),
                        user.getEmail(),
                        user.getUserRole().name()))
                .collect(Collectors.toList());
    }
}
