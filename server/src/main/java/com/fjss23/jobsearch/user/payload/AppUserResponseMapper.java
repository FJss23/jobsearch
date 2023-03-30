package com.fjss23.jobsearch.user.payload;

import java.util.function.Function;

import com.fjss23.jobsearch.user.AppUser;
import org.springframework.stereotype.Service;

@Service
public class AppUserResponseMapper implements Function<AppUser, AppUserResponse> {

    @Override
    public AppUserResponse apply(AppUser appUser) {
        return new AppUserResponse(
                appUser.getFirstName(),
                appUser.getLastName(),
                appUser.getEmail(),
                appUser.getUserRole().name(),
                appUser.getCreatedAt());
    }
}
