package com.fjss23.jobsearch.user;

public record AppUserResponseDto(
    String firstName,
    String lastName,
    String email,
    String role
) {
}
