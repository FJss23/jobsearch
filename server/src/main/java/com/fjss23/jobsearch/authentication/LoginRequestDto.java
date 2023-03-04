package com.fjss23.jobsearch.authentication;

public record LoginRequestDto(
    String email,
    String password
) {
}
